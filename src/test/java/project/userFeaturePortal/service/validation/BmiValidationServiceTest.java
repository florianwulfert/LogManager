package project.userFeaturePortal.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.exception.ParameterNotPresentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BmiValidationServiceTest {

  @InjectMocks
  BmiValidationService systemUnderTest;

  @Test
  void entriesAreNull() {
    ParameterNotPresentException ex =
        assertThrows(
            ParameterNotPresentException.class,
            () -> systemUnderTest.checkIfEntriesAreNull(null, 1.87));

    assertEquals(ErrorMessages.PARAMETER_IS_MISSING, ex.getMessage());
  }
}
