package be.twofold.tinycast.nodes;

public enum SkinningMethod {
    LINEAR,

    QUATERNION;

    public static SkinningMethod from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
