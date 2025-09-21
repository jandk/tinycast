package be.twofold.tinycast;

public enum CastPropertyID {
    BYTE(0x0062, 1, 1),
    SHORT(0x0068, 2, 1),
    INT(0x0069, 4, 1),
    LONG(0x006c, 8, 1),
    FLOAT(0x0066, 4, 1),
    DOUBLE(0x0064, 8, 1),
    STRING(0x0073, 0, 1),
    VECTOR2(0x7632, 8, 2),
    VECTOR3(0x7633, 12, 3),
    VECTOR4(0x7634, 16, 4),
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

    public static CastPropertyID fromValue(short value) {
        switch (value) {
            case 0x0062:
                return BYTE;
            case 0x0068:
                return SHORT;
            case 0x0069:
                return INT;
            case 0x006c:
                return LONG;
            case 0x0066:
                return FLOAT;
            case 0x0064:
                return DOUBLE;
            case 0x0073:
                return STRING;
            case 0x7632:
                return VECTOR2;
            case 0x7633:
                return VECTOR3;
            case 0x7634:
                return VECTOR4;
            default:
                throw new IllegalArgumentException("Unknown CastPropertyID: " + value);
        }
    }
}
