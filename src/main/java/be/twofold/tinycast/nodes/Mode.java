package be.twofold.tinycast.nodes;

public enum Mode {
    ABSOLUTE,

    ADDITIVE,

    RELATIVE;

    public static Mode from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
