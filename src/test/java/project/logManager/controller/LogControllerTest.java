package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
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
        Mockito.verify(logDTOMapper, Mockito.times(1)).logsToLogDTOs(Mockito.any());
    }

    @Test
    void testGetLogsThrowsException() {
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0 );
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0 );
        Mockito.when(logService.getLogs(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(), Mockito.any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.getLogs("INFO", "Test", startDate, endDate));
        Mockito.verify(logService).getLogs(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(), Mockito.any());
    }

    @Test
    void testAddLog() {
        systemUnderTest.addLog("INFO", "Test", "Peter");
        Mockito.verify(logService).addLog(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void testAddLogThrowsException() {

    }

    @Test
    void testGetLogsById() {
        systemUnderTest.getLogsByID(1);
        Mockito.verify(logService).searchLogsByID(1);
    }

    @Test
    void testGetLogsByIdThrowsException() {

    }

    @Test
    void testDeleteById() {
        systemUnderTest.deleteLogsByID(1);
        Mockito.verify(logService).deleteById(1);
    }

    @Test
    void testDeleteByIdThrowsException() {

    }

    @Test
    void testDeleteBySeverity() {
        systemUnderTest.deleteBySeverity("INFO");
        Mockito.verify(logService).deleteBySeverity("INFO");
    }

    @Test
    void testDeleteBySeverityThrowsException() {

    }

    @Test
    void testDeleteAll() {
        systemUnderTest.deleteAll();
        Mockito.verify(logService).deleteAll();
    }

    @Test
    void testDeleteAllThrowsException() {

    }
}