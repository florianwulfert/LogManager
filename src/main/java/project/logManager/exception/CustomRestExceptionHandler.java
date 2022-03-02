package project.logManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.logManager.common.message.ErrorMessages;

@RestControllerAdvice
public class CustomRestExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String dateFormatExceptionHandler(DateFormatException ex) {
        return ErrorMessages.ILLEGAL_BIRTHDATE_FORMAT;
    }
}
