package project.logManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** @author - EugenFriesen 14.02.2021 */
@RestControllerAdvice
public class RestExceptionHandler {

  @ResponseBody
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String parameterIsMissingHandler(MissingServletRequestParameterException ex) {
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String pathVariableIsMissingHandler(MethodArgumentTypeMismatchException ex) {
    return "Required path variable was not found or request param has wrong format! "
        + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String runtimeExceptionHandler(RuntimeException ex) {
    return ex.getMessage();
  }
}
