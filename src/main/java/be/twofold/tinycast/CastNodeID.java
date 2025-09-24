package be.twofold.tinycast;

public enum CastNodeID {
    ROOT(0x746f6f72),
    MODEL(0x6c646f6d),
    MESH(0x6873656d),
    HAIR(0x72696168),
    BLEND_SHAPE(0x68736c62),
    SKELETON(0x6c656b73),
    BONE(0x656e6f62),
    IK_HANDLE(0x64686b69),
    CONSTRAINT(0x74736e63),
    ANIMATION(0x6d696e61),
    CURVE(0x76727563),
    CURVE_MODE_OVERRIDE(0x564f4d43),
    NOTIFICATION_TRACK(0x6669746e),
    MATERIAL(0x6c74616d),
    FILE(0x656c6966),
    COLOR(0x726c6f63),
    INSTANCE(0x74736e69),
    METADATA(0x6174656d),
    ;

    private final int id;

    CastNodeID(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CastNodeID fromValue(int value) throws CastException {
        switch (value) {
            case 0x746f6f72:
                return ROOT;
            case 0x6c646f6d:
                return MODEL;
            case 0x6873656d:
                return MESH;
            case 0x72696168:
                return HAIR;
            case 0x68736c62:
                return BLEND_SHAPE;
            case 0x6c656b73:
                return SKELETON;
            case 0x656e6f62:
                return BONE;
            case 0x64686b69:
                return IK_HANDLE;
            case 0x74736e63:
                return CONSTRAINT;
            case 0x6d696e61:
                return ANIMATION;
            case 0x76727563:
                return CURVE;
            case 0x564f4d43:
                return CURVE_MODE_OVERRIDE;
            case 0x6669746e:
                return NOTIFICATION_TRACK;
            case 0x6c74616d:
                return MATERIAL;
            case 0x656c6966:
                return FILE;
            case 0x726c6f63:
                return COLOR;
            case 0x74736e69:
                return INSTANCE;
            case 0x6174656d:
                return METADATA;
            default:
                throw new CastException("Unknown CastNodeID: " + value);
        }
    }
}
