package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.service.model.BmiService;
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

    @Mock
    BmiService bmiService;

    List<User> users;

    @BeforeEach
    void init() {
        users = addTestUser();
    }

    @Test
    void testAddUser() {
        Mockito.when(userService.addUser(users.get(1).getName(), users.get(0).getName(), users.get(0).getGeburtsdatum(),
                90.0, 1.85, users.get(0).getLieblingsfarbe())).thenReturn("Test");
        Assertions.assertEquals("User Peter wurde erstellt. Test",
        systemUnderTest.addUser(users.get(1).getName(), users.get(0).getName(), users.get(0).getGeburtsdatum(),
                90.0, 1.85, users.get(0).getLieblingsfarbe()));
        Mockito.verify(userService).addUser(users.get(1).getName(), users.get(0).getName(), users.get(0).getGeburtsdatum(),
                users.get(0).getGewicht(), users.get(0).getGroesse(), users.get(0).getLieblingsfarbe());
    }

    @Test
    void testAddUserThrowsException() {
        Mockito.when(userService.addUser(users.get(1).getName(), users.get(0).getName(),
                users.get(0).getGeburtsdatum(),
                90.0, 1.85, users.get(0).getLieblingsfarbe()))
                .thenThrow(RuntimeException.class);
        Assertions.assertNull(systemUnderTest.addUser(users.get(1).getName(), users.get(0).getName(),
                        users.get(0).getGeburtsdatum(),
                        90.0, 1.85, users.get(0).getLieblingsfarbe()));
        Mockito.verify(userService).addUser(users.get(1).getName(), users.get(0).getName(),
                users.get(0).getGeburtsdatum(),
                90.0, 1.85, users.get(0).getLieblingsfarbe());
    }

    @Test
    void testFindUsers() {
        systemUnderTest.findUsers();
        Mockito.verify(userService).findUserList();
    }

    @Test
    void testFindUsersThrowsException() {
        Mockito.when(userService.findUserList()).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.findUsers());
        Mockito.verify(userService).findUserList();
    }

    @Test
    void testFindUserById() {
        systemUnderTest.findUserByID(1);
        Mockito.verify(userService).findUserById(1);
    }

    @Test
    void testFindUserByIdThrowsException() {
        Mockito.when(userService.findUserById(Mockito.any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () ->
                systemUnderTest.findUserByID(1));
        Mockito.verify(userService).findUserById(1);
    }

    @Test
    void testDeleteUserById() {
        systemUnderTest.deleteUserByID(users.get(0).getId(), users.get(1).getName());
        Mockito.verify(userService).deleteById(1, users.get(1).getName());
    }

    @Test
    void testDeleteByIdThrowsException() {
        Mockito.when(userService.deleteById(1, "Hans"))
                .thenThrow(RuntimeException.class);
        Assertions.assertNull(systemUnderTest.deleteUserByID(1, "Hans"));
        Mockito.verify(userService).deleteById(1, "Hans");
    }

    @Test
    void testDeleteUserByName() {
        systemUnderTest.deleteUserByName("Peter", "Hans");
        Mockito.verify(userService).deleteByName("Peter", "Hans");
    }

    @Test
    void testDeleteByNameThrowsException() {
        Mockito.when(userService.deleteByName( "Hans", "Peter"))
                .thenThrow(RuntimeException.class);
        Assertions.assertNull(systemUnderTest.deleteUserByName("Hans","Peter"));
        Mockito.verify(userService).deleteByName("Hans", "Peter");
    }

    @Test
    void testDeleteAll() {
        systemUnderTest.deleteAll();
        Mockito.verify(userService).deleteAll();
    }

    @Test
    void testDeleteAllThrowsException() {
        Mockito.when(userService.deleteAll())
                .thenThrow(RuntimeException.class);
        Assertions.assertNull(systemUnderTest.deleteAll());
        Mockito.verify(userService).deleteAll();
    }

    private List<User> addTestUser() {
      List<User> users = new ArrayList<>();
      users.add(User.builder()
              .id(1)
          .name("Peter")
          .geburtsdatum(LocalDate.of(1988, 12, 12))
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