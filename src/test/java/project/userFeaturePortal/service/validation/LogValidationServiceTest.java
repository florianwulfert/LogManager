package project.userFeaturePortal.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.dto.log.LogMessageDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.exception.SeverityNotFoundException;
import project.userFeaturePortal.model.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author - EugenFriesen 13.02.2021
 */
@ExtendWith(MockitoExtension.class)
class LogValidationServiceTest {

  @InjectMocks
  LogValidationService systemUnderTest;

  @Mock
  UserRepository userRepository;

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
    systemUnderTest.validateSeverity("INFO");
  }

  @Test
  void validateSeverityWrongSeverity() {
    SeverityNotFoundException ex =
        assertThrows(
            SeverityNotFoundException.class, () -> systemUnderTest.validateSeverity("Moin"));
    assertEquals(
        "Severity Moin not registered. Please choose one of the following options: TRACE, DEBUG, INFO, WARNING, ERROR, FATAL",
        ex.getMessage());
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
