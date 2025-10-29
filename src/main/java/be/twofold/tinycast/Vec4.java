package be.twofold.tinycast;

/**
 * Immutable 4D vector of single-precision floats.
 * <p>
 * Commonly used for quaternions (rotation) and RGBA colors in the Cast format. Instances are
 * value objects: equality and hash code are based on component values.
 */
public final class Vec4 {
    private final float x;
    private final float y;
    private final float z;
    private final float w;

    /**
     * Creates a vector with the given components.
     *
     * @param x the X component (or R for colors)
     * @param y the Y component (or G for colors)
     * @param z the Z component (or B for colors)
     * @param w the W component (or A for colors / W for quaternions)
     */
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Returns the X component.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the Y component.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the Z component.
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the W component.
     */
    public float getW() {
        return w;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec4)) {
            return false;
        }

        Vec4 other = (Vec4) obj;
        return Float.compare(x, other.x) == 0
            && Float.compare(y, other.y) == 0
            && Float.compare(z, other.z) == 0
            && Float.compare(w, other.w) == 0;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        result = 31 * result + Float.hashCode(w);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
