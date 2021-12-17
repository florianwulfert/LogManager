package project.logManager.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.mapper.LogDTOMapper;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

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

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

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
    void testAddLog() {
        Mockito.when(userService.findUserByName(Mockito.any())).thenReturn(addTestUser().get(0));
        systemUnderTest.addLog("INFO", "Test", "Hans");
        Mockito.verify(logService, Mockito.times(1)).addLog(Mockito.any(), Mockito.any(), Mockito.any());
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

    @Test
    void testDeleteBySeverity() {
        systemUnderTest.deleteBySeverity("INFO");
        Mockito.verify(logService).deleteBySeverity("INFO");
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .name("Peter")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());
        users.add(User.builder()
                .name("Florian")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .build());
        return users;
    }
}