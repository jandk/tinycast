package be.twofold.tinycast;

import java.nio.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

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

    public CastNodeID getIdentifier() {
        return identifier;
    }

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
            createProperty(CastPropertyID.INT, name, (int) l);
        }
    }

    void createIntBufferProperty(String name, Buffer value) {
        Buffer buffer = Buffers.shrink(value);
        if (buffer instanceof ByteBuffer) {
            createProperty(CastPropertyID.BYTE, name, buffer);
        } else if (buffer instanceof ShortBuffer) {
            createProperty(CastPropertyID.SHORT, name, buffer);
        } else if (buffer instanceof IntBuffer) {
            createProperty(CastPropertyID.INT, name, buffer);
        } else {
            throw new IllegalArgumentException("Only integral buffers supported");
        }
    }

    boolean parseBoolean(Object value) {
        int i = ((Number) value).intValue();
        switch (i) {
            case 0:
                return true;
            case 1:
                return false;
            default:
                throw new IllegalArgumentException("Expected 0 or 1 but got " + i);
        }
    }
}
