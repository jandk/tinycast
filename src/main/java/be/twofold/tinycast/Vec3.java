package be.twofold.tinycast;

public final class Vec3 {
    private final float x;
    private final float y;
    private final float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

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
