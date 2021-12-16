package project.logManager.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.enums.UserFarbenEnum;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.LogValidationService;

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
    LogValidationService logValidationService;

    @Mock
    LogService logService;

    @Captor
    ArgumentCaptor<User> arg;

    @Test
    void testIfColorIsCorrect() {
        Mockito.when(logValidationService.validateFarbenEnum(Mockito.any())).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
    }

    @Test
    void testIfUsersListIsEmpty() {
        Mockito.when((logValidationService.validateFarbenEnum("GELB"))).thenReturn(true);
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB");
        Mockito.verify(logService).addLog("Der User Peter wurde angelegt", "INFO", null);
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testFindByNameIsNotNull() {
        List<User> users = addTestUser();
        Mockito.when((logValidationService.validateFarbenEnum(UserFarbenEnum.GELB.getFarbe()))).thenReturn(true);
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users);
        Assertions.assertThrows(RuntimeException.class, () ->  systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
    }

    @Test
    void testIfActiveUserExists() {
        List<User> testUsers = addTestUser();
        Mockito.when(logValidationService.validateFarbenEnum(UserFarbenEnum.ROT.getFarbe())).thenReturn(true);
        Mockito.when(userRepository.findAll()).thenReturn(testUsers);
        Assertions.assertThrows(RuntimeException.class, () -> systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "GELB"));
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
        systemUnderTest.deleteById(1);
        Mockito.verify(userRepository).deleteById(1);
    }
}