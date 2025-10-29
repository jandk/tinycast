package be.twofold.tinycast;

/**
 * Immutable 3D vector of single-precision floats.
 * <p>
 * Commonly used for positions, normals, and scales in the Cast format. Instances are
 * value objects: equality and hash code are based on component values.
 */
public final class Vec3 {
    private final float x;
    private final float y;
    private final float z;

    /**
     * Creates a vector with the given components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec3)) {
            return false;
        }

        Vec3 other = (Vec3) obj;
        return Float.compare(x, other.x) == 0
            && Float.compare(y, other.y) == 0
            && Float.compare(z, other.z) == 0;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
