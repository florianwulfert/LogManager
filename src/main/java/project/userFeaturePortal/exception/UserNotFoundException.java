package project.userFeaturePortal.exception;

import project.userFeaturePortal.common.message.ErrorMessages;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String userName) {
    super(String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
  }
}
