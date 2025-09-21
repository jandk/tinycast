package be.twofold.tinycast;

public final class CastException extends RuntimeException {
    public CastException(String message) {
        super(message);
    }

    public CastException(String message, Throwable cause) {
        super(message, cause);
    }
}
