package project.userFeaturePortal.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.dto.log.AddLogRequestDto;
import project.userFeaturePortal.common.dto.log.LogDTO;
import project.userFeaturePortal.common.dto.log.LogMessageDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Log;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.mapper.LogDTOMapper;
import project.userFeaturePortal.model.repository.LogRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.LogValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static project.userFeaturePortal.TestMessages.ENTRIES_DELETED;

/**
 * @author - EugenFriesen 13.02.2021
 */
@ExtendWith(MockitoExtension.class)
class LogServiceTest {

  @InjectMocks
  LogService systemUnderTest;

  @Mock
  LogRepository logRepository;

  @Mock
  LogValidationService logValidationService;

  @Mock
  UserRepository userRepository;

  @Mock
  LogDTOMapper logDTOMapper;

  @Mock
  UserValidationService userValidationService;

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
    when(logDTOMapper.logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any(), any())))
        .thenReturn(logs);
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    systemUnderTest.getLogs("WARNING", "Test", startDate, endDate, null);
    verify(logDTOMapper).logsToLogDTOs(logRepository.findLogs(any(), any(), any(), any(), any()));
  }

  @Test
  void testSeverityIsFalseAtGetLogs() {
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    assertEquals(new ArrayList<>(), systemUnderTest.getLogs("Hallo", "Test", startDate, endDate, null));
  }

  @Test
  void testAddLog() {
    when(logValidationService.validateMessage(anyString())).thenReturn(customLogMessageDto.get(1));
    when(userValidationService.checkIfNameExists(anyString(),anyBoolean(),anyString())).thenReturn(users.get(0));
    assertEquals("Message \"Banane\" saved as WARNING!",
            systemUnderTest.addLog(logRequestDtos.get(0)));
    verify(logRepository, times(1)).save(any());
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
        LogRequestDto.builder()
                .addLogRequest(AddLogRequestDto.builder()
                        .message("Test")
                        .severity("WARNING")
                        .build())
                .user("Hans")
                .build());
    logRequestDtos.add(
        LogRequestDto.builder()
                .addLogRequest(AddLogRequestDto.builder()
                        .message("Test")
                        .severity("Hi")
                        .build())
                .user("Hans").build());
    return logRequestDtos;
  }
}
