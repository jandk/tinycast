package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base class for all Cast nodes in the Cast file format.
 * <p>
 * A Cast node represents a data structure in the Cast format hierarchy. Nodes are identified
 * by a unique {@link CastNodeID} that determines the type and purpose of the node, and a
 * unique 64-bit hash value used for linking nodes together (e.g., linking materials to meshes).
 * <p>
 * Each node contains:
 * <ul>
 *   <li>An identifier specifying the node type (Root, Model, Mesh, Material, etc.)</li>
 *   <li>A unique hash for cross-referencing</li>
 *   <li>A collection of properties containing the node's data</li>
 *   <li>A list of child nodes forming a hierarchy</li>
 * </ul>
 * <p>
 * Nodes follow a stack layout (FILO order) in the Cast file format, where properties
 * always precede a node's children in the serialized form.
 *
 * @see CastNodeID
 * @see CastProperty
 */
public abstract class CastNode {
    private final CastNodeID identifier;
    private final long hash;
    final AtomicLong hasher;
    final Map<String, CastProperty> properties;
    final List<CastNode> children;

    private CastNode(CastNodeID identifier, long hash, AtomicLong hasher, Map<String, CastProperty> properties, List<CastNode> children) {
        this.identifier = Objects.requireNonNull(identifier);
        this.hash = hash;
        this.hasher = hasher;
        this.properties = new LinkedHashMap<>(properties);
        this.children = new ArrayList<>(children);
    }

    CastNode(CastNodeID identifier, AtomicLong hasher) {
        this(identifier, hasher.getAndIncrement(), hasher, Map.of(), List.of());
    }

    CastNode(CastNodeID identifier, long hash, Map<String, CastProperty> properties, List<CastNode> children) {
        this(identifier, hash, null, properties, children);
    }

    /**
     * Returns the identifier that specifies the type of this node.
     * <p>
     * The identifier is used to determine which class the node uses and how to handle
     * its data during serialization and deserialization. Common identifiers include
     * Root, Model, Mesh, Skeleton, Material, and Animation.
     *
     * @return the node's type identifier
     */
    public CastNodeID getIdentifier() {
        return identifier;
    }

    /**
     * Returns the unique hash value for this node.
     * <p>
     * The hash acts as a unique identifier for linking nodes together. For example,
     * a mesh node can reference a material node by storing the material's hash value
     * in its properties. This enables relationships between nodes without requiring
     * direct object references in the file format.
     *
     * @return the unique 64-bit hash value
     */
    public long getHash() {
        return hash;
    }

    int getLength() {
        int result = 24;
        for (CastProperty property : properties.values()) {
            result += property.getLength();
        }
        for (CastNode child : children) {
            result += child.getLength();
        }
        return result;
    }

    <T extends CastNode> Optional<T> getChildOfType(Class<T> type) {
        return children.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .findFirst();
    }

    <T extends CastNode> List<T> getChildrenOfType(Class<T> type) {
        return children.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .collect(Collectors.toUnmodifiableList());
    }

    <T> Optional<T> getProperty(String name, Function<Object, ? extends T> mapper) {
        return Optional.ofNullable(properties.get(name))
            .map(p -> mapper.apply(p.getValue()));
    }

    Optional<Integer> getIntProperty(String name) {
        return Optional.ofNullable(properties.get(name))
            .map(p -> {
                if (p.getValue() instanceof Byte) {
                    return Byte.toUnsignedInt((Byte) p.getValue());
                } else if (p.getValue() instanceof Short) {
                    return Short.toUnsignedInt((Short) p.getValue());
                } else if (p.getValue() instanceof Integer) {
                    return (Integer) p.getValue();
                } else {
                    throw new UnsupportedOperationException();
                }
            });
    }

    <T extends CastNode> T createChild(T child) {
        children.add(child);
        return child;
    }

    void createProperty(CastPropertyID identifier, String name, Object value) {
        properties.put(name, new CastProperty(identifier, name, value));
    }

    void createIntProperty(String name, int value) {
        long l = Integer.toUnsignedLong(value);
        if (l <= 0xFF) {
            createProperty(CastPropertyID.BYTE, name, (byte) l);
        } else if (l <= 0xFFFF) {
            createProperty(CastPropertyID.SHORT, name, (short) l);
        } else {
            createProperty(CastPropertyID.INTEGER_32, name, (int) l);
        }
    }

    void createIntBufferProperty(String name, Buffer value) {
        Buffer buffer = Buffers.shrink(value);
        if (buffer instanceof ByteBuffer) {
            createProperty(CastPropertyID.BYTE, name, buffer);
        } else if (buffer instanceof ShortBuffer) {
            createProperty(CastPropertyID.SHORT, name, buffer);
        } else if (buffer instanceof IntBuffer) {
            createProperty(CastPropertyID.INTEGER_32, name, buffer);
        } else {
            throw new IllegalArgumentException("Only integral buffers supported");
        }
    }

    boolean parseBoolean(Object value) {
        int i = ((Number) value).intValue();
        switch (i) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Expected 0 or 1 but got " + i);
        }
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof CastNode)) {
            return false;
        }

        CastNode other = (CastNode) obj;
        return hash == other.hash
            && identifier == other.identifier
            && properties.equals(other.properties)
            && children.equals(other.children);
    }

    @Override
    public final int hashCode() {
        int result = 1;
        result = 31 * result + identifier.hashCode();
        result = 31 * result + Long.hashCode(hash);
        result = 31 * result + properties.hashCode();
        result = 31 * result + children.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CastNode(" +
            "identifier=" + identifier + ", " +
            "hash=" + hash + ", " +
            properties.size() + " properties, " +
            children.size() + " children" +
            ")";
    }
}
