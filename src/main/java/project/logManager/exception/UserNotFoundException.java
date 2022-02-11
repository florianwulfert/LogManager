package project.logManager.exception;

import project.logManager.common.message.ErrorMessages;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String userName) {
    super(String.format(ErrorMessages.USER_NOT_IDENTIFIED, userName));
  }
}
