package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.LogMessageDto;
import project.logManager.common.dto.LogRequestDto;
import project.logManager.common.message.ErrorMessages;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.dto.LogDTO;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.LogValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static project.logManager.common.message.TestMessages.ENTRIES_DELETED;

/** @author - EugenFriesen 13.02.2021 */
@ExtendWith(MockitoExtension.class)
class LogServiceTest {

  @InjectMocks LogService systemUnderTest;

  @Mock LogRepository logRepository;

  @Mock LogValidationService logValidationService;

  @Mock UserRepository userRepository;

  @Mock LogDTOMapper logDTOMapper;

  List<LogMessageDto> customLogMessageDto;
  List<User> users;
  List<LogDTO> logs;

  @BeforeEach
  void init() {
    customLogMessageDto = createCustomLogMessageDto();
    users = addTestUser();
  }

  @Test
  void testGetLogs() {
    Mockito.when(logDTOMapper.logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any())))
        .thenReturn(logs);
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    systemUnderTest.getLogs("WARNING", "Test", startDate, endDate);
    Mockito.verify(logDTOMapper).logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any()));
  }

  @Test
  void testSeverityIsFalseAtGetLogs() {
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    assertEquals(new ArrayList<>(), systemUnderTest.getLogs("Hallo", "Test", startDate, endDate));
  }

  @Test
  void testAddLog() {
    LogRequestDto logRequestDto =
        LogRequestDto.builder().message("Banane").severity("INFO").user("Paul").build();
    Mockito.when(logValidationService.validateSeverity(any())).thenReturn(true);
    Mockito.when(logValidationService.validateMessage(Mockito.anyString()))
        .thenReturn(customLogMessageDto.get(1));
    Mockito.when(userRepository.findUserByName("Paul")).thenReturn(users.get(0));
    assertEquals("Message \"Banane\" saved as INFO!", systemUnderTest.addLog(logRequestDto));
    Mockito.verify(logRepository, Mockito.times(1)).save(any());
  }

  @Test
  void testAddLogWrongSeverity() {
    LogRequestDto logRequestDto =
        LogRequestDto.builder().message("Ein Test").severity("KATZE").user("Peter").build();
    Mockito.when(logValidationService.validateSeverity(any())).thenReturn(false);
    RuntimeException ex =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> systemUnderTest.addLog(logRequestDto));
    assertEquals(
        String.format(ErrorMessages.SEVERITY_NOT_REGISTERED_CHOICE, "KATZE"), ex.getMessage());
  }

  @Test
  void testKatzeReturnMessage() {
    LogRequestDto logRequestDto =
        LogRequestDto.builder().message("Katze").severity("INFO").user("Peter").build();
    Mockito.when(logValidationService.validateSeverity(anyString())).thenReturn(true);
    Mockito.when(userRepository.findUserByName("Peter")).thenReturn(users.get(0));
    Mockito.when(logValidationService.validateMessage(Mockito.anyString()))
        .thenReturn(customLogMessageDto.get(0));
    assertEquals(
        customLogMessageDto.get(0).getReturnMessage() + InfoMessages.HUND_SAVED,
        systemUnderTest.addLog(logRequestDto));
  }

  @Test
  void testIfUserIsNull() {
    LogRequestDto logRequestDto =
        LogRequestDto.builder().message("Hallo").severity("INFO").user("Hans").build();
    Mockito.when(logValidationService.validateSeverity(anyString())).thenReturn(true);
    Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(any());
    RuntimeException ex =
        Assertions.assertThrows(
            RuntimeException.class, () -> systemUnderTest.addLog(logRequestDto));
    assertEquals(String.format(ErrorMessages.USER_NOT_FOUND_NAME, "Hans"), ex.getMessage());
  }

  @Test
  void testSearchLogsByID() {
    systemUnderTest.searchLogsByID(1);
    Mockito.verify(logRepository).findById(1);
  }

  @Test
  void testDeleteById() {
    assertEquals(String.format(InfoMessages.ENTRY_DELETED_ID, 2), systemUnderTest.deleteById(2));
    Mockito.verify(logRepository).deleteById(2);
  }

  @Test
  void testSearchLogByActorId() {
    systemUnderTest.existLogByUserToDelete(any());
    Mockito.verify(logRepository).findByUser(any());
  }

  @Test
  void testDeleteBySeverity() {
    List<Log> logs = new ArrayList<>();
    logs.add(
        Log.builder()
            .timestamp(LocalDateTime.of(2020, Month.JANUARY, 25, 17, 0, 0))
            .id(1)
            .severity("INFO")
            .message("Pizza")
            .build());

    logs.add(
        Log.builder()
            .timestamp(LocalDateTime.of(2021, Month.JANUARY, 21, 17, 0, 0))
            .id(2)
            .severity("INFO")
            .message("Haus")
            .build());

    Mockito.when(logRepository.deleteBySeverity(Mockito.anyString())).thenReturn(logs);
    assertEquals(ENTRIES_DELETED, systemUnderTest.deleteBySeverity("INFO"));
    Mockito.verify(logRepository).deleteBySeverity("INFO");
  }

  @Test
  void testNoEntriesFound() {
    assertEquals(ErrorMessages.NO_ENTRIES_FOUND, systemUnderTest.deleteBySeverity("INFO"));
    Mockito.verify(logRepository).deleteBySeverity("INFO");
  }

  @Test
  void testDeleteAll() {
    systemUnderTest.deleteAll();
    Mockito.verify(logRepository).deleteAll();
  }

  private Log createNewLog(int id, String severity, String message, LocalDateTime timestamp) {
    Log log = new Log();
    log.setId(id);
    log.setSeverity(severity);
    log.setMessage(message);
    log.setTimestamp(timestamp);
    return log;
  }

  private List<User> addTestUser() {
    List<User> users = new ArrayList<>();
    users.add(
        User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(1999, 12, 13))
            .bmi(25.39)
            .weight(65)
            .height(1.60)
            .favouriteColor("Red")
            .build());
    return users;
  }

  private List<LogMessageDto> createCustomLogMessageDto() {
    List<LogMessageDto> customLogMessageDto = new ArrayList<>();
    customLogMessageDto.add(
        LogMessageDto.builder().message("Hund").returnMessage(InfoMessages.KATZE_TO_HUND).build());
    customLogMessageDto.add(LogMessageDto.builder().message("Banane").returnMessage("").build());
    return customLogMessageDto;
  }
}
