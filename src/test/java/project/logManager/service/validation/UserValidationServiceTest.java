package project.logManager.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.LogService;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @InjectMocks
    UserValidationService systemUnderTest;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    LogService logService;

    List<User> users;

    @BeforeEach
    void init() {
        users = addTestUser();
    }


    @Test
    void testIfColorIsNotCorrect() {
        RuntimeException ex = Assertions.assertThrows(IllegalArgumentException.class, () ->
                systemUnderTest.validateFarbenEnum("gold"));
        Assertions.assertEquals("Farbe falsch! Wählen Sie eine der folgenden Farben: " +
                "blau, rot, orange, gelb, schwarz", ex.getMessage());
    }

    @Test
    void testCheckIfUsersListIsEmpty() {
        Assertions.assertTrue(systemUnderTest.checkIfUsersListIsEmpty("Peter", users.get(0)),
                "Test");
    }

    @Test
    void testIfUserNotEqualActor() {
        Mockito.when(logService.addLog(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(RuntimeException.class);
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.checkIfUsersListIsEmpty(users.get(1).getName(), users.get(0)));
        Assertions.assertEquals("User kann nicht angelegt werden, da noch keine User in der " +
                "Datenbank angelegt sind. Erster User muss sich selbst anlegen!" +
                " Peter ungleich Florian", ex.getMessage());
    }

    //Der Fall trifft aktuell nicht ein, wird aber aus Testcoverage-Gründen getestet
    @Test
    void testIfLogServiceDoesNotThrowException() {
        systemUnderTest.checkIfUsersListIsEmpty("Hänsel", users.get(0));
    }

    @Test
    void testIfActorExists() {
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
        Assertions.assertEquals(users.get(0),
                systemUnderTest.checkIfActorExists(users.get(0).getName()));
    }

    @Test
    void testIfActorIsNull() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.checkIfActorExists(users.get(1).getName()));
        Assertions.assertEquals("User Florian nicht gefunden", ex.getMessage());
    }

    @Test
    void testIfUserToPostExists() {
        Mockito.when(userRepository.findUserByName(Mockito.anyString())).thenReturn(users.get(0));
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.checkIfUserToPostExists(users.get(0).getName()));
        Assertions.assertEquals("User Peter bereits vorhanden", ex.getMessage());
    }

    @Test
    void testIfUserToPostIsNull() {
        systemUnderTest.checkIfUserToPostExists(users.get(0).getName());
    }

    private List<User> addTestUser() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .name("Peter")
                .geburtsdatum(LocalDate.of(2005, 12, 12))
                .gewicht(90.0)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .bmi(26.29)
                .build());

        users.add(User.builder()
                .id(2)
                .name("Florian")
                .geburtsdatum(LocalDate.of(1988, 12, 12))
                .gewicht(70.0)
                .groesse(1.85)
                .lieblingsfarbe("gelb")
                .bmi(20.45)
                .build());
        return users;
    }
}