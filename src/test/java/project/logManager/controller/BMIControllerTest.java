package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.service.model.UserService;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)

class BMIControllerTest {

    @InjectMocks
    BMIController systemUnderTest;

    @Mock
    UserService userService;

    @Test
    void testBerechneBMI() {
        systemUnderTest.berechneBMI(LocalDate.of(2000, 12, 12), 85.3, 1.70);
        Mockito.verify(userService).berechneBMI(1.70, 85.3);
    }

}