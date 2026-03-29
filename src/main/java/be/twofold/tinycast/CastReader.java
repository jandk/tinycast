package be.twofold.tinycast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

final class CastReader {
    private static final Map<CastNodeID, Set<String>> ARRAY_TYPES = Map.of(
        CastNodeID.MESH, Set.of("vp", "vn", "vt", "c%d", "u%d", "wb", "wv", "f"),
        CastNodeID.HAIR, Set.of("se", "pt"),
        CastNodeID.BLEND_SHAPE, Set.of("vi", "vp", "ts"),
        CastNodeID.CURVE, Set.of("kb", "kv"),
        CastNodeID.NOTIFICATION_TRACK, Set.of("kb")
    );

    private final BinaryReader reader;
    private long maxHash = 0;

    CastReader(BinaryReader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    static Cast read(InputStream in) throws CastException {
        try (BinaryReader reader = new BinaryReader(new BufferedInputStream(in))) {
            return new CastReader(reader).read();
        } catch (CastException e) {
            throw e;
        } catch (IOException e) {
            throw new CastException("Error reading cast file", e);
        }
    }

    private Cast read() throws IOException {
        int magic = reader.readInt();
        if (magic != 0x74736163) {
            throw new CastException("Invalid magic number: 0x" + Integer.toHexString(magic));
        }

        int version = reader.readInt();
        if (version != 1) {
            throw new CastException("Invalid version: " + version);
        }

        int rootNodeCount = reader.readInt();
        int flags = reader.readInt();
        if (flags != 0) {
            throw new CastException("Invalid flags: " + flags);
        }

        AtomicLong hasher = new AtomicLong();
        List<CastNode> rootNodes = new ArrayList<>(rootNodeCount);
        for (int i = 0; i < rootNodeCount; i++) {
            rootNodes.add(readNode(hasher));
        }
        hasher.set(maxHash + 1);
        return new Cast(hasher, rootNodes);
    }

    private CastNode readNode(AtomicLong hasher) throws IOException {
        CastNodeID identifier;
        try {
            identifier = CastNodeID.fromValue(reader.readInt());
        } catch (IllegalArgumentException e) {
            throw new CastException(e.getMessage());
        }
        int nodeSize = reader.readInt();
        long nodeHash = reader.readLong();
        if (Long.compareUnsigned(nodeHash, maxHash) > 0) {
            maxHash = nodeHash;
        }
        int propertyCount = reader.readInt();
        int childCount = reader.readInt();

        Map<String, CastProperty> properties = new LinkedHashMap<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            CastProperty property = readProperty(identifier);
            properties.put(property.getName(), property);
        }

        List<CastNode> children = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            children.add(readNode(hasher));
        }

        return CastNodes.create(identifier, hasher, nodeHash, properties, children);
    }

    private CastProperty readProperty(CastNodeID typeId) throws IOException {
        CastPropertyID identifier;
        try {
            identifier = CastPropertyID.fromValue(reader.readShort());
        } catch (IllegalArgumentException e) {
            throw new CastException(e.getMessage());
        }
        short nameSize = reader.readShort();
        int arrayLength = reader.readInt();

        String name = reader.readString(nameSize);
        boolean isArray = ARRAY_TYPES.getOrDefault(typeId, Set.of()).contains(name)
            || (typeId == CastNodeID.MESH && (name.startsWith("c") || name.startsWith("u")));
        Object value = isArray ? readArray(identifier, arrayLength) : readSingle(identifier);

        return new CastProperty(identifier, name, value);
    }

    private Object readSingle(CastPropertyID identifier) throws IOException {
        switch (identifier) {
            case BYTE:
                return reader.readByte();
            case SHORT:
                return reader.readShort();
            case INTEGER_32:
                return reader.readInt();
            case INTEGER_64:
                return reader.readLong();
            case FLOAT:
                return reader.readFloat();
            case DOUBLE:
                return reader.readDouble();
            case STRING:
                return reader.readCString();
            case VECTOR_2: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                return new Vec2(x, y);
            }
            case VECTOR_3: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                float z = reader.readFloat();
                return new Vec3(x, y, z);
            }
            case VECTOR_4: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                float z = reader.readFloat();
                float w = reader.readFloat();
                return new Vec4(x, y, z, w);
            }
            default:
                throw new AssertionError();
        }
    }

    private Buffer readArray(CastPropertyID identifier, int arrayLength) throws IOException {
        ByteBuffer buffer = reader.readBuffer(arrayLength * identifier.getSize());
        switch (identifier) {
            case BYTE:
                return buffer;
            case SHORT:
                return buffer.asShortBuffer();
            case INTEGER_32:
                return buffer.asIntBuffer();
            case INTEGER_64:
                return buffer.asLongBuffer();
            case FLOAT:
            case VECTOR_2:
            case VECTOR_3:
            case VECTOR_4:
                return buffer.asFloatBuffer();
            case DOUBLE:
                return buffer.asDoubleBuffer();
            default:
                throw new AssertionError();
        }
    }
}
