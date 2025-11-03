package be.twofold.tinycast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

final class CastWriter {
    private final BinaryWriter writer;

    CastWriter(BinaryWriter writer) {
        this.writer = Objects.requireNonNull(writer);
    }

    static void write(Cast cast, OutputStream out) throws CastException {
        try (BinaryWriter writer = new BinaryWriter(new BufferedOutputStream(out))) {
            new CastWriter(writer).write(cast);
        } catch (IOException e) {
            throw new CastException("Error writing cast file", e);
        }
    }

    private void write(Cast cast) throws IOException {
        writer.writeInt(0x74736163); // magic
        writer.writeInt(1);          // version
        writer.writeInt(cast.size());      // rootNodeCount
        writer.writeInt(0);          // flags

        for (CastNode rootNode : cast) {
            writeNode(rootNode);
        }
    }

    private void writeNode(CastNode node) throws IOException {
        writer.writeInt(node.getIdentifier().getId());
        writer.writeInt(node.getLength());
        writer.writeLong(node.getHash());
        writer.writeInt(node.properties.size());
        writer.writeInt(node.children.size());

        for (CastProperty property : node.properties.values()) {
            writeProperty(property);
        }

        for (CastNode child : node.children) {
            writeNode(child);
        }
    }

    private void writeProperty(CastProperty property) throws IOException {
        byte[] rawName = property.getName().getBytes(StandardCharsets.UTF_8);
        writer.writeShort(property.getIdentifier().getId()); // identifier
        writer.writeShort((short) rawName.length);           // nameSize
        writer.writeInt(property.getArrayLength());          // arrayLength
        writer.writeBytes(rawName);

        if (!(property.getValue() instanceof Buffer)) {
            writeSingle(property.getIdentifier(), property.getValue());
        } else {
            writer.writeBytes(Buffers.toByteArray((Buffer) property.getValue()));
        }
    }

    private void writeSingle(CastPropertyID identifier, Object value) throws IOException {
        switch (identifier) {
            case BYTE:
                writer.writeByte((byte) value);
                break;
            case SHORT:
                writer.writeShort((short) value);
                break;
            case INTEGER_32:
                writer.writeInt((int) value);
                break;
            case INTEGER_64:
                writer.writeLong((long) value);
                break;
            case FLOAT:
                writer.writeFloat((float) value);
                break;
            case DOUBLE:
                writer.writeDouble((double) value);
                break;
            case STRING:
                writer.writeBytes(value.toString().getBytes(StandardCharsets.UTF_8));
                writer.writeByte((byte) 0);
                break;
            case VECTOR_2: {
                Vec2 vec = (Vec2) value;
                writer.writeFloat(vec.getX());
                writer.writeFloat(vec.getY());
                break;
            }
            case VECTOR_3: {
                Vec3 vec = (Vec3) value;
                writer.writeFloat(vec.getX());
                writer.writeFloat(vec.getY());
                writer.writeFloat(vec.getZ());
                break;
            }
            case VECTOR_4: {
                Vec4 vec = (Vec4) value;
                writer.writeFloat(vec.getX());
                writer.writeFloat(vec.getY());
                writer.writeFloat(vec.getZ());
                writer.writeFloat(vec.getW());
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
    }
}
