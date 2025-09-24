package be.twofold.tinycast;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

final class BinaryReader implements Closeable {
    private final ByteBuffer buffer = ByteBuffer
        .allocate(8)
        .order(ByteOrder.LITTLE_ENDIAN);

    private final InputStream in;

    BinaryReader(InputStream in) {
        this.in = Objects.requireNonNull(in);
    }

    byte readByte() throws IOException {
        int read = in.read();
        if (read < 0) {
            throw new EOFException("Unexpected end of stream");
        }
        return (byte) read;
    }

    short readShort() throws IOException {
        buffer(Short.BYTES);
        return buffer.getShort(0);
    }

    int readInt() throws IOException {
        buffer(Integer.BYTES);
        return buffer.getInt(0);
    }

    long readLong() throws IOException {
        buffer(Long.BYTES);
        return buffer.getLong(0);
    }

    float readFloat() throws IOException {
        buffer(Float.BYTES);
        return buffer.getFloat(0);
    }

    double readDouble() throws IOException {
        buffer(Double.BYTES);
        return buffer.getDouble(0);
    }

    String readCString() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            byte b = readByte();
            if (b == 0) {
                break;
            }
            out.write(b);
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    String readString(int length) throws IOException {
        byte[] bytes = readBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    ByteBuffer readBuffer(int length) throws IOException {
        byte[] bytes = readBytes(length);
        return ByteBuffer.wrap(bytes)
            .order(ByteOrder.LITTLE_ENDIAN);
    }

    private void buffer(int length) throws IOException {
        int read = in.read(buffer.array(), 0, length);
        if (read != length) {
            throw new EOFException("Expected " + length + " bytes but got " + read);
        }
    }

    private byte[] readBytes(int length) throws IOException {
        byte[] bytes = in.readNBytes(length);
        if (bytes.length != length) {
            throw new EOFException("Expected " + length + " bytes but got " + bytes.length);
        }
        return bytes;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
