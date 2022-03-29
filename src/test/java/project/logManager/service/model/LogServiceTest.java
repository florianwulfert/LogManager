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
  List<LogRequestDto> logRequestDtos;

  @BeforeEach
  void init() {
    customLogMessageDto = createCustomLogMessageDto();
    users = addTestUser();
    logRequestDtos = testLogRequestDto();
  }

  @Test
  void testGetLogs() {
    when(logDTOMapper.logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any())))
        .thenReturn(logs);
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    systemUnderTest.getLogs("WARNING", "Test", startDate, endDate);
    verify(logDTOMapper).logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any()));
  }

  @Test
  void testSeverityIsFalseAtGetLogs() {
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    assertEquals(new ArrayList<>(), systemUnderTest.getLogs("Hallo", "Test", startDate, endDate));
  }

  @Test
  void testAddLog() {
    when(logValidationService.validateMessage(anyString())).thenReturn(customLogMessageDto.get(1));
    assertEquals(
        "Message \"Banane\" saved as INFO!", systemUnderTest.addLog(logRequestDtos.get(0)));
    verify(logRepository, Mockito.times(1)).save(any());
  }

  @Test
  void testSearchLogsByID() {
    systemUnderTest.searchLogsByID(1);
    verify(logRepository).findById(1);
  }

  @Test
  void testDeleteById() {
    assertEquals(String.format(InfoMessages.ENTRY_DELETED_ID, 2), systemUnderTest.deleteById(2));
    verify(logRepository).deleteById(2);
  }

  @Test
  void testSearchLogByActorId() {
    systemUnderTest.existLogByUserToDelete(any());
    verify(logRepository).findByUser(any());
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

    when(logRepository.deleteBySeverity(Mockito.anyString())).thenReturn(logs);
    assertEquals(ENTRIES_DELETED, systemUnderTest.deleteBySeverity("INFO"));
    verify(logRepository).deleteBySeverity("INFO");
  }

  @Test
  void testNoEntriesFound() {
    assertEquals(ErrorMessages.NO_ENTRIES_FOUND, systemUnderTest.deleteBySeverity("INFO"));
    verify(logRepository).deleteBySeverity("INFO");
  }

  @Test
  void testDeleteAll() {
    systemUnderTest.deleteAll();
    verify(logRepository).deleteAll();
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

  private List<LogRequestDto> testLogRequestDto() {
    List<LogRequestDto> logRequestDtos = new ArrayList<>();
    logRequestDtos.add(
        LogRequestDto.builder().message("Hallo").severity("INFO").user("Hans").build());
    logRequestDtos.add(
        LogRequestDto.builder().message("Hallo").severity("Moin").user("Hans").build());
    return logRequestDtos;
  }
}
