package project.logManager.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.logManager.service.model.UserService;

@RestControllerAdvice
public class CustomRestExceptionHandler {
  private static final Logger LOGGER = LogManager.getLogger(UserService.class);

  @ResponseBody
  @ExceptionHandler(DateFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String dateFormatExceptionHandler(DateFormatException ex) {
    LOGGER.info(ex.getMessage());
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(ParameterNotPresentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String parameterNotPresentExceptionHandler(ParameterNotPresentException ex) {
    LOGGER.info(ex.getMessage());
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(IllegalColorException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String illegalColorExceptionHandler(IllegalColorException ex) {
    LOGGER.info(ex.getMessage());
    return ex.getMessage();
  }
}
