package be.twofold.tinycast;

public enum CastPropertyID {
    BYTE(0x0062, 1, 1),
    SHORT(0x0068, 2, 1),
    INTEGER_32(0x0069, 4, 1),
    INTEGER_64(0x006c, 8, 1),
    FLOAT(0x0066, 4, 1),
    DOUBLE(0x0064, 8, 1),
    STRING(0x0073, 0, 1),
    VECTOR_2(0x7632, 8, 2),
    VECTOR_3(0x7633, 12, 3),
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

    public short getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

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
