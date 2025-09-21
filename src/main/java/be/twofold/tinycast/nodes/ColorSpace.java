package be.twofold.tinycast.nodes;

public enum ColorSpace {
    LINEAR,

    SRGB;

    public static ColorSpace from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
