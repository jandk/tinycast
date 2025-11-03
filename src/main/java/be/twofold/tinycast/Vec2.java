package be.twofold.tinycast;

/**
 * Immutable 2D vector of single-precision floats.
 * <p>
 * Commonly used for UVs, 2D positions, and sizes in the Cast format. Instances are
 * value objects: equality and hash code are based on component values.
 */
public final class Vec2 {
    private final float x;
    private final float y;

    /**
     * Creates a vector with the given components.
     *
     * @param x the X component
     * @param y the Y component
     */
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X component.
     *
     * @return The X component.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the Y component.
     *
     * @return The Y component.
     */
    public float getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec2)) {
            return false;
        }

        Vec2 other = (Vec2) obj;
        return Float.compare(x, other.x) == 0
            && Float.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
