package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Represents a single named property attached to a {@link CastNode}.
 * <p>
 * A property consists of:
 * <ul>
 *   <li>a {@linkplain #getIdentifier() type identifier} that dictates how the value is encoded,</li>
 *   <li>a UTF-8 {@linkplain #getName() name} used as the property key within a node,</li>
 *   <li>and a {@linkplain #getValue() value} whose Java type depends on the identifier.</li>
 * </ul>
 * The {@code value} is typically one of the following depending on {@link CastPropertyID}:
 * <ul>
 *   <li>{@link String} for {@link CastPropertyID#STRING}</li>
 *   <li>boxed primitives for scalar types (e.g. {@link Integer}, {@link Long}, {@link Float}, {@link Double})</li>
 *   <li>{@link Vec2}, {@link Vec3}, {@link Vec4} for vector types</li>
 *   <li>NIO buffers for arrays of scalars/vectors; the buffer {@code limit()} encodes the element count</li>
 * </ul>
 * Instances are immutable.
 */
public final class CastProperty {
    private final CastPropertyID identifier;
    private final String name;
    private final Object value;

    /**
     * Creates a new property.
     *
     * @param identifier the property type identifier; must not be {@code null}
     * @param name       the UTF-8 key/name of the property; must not be {@code null}
     * @param value      the property value; must not be {@code null}. The expected runtime type
     *                   depends on {@code identifier} (see class documentation).
     * @throws NullPointerException if any argument is {@code null}
     */
    public CastProperty(CastPropertyID identifier, String name, Object value) {
        this.identifier = Objects.requireNonNull(identifier);
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns the property type identifier that dictates how the value is encoded in a Cast stream.
     *
     * @return the {@link CastPropertyID} for this property
     */
    public CastPropertyID getIdentifier() {
        return identifier;
    }

    /**
     * Returns the property name (key) as stored in the Cast stream.
     * <p>
     * The name is encoded in UTF-8 when written. Names are case-sensitive within a node.
     *
     * @return the UTF-8 property name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of this property.
     * <p>
     * The runtime type depends on {@link #getIdentifier()}. See the class documentation for
     * the expected mappings. For array values, a suitable NIO {@link java.nio.Buffer} is used
     * and the buffer's {@code limit()} determines the number of elements.
     *
     * @return the property value (never {@code null})
     */
    public Object getValue() {
        return value;
    }

    /**
     * Computes the number of bytes this property contributes to the Cast stream payload.
     * <p>
     * The calculation includes:
     * <ul>
     *   <li>8 bytes of overhead for the property header,</li>
     *   <li>the UTF-8 byte length of {@link #getName()},</li>
     *   <li>and the encoded value size. For {@link CastPropertyID#STRING}, this is the
     *       UTF-8 byte length of the string plus a trailing {@code NUL} terminator. For
     *       other types, this is {@code getArrayLength() * identifier.getSize()}.</li>
     * </ul>
     *
     * @return the payload size in bytes for this property
     */
    public int getLength() {
        int length = 0x08;
        length += name.getBytes(StandardCharsets.UTF_8).length;
        if (identifier == CastPropertyID.STRING) {
            length += ((String) value).getBytes(StandardCharsets.UTF_8).length + 1;
        } else {
            length += getArrayLength() * identifier.getSize();
        }
        return length;
    }

    /**
     * Returns the number of logical values represented by {@link #getValue()} when it is an array.
     * <p>
     * For non-array values (i.e., when the value is not a {@link Buffer}), this returns {@code 1}.
     * When the value is a NIO {@link Buffer}, its {@code limit()} indicates the number of stored
     * elements. For vector types, {@link CastPropertyID#getCount()} elements form one logical value,
     * so the returned array length is {@code limit() / identifier.getCount()}.
     *
     * @return the array length in number of logical values (at least {@code 1})
     * @throws IllegalArgumentException if the buffer limit is not a multiple of {@code identifier.getCount()}
     */
    public int getArrayLength() {
        if (!(value instanceof Buffer)) {
            return 1;
        }
        int limit = ((Buffer) value).limit();
        if (limit % identifier.getCount() != 0) {
            throw new IllegalArgumentException("Limit of buffer is not a multiple of count");
        }
        return limit / identifier.getCount();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CastProperty)) {
            return false;
        }

        CastProperty other = (CastProperty) obj;
        return identifier == other.identifier
            && name.equals(other.name)
            && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + identifier.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CastProperty(" +
            "identifier=" + identifier + ", " +
            "name=" + name + ", " +
            "value=" + value + ")";
    }
}
