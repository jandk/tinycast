package be.twofold.tinycast;

/**
 * Enumeration of property type identifiers used by the Cast file format.
 * <p>
 * Each constant defines:
 * <ul>
 *   <li>an unsigned 16-bit identifier value stored in the stream (little-endian)</li>
 *   <li>the byte size of a single element of that type</li>
 *   <li>the number of elements that make up one logical value (e.g. vectors)</li>
 * </ul>
 * The combination of {@code size} and {@code count} determines how many bytes are
 * consumed for a single value: {@code size * count}. For arrays, the element size is
 * multiplied by the array length.
 */
public enum CastPropertyID {
    /** 8-bit signed integer. Identifier 0x0062 ('b'). Size 1, count 1. */
    BYTE(0x0062, 1, 1),
    /** 16-bit signed integer. Identifier 0x0068 ('h'). Size 2, count 1. */
    SHORT(0x0068, 2, 1),
    /** 32-bit signed integer. Identifier 0x0069 ('i'). Size 4, count 1. */
    INTEGER_32(0x0069, 4, 1),
    /** 64-bit signed integer. Identifier 0x006c ('l'). Size 8, count 1. */
    INTEGER_64(0x006c, 8, 1),
    /** 32-bit IEEE-754 floating point. Identifier 0x0066 ('f'). Size 4, count 1. */
    FLOAT(0x0066, 4, 1),
    /** 64-bit IEEE-754 floating point. Identifier 0x0064 ('d'). Size 8, count 1. */
    DOUBLE(0x0064, 8, 1),
    /** UTF-8 string. Identifier 0x0073 ('s'). Size 0 indicates variable length. Count 1. */
    STRING(0x0073, 0, 1),
    /** 2D vector of floats (x, y). Identifier 0x7632 ('v2'). Size 8, count 2. */
    VECTOR_2(0x7632, 8, 2),
    /** 3D vector of floats (x, y, z). Identifier 0x7633 ('v3'). Size 12, count 3. */
    VECTOR_3(0x7633, 12, 3),
    /** 4D vector of floats (x, y, z, w). Identifier 0x7634 ('v4'). Size 16, count 4. */
    VECTOR_4(0x7634, 16, 4),
    ;

    private final short id;
    private final int size;
    private final int count;

    CastPropertyID(int id, int size, int count) {
        this.id = (short) id;
        this.size = size;
        this.count = count;
    }

    /**
     * Returns the 16-bit identifier for this property type.
     * <p>
     * The identifier is written to and read from Cast streams to indicate the
     * type of a property value. The value is stored in little-endian order.
     *
     * @return the short identifier associated with this property type
     */
    public short getId() {
        return id;
    }

    /**
     * Returns the size in bytes of a single element of this property type.
     * <p>
     * For scalar types (e.g., {@link #INTEGER_32}) the size corresponds to the
     * full value. For vector types (e.g., {@link #VECTOR_3}) this is the size of
     * one component; multiply by {@link #getCount()} to get the total bytes for a
     * single logical value.
     *
     * @return the element size in bytes
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the number of elements that make up one logical value for this type.
     * <p>
     * Scalars have a count of {@code 1}; vectors have counts of {@code 2}, {@code 3},
     * or {@code 4} depending on their dimensionality.
     *
     * @return the number of elements per value
     */
    public int getCount() {
        return count;
    }

    /**
     * Resolves a {@code CastPropertyID} from its 16-bit identifier value.
     * <p>
     * If the value is not recognized, a {@link CastException} is thrown.
     *
     * @param value the short identifier read from a Cast stream
     * @return the matching {@code CastPropertyID}
     * @throws CastException if the value does not map to a known property type
     */
    public static CastPropertyID fromValue(short value) throws CastException {
        switch (value) {
            case 0x0062:
                return BYTE;
            case 0x0068:
                return SHORT;
            case 0x0069:
                return INTEGER_32;
            case 0x006c:
                return INTEGER_64;
            case 0x0066:
                return FLOAT;
            case 0x0064:
                return DOUBLE;
            case 0x0073:
                return STRING;
            case 0x7632:
                return VECTOR_2;
            case 0x7633:
                return VECTOR_3;
            case 0x7634:
                return VECTOR_4;
            default:
                throw new CastException("Unknown CastPropertyID: " + value);
        }
    }
}
