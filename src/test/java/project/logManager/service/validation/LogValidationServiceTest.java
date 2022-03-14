package project.logManager.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.LogMessageDto;
import project.logManager.common.dto.LogRequestDto;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.exception.ParameterNotPresentException;

import static org.junit.jupiter.api.Assertions.*;

/** @author - EugenFriesen 13.02.2021 */
@ExtendWith(MockitoExtension.class)
class LogValidationServiceTest {

  @InjectMocks LogValidationService systemUnderTest;

  @Test
  void testEntryIsNull() {
    LogRequestDto testDto =
        LogRequestDto.builder().message(null).severity(null).user("Hans").build();
    ParameterNotPresentException ex =
        assertThrows(
            ParameterNotPresentException.class,
            () -> systemUnderTest.checkIfAnyEntriesAreNull(testDto));
    assertEquals(ErrorMessages.PARAMETER_IS_MISSING, ex.getMessage());
  }

  @Test
  void testEntryIsEmptyString() {
    LogRequestDto testDto =
        LogRequestDto.builder().message("Test").severity("WARNING").user("").build();
    ParameterNotPresentException ex =
        assertThrows(
            ParameterNotPresentException.class,
            () -> systemUnderTest.checkIfAnyEntriesAreNull(testDto));
    assertEquals(ErrorMessages.PARAMETER_IS_MISSING, ex.getMessage());
  }

  @Test
  void validateSeverity() {
    assertTrue(systemUnderTest.validateSeverity("INFO"));
  }

  @Test
  void validateSeverityWrongSeverity() {
    assertFalse(systemUnderTest.validateSeverity("KATZE"));
  }

  @Test
  void validateMessageIsKatze() {
    LogMessageDto customLogMessageDto =
        LogMessageDto.builder()
            .message("Hund")
            .returnMessage(InfoMessages.KATZE_TO_HUND + "\n")
            .build();
    Assertions.assertEquals(customLogMessageDto, systemUnderTest.validateMessage("Katze"));
  }

  @Test
  void validateMessageIsNotKatze() {
    LogMessageDto customLogMessageDto =
        LogMessageDto.builder().message("Apfel").returnMessage("").build();
    Assertions.assertEquals(customLogMessageDto, systemUnderTest.validateMessage("Apfel"));
  }
}
