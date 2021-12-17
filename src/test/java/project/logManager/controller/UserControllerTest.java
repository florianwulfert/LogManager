package project.logManager.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

@ExtendWith(MockitoExtension.class)

class UserControllerTest {

    @InjectMocks
    UserController systemUnderTest;

    @Mock
    UserService userService;

    @Test
    void testAddUser() {
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "GELB");
        Mockito.verify(userService).addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "GELB");

    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserByID(1);
        Mockito.verify(userService).findUserById(1);
    }

    @Test
    void testDeleteUserById() {
        List<User> testUser = addTestUser();
        systemUnderTest.deleteUserByID(1, testUser.get(1));
        Mockito.verify(userService).deleteById(1, testUser.get(1));
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