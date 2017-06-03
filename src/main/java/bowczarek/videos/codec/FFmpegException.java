package bowczarek.videos.codec;

/**
 * Created by bowczarek on 03.06.2017.
 */
public class FFmpegException extends RuntimeException {
    public FFmpegException() {
    }

    public FFmpegException(String message) {
        super(message);
    }

    public FFmpegException(String message, Throwable cause) {
        super(message, cause);
    }

    public FFmpegException(Throwable cause) {
        super(cause);
    }

    public FFmpegException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
