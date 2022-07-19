package project.userFeaturePortal.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import project.userFeaturePortal.common.message.ErrorMessages;

/**
 * @author - EugenFriesen 14.02.2021
 */
@RestControllerAdvice
public class RestExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

  @ResponseBody
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String parameterIsMissingHandler(MissingServletRequestParameterException ex) {
    LOGGER.warn(ex.getMessage());
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String pathVariableIsMissingHandler(MethodArgumentTypeMismatchException ex) {
    LOGGER.warn(ErrorMessages.PARAMETER_MISSING_OR_WRONG_FORMAT + ex.getMessage());
    return ErrorMessages.PARAMETER_MISSING_OR_WRONG_FORMAT + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String runtimeExceptionHandler(RuntimeException ex) {
    LOGGER.warn(ex.getMessage());
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String invalidFormatExceptionHandler() {
    LOGGER.warn(ErrorMessages.PARAMETER_WRONG_FORMAT);
    return ErrorMessages.PARAMETER_WRONG_FORMAT;
  }
}
