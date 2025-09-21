package be.twofold.tinycast.nodes;

public enum UpAxis {
    X,

    Y,

    Z;

    public static UpAxis from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
