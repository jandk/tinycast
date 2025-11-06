package be.twofold.tinycast;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.function.Consumer;

final class Buffers {
    private Buffers() {
    }

    static Buffer shrink(Buffer buffer) {
        if (buffer instanceof ByteBuffer) {
            return buffer;
        }

        if (buffer instanceof ShortBuffer) {
            ShortBuffer slice = ((ShortBuffer) buffer).slice();

            int max = 0;
            while (slice.hasRemaining()) {
                max = Math.max(max, Short.toUnsignedInt(slice.get()));
            }

            slice.rewind();
            if (max <= 0xFF) {
                ByteBuffer result = ByteBuffer.allocate(slice.remaining());
                while (slice.hasRemaining()) {
                    result.put((byte) slice.get());
                }
                return result.flip();
            }
            return buffer;
        }

        if (buffer instanceof IntBuffer) {
            IntBuffer slice = ((IntBuffer) buffer).slice();

            long max = 0;
            while (slice.hasRemaining()) {
                max = Math.max(max, Integer.toUnsignedLong(slice.get()));
            }

            slice.rewind();
            if (max <= 0xFF) {
                ByteBuffer result = ByteBuffer.allocate(slice.remaining());
                while (slice.hasRemaining()) {
                    result.put((byte) slice.get());
                }
                return result.flip();
            }
            if (max <= 0xFFFF) {
                ShortBuffer result = ShortBuffer.allocate(slice.remaining());
                while (slice.hasRemaining()) {
                    result.put((short) slice.get());
                }
                return result.flip();
            }
            return buffer;
        }

        throw new UnsupportedOperationException("Only integral buffers are supported");
    }

    static byte[] toByteArray(Buffer buffer) {
        Buffer slice = buffer.slice();
        if (slice instanceof ByteBuffer) {
            return allocateAndApply(slice.remaining(), bb -> bb.put((ByteBuffer) slice));
        } else if (slice instanceof ShortBuffer) {
            return allocateAndApply(slice.remaining() * Short.BYTES, bb -> bb.asShortBuffer().put((ShortBuffer) slice));
        } else if (slice instanceof IntBuffer) {
            return allocateAndApply(slice.remaining() * Integer.BYTES, bb -> bb.asIntBuffer().put((IntBuffer) slice));
        } else if (slice instanceof LongBuffer) {
            return allocateAndApply(slice.remaining() * Long.BYTES, bb -> bb.asLongBuffer().put((LongBuffer) slice));
        } else if (slice instanceof FloatBuffer) {
            return allocateAndApply(slice.remaining() * Float.BYTES, bb -> bb.asFloatBuffer().put((FloatBuffer) slice));
        } else if (slice instanceof DoubleBuffer) {
            return allocateAndApply(slice.remaining() * Double.BYTES, bb -> bb.asDoubleBuffer().put((DoubleBuffer) slice));
        } else if (slice instanceof CharBuffer) {
            return allocateAndApply(slice.remaining() * Character.BYTES, bb -> bb.asCharBuffer().put((CharBuffer) slice));
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
