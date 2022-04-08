package project.userFeaturePortal.exception;

import project.userFeaturePortal.common.message.ErrorMessages;

public class FirstUserUnequalActorException extends RuntimeException {

  public FirstUserUnequalActorException(String actor, String name) {
    super(ErrorMessages.NO_USERS_YET + name + " unequal " + actor);
  }
}
