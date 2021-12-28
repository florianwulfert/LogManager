package project.logManager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        systemUnderTest.deleteUserByID(testUser.get(0).getId(), testUser.get(1).getName());
        Mockito.verify(userService).findUserByName(testUser.get(1).getName());
        Mockito.verify(userService).deleteById(testUser.get(0).getId(), testUser.get(0));
    }

    @Test
    void testFindUserAndCalculateBMI() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserAndCalculateBMI(testUser.get(0).getName());
        Mockito.verify(userService).findUserAndCalculateBMI(testUser.get(0).getName());
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