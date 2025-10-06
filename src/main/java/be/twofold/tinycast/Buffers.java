package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

final class Buffers {
    private Buffers() {
    }

    static Buffer shrink(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            return buffer;
        }

        if (buffer instanceof ShortBuffer) {
            ShortBuffer duplicate = ((ShortBuffer) buffer).duplicate().rewind();

            int max = 0;
            while (duplicate.hasRemaining()) {
                max = Math.max(max, Short.toUnsignedInt(duplicate.get()));
            }

            duplicate.rewind();
            if (max <= 0xFF) {
                ByteBuffer result = ByteBuffer.allocate(duplicate.limit());
                while (duplicate.hasRemaining()) {
                    result.put((byte) duplicate.get());
                }
                return result.flip();
            }
            return buffer;
        }

        if (buffer instanceof IntBuffer) {
            IntBuffer duplicate = ((IntBuffer) buffer).duplicate().rewind();

            long max = 0;
            while (duplicate.hasRemaining()) {
                max = Math.max(max, Integer.toUnsignedLong(duplicate.get()));
            }

            duplicate.rewind();
            if (max <= 0xFF) {
                ByteBuffer result = ByteBuffer.allocate(duplicate.limit());
                while (duplicate.hasRemaining()) {
                    result.put((byte) duplicate.get());
                }
                return result.flip();
            }
            if (max <= 0xFFFF) {
                ShortBuffer result = ShortBuffer.allocate(duplicate.limit());
                while (duplicate.hasRemaining()) {
                    result.put((short) duplicate.get());
                }
                return result.flip();
            }
            return buffer;
        }

        throw new UnsupportedOperationException("Only integral buffers are supported");
    }
}
