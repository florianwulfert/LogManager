package project.logManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomRestExceptionHandler {
  @ResponseBody
  @ExceptionHandler(DateFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String dateFormatExceptionHandler(DateFormatException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(ParameterNotPresentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String parameterNotPresentExceptionHandler(ParameterNotPresentException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(UserNotAllowedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String userNotAllowedExceptionHandler(UserNotAllowedException ex) {
    return ex.getMessage();
  }
}
