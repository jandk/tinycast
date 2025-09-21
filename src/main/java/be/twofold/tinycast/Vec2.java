package be.twofold.tinycast;

public final class Vec2 {
    private final float x;
    private final float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

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
