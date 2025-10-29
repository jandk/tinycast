package be.twofold.tinycast;

/**
 * Exception indicating a problem encountered while processing Cast data.
 * <p>
 * This exception is thrown when reading or writing Cast streams fails due to
 * malformed input, unknown identifiers, or other format-related issues.
 */
public final class CastException extends Exception {
    /**
     * Creates a new exception with the specified detail message.
     *
     * @param message a human-readable description of the error
     */
    public CastException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified detail message and cause.
     *
     * @param message a human-readable description of the error
     * @param cause   the underlying cause of this exception
     */
    public CastException(String message, Throwable cause) {
        super(message, cause);
    }
}
