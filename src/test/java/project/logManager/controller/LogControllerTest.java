package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.service.model.LogService;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * @author - EugenFriesen
 * 14.02.2021
 **/

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

    @InjectMocks
    LogController systemUnderTest;

    @Mock
    LogService logService;

    @Mock
    LogDTOMapper logDTOMapper;

    @Test
    void testGetLogs() {
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0 );
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0 );
        systemUnderTest.getLogs("INFO", "Test", startDate, endDate);
        Mockito.verify(logService, Mockito.times(1))
                .getLogs(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(logDTOMapper, Mockito.times(1)).mapLogsToLogDTOs(Mockito.any());
    }

    @Test
    void testAddLog() {
        systemUnderTest.addLog("INFO", "Test");
        Mockito.verify(logService, Mockito.times(1)).addLog(Mockito.any(), Mockito.any());
    }

    @Test
    void testGetLogsBySeverity() {
        systemUnderTest.getLogsBySeverity("INFO");
        Mockito.verify(logService, Mockito.times(1)).getLogsBySeverity(Mockito.any());
        Mockito.verify(logDTOMapper, Mockito.times(1)).mapLogsToLogDTOs(Mockito.any());
    }

    @Test
    void testFilterLogsByMessageParts() {
        systemUnderTest.filterLogsByMessageParts("Test");
        Mockito.verify(logService, Mockito.times(1)).searchLogsByMessageParts(Mockito.any());
        Mockito.verify(logDTOMapper, Mockito.times(1)).mapLogsToLogDTOs(Mockito.any());
    }

    @Test
    void testGetLogsByDateRange() {
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0 );
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0 );
        systemUnderTest.getLogsByDateRange(startDate, endDate);
        Mockito.verify(logDTOMapper, Mockito.times(1)).mapLogsToLogDTOs(Mockito.any());
    }

    @Test
    void testGetLogsById() {
        systemUnderTest.getLogsByID(1);
        Mockito.verify(logService).searchLogsByID(1);

    }

    @Test
    void testDeleteById() {
        systemUnderTest.deleteLogsByID(1);
        Mockito.verify(logService).deleteById(1);
    }
}