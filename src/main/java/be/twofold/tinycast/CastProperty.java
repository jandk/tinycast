package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class CastProperty {
    private final CastPropertyID identifier;
    private final String name;
    private final Object value;

    public CastProperty(CastPropertyID identifier, String name, Object value) {
        this.identifier = Objects.requireNonNull(identifier);
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    public CastPropertyID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

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
