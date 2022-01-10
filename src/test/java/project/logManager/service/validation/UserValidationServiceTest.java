package project.logManager.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.exception.ErsterUserUngleichActorException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @InjectMocks
    UserValidationService systemUnderTest;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    List<User> users;
    @BeforeEach
    void init() {
        users = addTestUser();
    }

    @Test
    void validateUserFarben() {
        assertTrue(systemUnderTest.validateFarbenEnum("blau"));
    }

    @Test
    void validateWrongUserFarben() {
        assertFalse(systemUnderTest.validateFarbenEnum("gold"));
    }

    @Test
    void testIfColorIsNotCorrect() {
        RuntimeException ex = Assertions.assertThrows(IllegalArgumentException.class, () ->
                systemUnderTest.handleFarbeNichtZugelassen("gold"));
        Assertions.assertEquals("Farbe falsch! Wählen Sie eine der folgenden Farben: " +
                "blau, rot, orange, gelb, schwarz", ex.getMessage());
    }

    @Test
    void testCheckIfUsersListIsEmpty() {
        systemUnderTest.checkIfUsersListIsEmpty("Florian", users.get(1));
        Mockito.verify(userService).saveUser(users.get(1), "Florian");
    }

    @Test
    void testIfUserNotEqualActor() {
        ErsterUserUngleichActorException ex = Assertions.assertThrows(ErsterUserUngleichActorException.class, () ->
                systemUnderTest.checkIfUsersListIsEmpty("Hans", users.get(0)));
        Assertions.assertEquals("User kann nicht angelegt werden, da noch keine User in der " +
                "Datenbank angelegt sind. Erster User muss sich selbst anlegen!" +
                "Hans ungleich Peter", ex.getMessage());
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