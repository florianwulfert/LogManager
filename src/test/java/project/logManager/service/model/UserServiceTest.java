package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.respository.LogRepository;
import project.logManager.model.respository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)


class UserServiceTest {

    @InjectMocks
    UserService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Mock
    LogRepository logRepository;

    @Captor
    ArgumentCaptor<User> arg;

    @Test
    void testAddUser() {
        systemUnderTest.addUser("Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "blau");
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testAddUserLog() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .name("Peter")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(90)
                .groesse(1.85)
                .lieblingsfarbe("blau")
                .build());

        Assertions.assertEquals("Der User Peter wurde angelegt", systemUnderTest.addUser("Peter",
                LocalDate.of(1988, 12, 12), 90, 1.85, "blau"));
    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserById(1);
        Mockito.verify(userRepository).findById(1);
    }

    @Test
    void testDeleteUserById() {
        systemUnderTest.deleteById(1);
        Mockito.verify(userRepository).deleteById(1);
    }
}