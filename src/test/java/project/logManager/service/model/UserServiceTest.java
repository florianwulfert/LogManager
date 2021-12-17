package project.logManager.service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;


@ExtendWith(MockitoExtension.class)


class UserServiceTest {

    @InjectMocks
    UserService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Mock
    ValidationService logValidationService;

    @Mock
    LogService logService;

    @Captor
    ArgumentCaptor<User> arg;

    @Test
    void testIfColorIsNotCorrect() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
    }

    @Test
    void testIfUsersListIsEmpty() {
        Mockito.when((logValidationService.validateFarbenEnum(Mockito.anyString()))).thenReturn(true);
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(logService).addLog("Der User Peter wurde angelegt", "INFO", null);
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testFindByNameIsNotNull() {
        List<User> users = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users);
        Assertions.assertThrows(RuntimeException.class, () ->  systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
    }

    @Test
    void testIfActiveUserExists() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
        }


    @Test
    void testIfEverythingIsCorrectAtAddUser() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.anyString())).thenReturn(true);
        Mockito.when(userRepository.findUserByName(testUsers.get(0).getName())).thenReturn(new ArrayList<>());
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Mockito.when(userRepository.findUserByName(testUsers.get(1).getName())).thenReturn(testUsers);
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(userRepository).save(Mockito.any());
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

    @Test
    void testFindUserById() {
        systemUnderTest.findUserById(1);
        Mockito.verify(userRepository).findById(1);
    }

    @Test
    void testDeleteUserById() {
        systemUnderTest.deleteById(1, addTestUser().get(1));
        Mockito.verify(userRepository).deleteById(1);
    }
}