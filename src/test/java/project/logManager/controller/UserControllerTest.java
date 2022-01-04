package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals("User Peter erstellt!",
        systemUnderTest.addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "GELB"));
        Mockito.verify(userService).addUser("Florian", "Peter", LocalDate.of(1988, 12, 12),
                87.5, 1.85, "GELB");
    }

    @Test
    void testFindUsers() {
        systemUnderTest.findUsers();
        Mockito.verify(userService).findUserList();
    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserByID(1);
        Mockito.verify(userService).findUserById(1);
    }

    @Test
    void testDeleteUserById() {
        List<User> users = addTestUser();
        systemUnderTest.deleteUserByID(users.get(0).getId(), users.get(1).getName());
        Mockito.verify(userService).deleteById(1, users.get(1).getName());
    }

    @Test
    void testDeleteUserByName() {
        systemUnderTest.deleteUserByName("Peter", "Hans");
        Mockito.verify(userService).deleteByName("Peter", "Hans");
    }

    @Test
    void testFindUserAndCalculateBMI() {
        List<User> testUser = addTestUser();
        systemUnderTest.findUserAndCalculateBMI(testUser.get(0).getName(), testUser.get(0).getGewicht(),
                testUser.get(0).getGroesse());
        Mockito.verify(userService).findUserAndCalculateBMI(testUser.get(0).getName()
                ,testUser.get(0).getGewicht(),
                testUser.get(0).getGroesse());
    }

    @Test
    void testBerechneBMI() {
        List<User> testUser = addTestUser();
        systemUnderTest.berechneBMI(testUser.get(0).getGewicht(), testUser.get(0).getGroesse());
        Mockito.verify(userService).berechneBMI(testUser.get(0).getGewicht(), testUser.get(0).getGroesse());
    }

    @Test
    void testBerechneBMIWithMessage() {
        systemUnderTest.berechneBMIwithMessage(LocalDate.of(2000, 12, 12),
                80.0, 1.75);
        Mockito.verify(userService).berechneBmiWithMessage(LocalDate.of(2000, 12, 12),
                80.0, 1.75);
    }

    private List<User> addTestUser() {
      List<User> users = new ArrayList<>();
      users.add(User.builder()
              .id(1)
          .name("Peter")
          .geburtsdatum(LocalDate.of(1988, 12, 12))
          .gewicht(90)
          .groesse(1.85)
          .lieblingsfarbe("gelb")
          .build());

      users.add(User.builder()
              .id(2)
          .name("Florian")
          .geburtsdatum(LocalDate.of(1988, 12, 12))
          .gewicht(90)
          .groesse(1.85)
          .lieblingsfarbe("gelb")
          .build());
      return users;
    }

}