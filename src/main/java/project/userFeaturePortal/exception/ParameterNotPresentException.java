package project.userFeaturePortal.exception;

import project.userFeaturePortal.common.message.ErrorMessages;

public class ParameterNotPresentException extends RuntimeException {

  public ParameterNotPresentException() {
    super(ErrorMessages.PARAMETER_IS_MISSING);
  }
}
