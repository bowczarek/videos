package bowczarek.videos.api;

/**
 * Created by bowczarek on 03.06.2017.
 */
public class NotAllowedFormatException extends RuntimeException {
    public NotAllowedFormatException() {
    }

    public NotAllowedFormatException(String message) {
        super(message);
    }

    public NotAllowedFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedFormatException(Throwable cause) {
        super(cause);
    }

    public NotAllowedFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
