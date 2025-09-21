package be.twofold.tinycast;

public final class Vec4 {
    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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
