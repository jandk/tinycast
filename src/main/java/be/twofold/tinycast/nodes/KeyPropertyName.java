package be.twofold.tinycast.nodes;

public enum KeyPropertyName {
    BS,

    TX,

    TY,

    SX,

    TZ,

    SY,

    SZ,

    VB,

    RQ;

    public static KeyPropertyName from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
