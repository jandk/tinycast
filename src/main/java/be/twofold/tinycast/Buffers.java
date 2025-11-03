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

    static byte[] toByteArray(Buffer buffer) {
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
