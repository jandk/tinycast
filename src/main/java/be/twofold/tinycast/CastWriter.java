package be.twofold.tinycast;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

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
        writer.writeInt(1); // version
        writer.writeInt(cast.size()); // rootNodeCount
        writer.writeInt(0); // flags

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
        writer.writeShort((short) rawName.length); // nameSize
        writer.writeInt(property.getArrayLength()); // arrayLength
        writer.writeBytes(rawName);

        if (!(property.getValue() instanceof Buffer)) {
            writeSingle(property.getIdentifier(), property.getValue());
        } else {
            writer.writeBytes(toByteArray((Buffer) property.getValue()));
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
            case INT:
                writer.writeInt((int) value);
                break;
            case LONG:
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
            case VECTOR2: {
                Vec2 vec = (Vec2) value;
                writer.writeFloat(vec.getX());
                writer.writeFloat(vec.getY());
                break;
            }
            case VECTOR3: {
                Vec3 vec = (Vec3) value;
                writer.writeFloat(vec.getX());
                writer.writeFloat(vec.getY());
                writer.writeFloat(vec.getZ());
                break;
            }
            case VECTOR4: {
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

    private static byte[] toByteArray(Buffer buffer) {
        buffer.rewind();

        if (buffer instanceof ByteBuffer) {
            return allocateAndApply(buffer.limit(), bb -> bb.put((ByteBuffer) buffer));
        } else if (buffer instanceof ShortBuffer) {
            return allocateAndApply(buffer.limit() * Short.BYTES, bb -> bb.asShortBuffer().put((ShortBuffer) buffer));
        } else if (buffer instanceof IntBuffer) {
            return allocateAndApply(buffer.limit() * Integer.BYTES, bb -> bb.asIntBuffer().put((IntBuffer) buffer));
        } else if (buffer instanceof LongBuffer) {
            return allocateAndApply(buffer.limit() * Long.BYTES, bb -> bb.asLongBuffer().put((LongBuffer) buffer));
        } else if (buffer instanceof FloatBuffer) {
            return allocateAndApply(buffer.limit() * Float.BYTES, bb -> bb.asFloatBuffer().put((FloatBuffer) buffer));
        } else if (buffer instanceof DoubleBuffer) {
            return allocateAndApply(buffer.limit() * Double.BYTES, bb -> bb.asDoubleBuffer().put((DoubleBuffer) buffer));
        } else if (buffer instanceof CharBuffer) {
            return allocateAndApply(buffer.limit() * Character.BYTES, bb -> bb.asCharBuffer().put((CharBuffer) buffer));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static byte[] allocateAndApply(int capacity, Consumer<ByteBuffer> consumer) {
        ByteBuffer byteBuffer = ByteBuffer
            .allocate(capacity)
            .order(ByteOrder.LITTLE_ENDIAN);
        consumer.accept(byteBuffer);
        return byteBuffer.array();
    }
}
