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

class UserControllerTest {

    @InjectMocks
    UserController systemUnderTest;

    @Mock
    UserService userService;

    @Test
    void testAddUser() {
        systemUnderTest.addUser("Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "blau");
        Mockito.verify(userService).addUser("Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "blau");

    }



}