package marshmallow.util;

import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.ErrorResponse;
import net.dv8tion.jda.core.requests.RestAction;
import okhttp3.internal.http2.StreamResetException;
import org.apache.http.ConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.function.Consumer;

public class RestActionUtil {

    /**
     * The slf4j rest action utility logger instance.
     */
    public static final Logger log = LoggerFactory.getLogger(RestActionUtil.class);

    /**
     * This function does nothing other than work as a rest
     * action failure consumer that ignores the failure.
     */
    public static final Consumer<Throwable> ignore = ignored -> {
        // Nothing to see here
    };

    /**
     * This functions passes on the error to the default failure,
     * unless it meets some special caterina, in which case it
     * may either be ignored, or handled differently.
     */
    public static final Consumer<Throwable> handleMessageCreate = error -> {
        if (error == null) return;

        if (error instanceof ErrorResponseException) {
            if (((ErrorResponseException) error).getErrorCode() == ErrorResponse.MISSING_ACCESS.getCode()) {
                return; // Ignore missing access errors
            }
        }

        if (isOkHttpConnectionError(error)) {
            log.warn("RestAction queue resulted in a {}: {}",
                    error.getClass().getSimpleName(), error.getMessage(), error
            );
            return; // Ignore OkHttp connection errors and simply warn the user instead
        }

        RestAction.DEFAULT_FAILURE.accept(error);
    };

    private static boolean isOkHttpConnectionError(Throwable error) {
        return error instanceof StreamResetException
                || error instanceof SocketTimeoutException
                || error instanceof ConnectionClosedException;
    }
}
