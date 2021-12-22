package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.respository.LogRepository;
import project.logManager.service.validation.ValidationService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @InjectMocks
    LogService systemUnderTest;

    @Mock
    LogRepository logRepository;

    @Mock
    ValidationService logValidationService;

    @Captor
    ArgumentCaptor<Log> arg;

    @Test
    void testGetLogs() {
        LocalDateTime timestamp = LocalDateTime.of(2020, Month.JANUARY, 25, 17, 0, 0);
        List<Log> logs = new ArrayList<>();
        Log log = createNewLog(1, "INFO", "Das ist ein Test", timestamp);
        logs.add(log);
        Mockito.when(logRepository.findLogs(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(logs);
        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 25, 15, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, Month.JANUARY, 25, 18, 0, 0);
        systemUnderTest.getLogs("INFO", "Test", startDate, endDate);
        Mockito.verify(logRepository, Mockito.times(1))
                .findLogs(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void testAddLog() {
        Mockito.when(logValidationService.validateSeverity(Mockito.any())).thenReturn(true);
        systemUnderTest.addLog("My new log message", "INFO", User.builder().name("Peter").build());
        Mockito.verify(logRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testAddLogWrongSeverity() {
        Mockito.when(logValidationService.validateSeverity(Mockito.any())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.addLog("Ein Test", "KATZE", User.builder().name("Mia").build()));
    }

    @Test
    void testAddLogNullParameter() {
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addLog(null, "KATZE", User.builder().name("Mia").build()));
    }

    @Test
    void testKatzeMessage() {
        Mockito.when(logValidationService.validateSeverity(anyString())).thenReturn(true);
        systemUnderTest.addLog("Katze", "INFO", User.builder().name("Mia").build());
        Mockito.verify(logRepository).save(arg.capture());
        Assertions.assertEquals("Hund", arg.getValue().getMessage());
        Assertions.assertEquals("INFO", arg.getValue().getSeverity());
    }

    @Test
    void testSeverityMessage() {
        Mockito.when(logValidationService.validateSeverity(anyString())).thenReturn(true);
        Assertions.assertEquals("Es wurde die Nachricht \"Papagei\" als INFO abgespeichert!",
                systemUnderTest.addLog("Papagei", "INFO", User.builder().name("Paul").build()));
    }

    @Test
    void testKatzeReturnMessage() {
        Mockito.when(logValidationService.validateSeverity(anyString())).thenReturn(true);
        Assertions.assertEquals("Katze wurde in Hund übersetzt!\n" +
                        "Es wurde die Nachricht \"Hund\" als INFO abgespeichert!",
                systemUnderTest.addLog("Katze", "INFO", User.builder().name("Mia").build()));
    }

    private Log createNewLog(int id, String severity, String message, LocalDateTime timestamp) {
        Log log = new Log();
        log.setId(id);
        log.setSeverity(severity);
        log.setMessage(message);
        log.setTimestamp(timestamp);
        return log;
    }

    @Test
    void testSearchLogsByID() {
        systemUnderTest.searchLogsByID(1);
        Mockito.verify(logRepository)
                .findById(1);
    }

    @Test
    void testDeleteById() {
        systemUnderTest.deleteById(1);
        Mockito.verify(logRepository)
                .deleteById(1);
    }

    @Test
    void testSearchLogByActorId() {
        systemUnderTest.searchLogByActorId(Mockito.any());
        Mockito.verify(logRepository).findByUser(Mockito.any());
    }

    @Test
    void testDeleteBySeverity() {
        List<Log> logs = new ArrayList<>();
        logs.add(Log.builder()
                .timestamp(LocalDateTime.of(2020, Month.JANUARY, 25, 17, 0, 0))
                .id(1)
                .severity("INFO")
                .message("Pizza")
                .build());

        logs.add(Log.builder()
                .timestamp(LocalDateTime.of(2021, Month.JANUARY, 21, 17, 0, 0))
                .id(8)
                .severity("INFO")
                .message("Haus")
                .build());

        Mockito.when(logRepository.deleteBySeverity(Mockito.anyString())).thenReturn(logs);
        Assertions.assertEquals("Es wurden die Einträge mit den IDs 1, 8 aus der Datenbank gelöscht",
                systemUnderTest.deleteBySeverity("INFO"));
        Mockito.verify(logRepository).deleteBySeverity("INFO");
    }

    @Test
    void testNoEntriesFound() {
        Assertions.assertEquals("Keine Einträge gefunden!", systemUnderTest.deleteBySeverity("INFO"));
        Mockito.verify(logRepository).deleteBySeverity("INFO");
    }

}