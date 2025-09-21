package be.twofold.tinycast.nodes;

public enum ConstraintType {
    SC,

    OR,

    PT;

    public static ConstraintType from(Object o) {
        return valueOf(o.toString().toUpperCase());
    }
}
