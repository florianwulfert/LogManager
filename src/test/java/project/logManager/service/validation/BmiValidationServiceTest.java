package project.logManager.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.message.ErrorMessages;
import project.logManager.exception.ParameterNotPresentException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BmiValidationServiceTest {

  @InjectMocks BmiValidationService systemUnderTest;

  @Test
  void entriesAreNull() {
    ParameterNotPresentException ex =
        assertThrows(
            ParameterNotPresentException.class,
            () -> systemUnderTest.checkIfEntriesAreNull(LocalDate.of(2000, 12, 12), null, 1.87));

    assertEquals(ErrorMessages.PARAMETER_IS_MISSING, ex.getMessage());
  }
}
