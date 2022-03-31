package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.log.LogRequestDto;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/** @author - EugenFriesen 14.02.2021 */
@ExtendWith(MockitoExtension.class)
class LogControllerTest {

  @InjectMocks LogController systemUnderTest;

  @Mock LogService logService;

  @Mock LogDTOMapper logDTOMapper;

  @Test
  void testGetLogs() {
    LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
    systemUnderTest.getLogs("INFO", "Test", startDate, endDate);
    verify(logService).getLogs(any(), any(), any(), any());
  }

  @Test
  void testAddLog() {
    LogRequestDto testDto =
        LogRequestDto.builder().message("TestMessage").severity("INFO").user("Peter").build();
    systemUnderTest.addLog(testDto);
    verify(logService).addLog(any());
  }

  @Test
  void testGetLogsById() {
    systemUnderTest.getLogsByID(any());
    verify(logDTOMapper).logsToLogDTOs(any());
  }

  @Test
  void testDeleteById() {
    systemUnderTest.deleteLogsByID(1);
    verify(logService).deleteById(1);
  }

  @Test
  void testDeleteBySeverity() {
    systemUnderTest.deleteLogsBySeverity("INFO");
    verify(logService).deleteBySeverity("INFO");
  }

  @Test
  void testDeleteAll() {
    systemUnderTest.deleteAll();
    verify(logService).deleteAll();
  }
}
