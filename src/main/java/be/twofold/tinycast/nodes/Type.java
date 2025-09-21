package be.twofold.tinycast.nodes;

public enum Type {
    PBR;

    public static Type from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
