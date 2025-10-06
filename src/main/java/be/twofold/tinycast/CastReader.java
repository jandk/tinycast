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

final class CastReader {
    private static final Map<CastNodeID, Set<String>> ARRAY_TYPES = Map.of(
        CastNodeID.MESH, Set.of("vp", "vn", "vt", "c%d", "u%d", "wb", "wv", "f"),
        CastNodeID.HAIR, Set.of("se", "pt"),
        CastNodeID.BLEND_SHAPE, Set.of("vi", "vp", "ts"),
        CastNodeID.CURVE, Set.of("kb", "kv"),
        CastNodeID.NOTIFICATION_TRACK, Set.of("kb")
    );

    private final BinaryReader reader;

    CastReader(BinaryReader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    static Cast read(InputStream in) throws CastException {
        try (BinaryReader reader = new BinaryReader(new BufferedInputStream(in))) {
            return new CastReader(reader).read();
        } catch (IOException e) {
            throw new CastException("Error reading cast file", e);
        }
    }

    private Cast read() throws CastException, IOException {
        int magic = reader.readInt();
        if (magic != 0x74736163) {
            throw new CastException("Invalid magic number: " + magic);
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

        List<CastNode> rootNodes = new ArrayList<>(rootNodeCount);
        for (int i = 0; i < rootNodeCount; i++) {
            rootNodes.add(readNode());
        }
        return new Cast(rootNodes);
    }

    private CastNode readNode() throws CastException, IOException {
        CastNodeID identifier = CastNodeID.fromValue(reader.readInt());
        int nodeSize = reader.readInt();
        long nodeHash = reader.readLong();
        int propertyCount = reader.readInt();
        int childCount = reader.readInt();

        Map<String, CastProperty> properties = new LinkedHashMap<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            CastProperty property = readProperty(identifier);
            properties.put(property.getName(), property);
        }

        List<CastNode> children = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            children.add(readNode());
        }

        return CastNodes.create(identifier, nodeHash, properties, children);
    }

    private CastProperty readProperty(CastNodeID typeId) throws CastException, IOException {
        CastPropertyID identifier = CastPropertyID.fromValue(reader.readShort());
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
            case INT:
                return reader.readInt();
            case LONG:
                return reader.readLong();
            case FLOAT:
                return reader.readFloat();
            case DOUBLE:
                return reader.readDouble();
            case STRING:
                return reader.readCString();
            case VECTOR2: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                return new Vec2(x, y);
            }
            case VECTOR3: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                float z = reader.readFloat();
                return new Vec3(x, y, z);
            }
            case VECTOR4: {
                float x = reader.readFloat();
                float y = reader.readFloat();
                float z = reader.readFloat();
                float w = reader.readFloat();
                return new Vec4(x, y, z, w);
            }
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Buffer readArray(CastPropertyID identifier, int arrayLength) throws IOException {
        ByteBuffer buffer = reader.readBuffer(arrayLength * identifier.getSize());
        switch (identifier) {
            case BYTE:
                return buffer;
            case SHORT:
                return buffer.asShortBuffer();
            case INT:
                return buffer.asIntBuffer();
            case LONG:
                return buffer.asLongBuffer();
            case FLOAT:
            case VECTOR2:
            case VECTOR3:
            case VECTOR4:
                return buffer.asFloatBuffer();
            case DOUBLE:
                return buffer.asDoubleBuffer();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
