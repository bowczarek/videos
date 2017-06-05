package bowczarek.videos.api;

import bowczarek.videos.storage.FileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bowczarek on 03.06.2017.
 */
@ControllerAdvice(basePackageClasses = VideoFileController.class)
public class VideoFileControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotAllowedFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    String handleNotAllowedFormatException(HttpServletRequest request, Throwable ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({FileNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    String handleFileNotFoundException(HttpServletRequest request, Throwable ex) {
        return ex.getMessage();
    }
}
