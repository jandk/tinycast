package be.twofold.tinycast;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

final class BinaryWriter implements Closeable {
    private final ByteBuffer buffer = ByteBuffer
        .allocate(8)
        .order(ByteOrder.LITTLE_ENDIAN);

    private final OutputStream out;

    BinaryWriter(OutputStream out) {
        this.out = Objects.requireNonNull(out);
    }

    void writeByte(byte b) throws IOException {
        out.write(b);
    }

    void writeShort(short value) throws IOException {
        buffer.putShort(0, value);
        out.write(buffer.array(), 0, Short.BYTES);
    }

    void writeInt(int value) throws IOException {
        buffer.putInt(0, value);
        out.write(buffer.array(), 0, Integer.BYTES);
    }

    void writeLong(long value) throws IOException {
        buffer.putLong(0, value);
        out.write(buffer.array(), 0, Long.BYTES);
    }

    void writeFloat(float value) throws IOException {
        buffer.putFloat(0, value);
        out.write(buffer.array(), 0, Float.BYTES);
    }

    void writeDouble(double value) throws IOException {
        buffer.putDouble(0, value);
        out.write(buffer.array(), 0, Double.BYTES);
    }

    void writeBytes(byte[] bytes) throws IOException {
        out.write(bytes);
    }

    @Override
    public void close() throws IOException {
        out.flush();
    }
}
