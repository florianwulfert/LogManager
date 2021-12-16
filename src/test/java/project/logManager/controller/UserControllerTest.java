package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.enums.UserFarbenEnum;
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
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, UserFarbenEnum.GELB);
        Mockito.verify(userService).addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, UserFarbenEnum.GELB);

    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserByID(1);
        Mockito.verify(userService).findUserById(1);
    }

    @Test
    void testDeleteUserById() {
        systemUnderTest.deleteUserByID(1);
        Mockito.verify(userService).deleteById(1);
    }


}