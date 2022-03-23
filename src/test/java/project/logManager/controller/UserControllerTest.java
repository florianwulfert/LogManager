package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.UserRequestDto;
import project.logManager.model.entity.User;
import project.logManager.service.model.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks UserController systemUnderTest;

  @Mock UserService userService;

  List<User> users;

  @BeforeEach
  void init() {
    users = addTestUser();
  }

  @Test
  void testAddUser() {
    Mockito.when(
            systemUnderTest.addUser(
                UserRequestDto.builder()
                    .actor("Torsten")
                    .name("Hugo")
                    .birthdate("05.10.1994")
                    .weight(75.0)
                    .height(1.65)
                    .favouriteColor("red")
                    .build()))
        .thenReturn("Test");
    Assertions.assertEquals(
        "User Hugo was created. Test",
        systemUnderTest.addUser(
            UserRequestDto.builder()
                .actor("Torsten")
                .name("Hugo")
                .birthdate("05.10.1994")
                .weight(75.0)
                .height(1.65)
                .favouriteColor("red")
                .build()));
    verify(userService)
        .addUser(
            UserRequestDto.builder()
                .actor("Torsten")
                .name("Hugo")
                .birthdate("05.10.1994")
                .weight(75.0)
                .height(1.65)
                .favouriteColor("red")
                .build());
  }

  @Test
  void testFindUsers() {
    systemUnderTest.findUsers();
    verify(userService).findUserList();
  }

  @Test
  void testFindUserById() {
    systemUnderTest.findUserByID(1);
    verify(userService).findUserById(1);
  }

  @Test
  void testDeleteUserById() {
    systemUnderTest.deleteUserByID(users.get(0).getId(), users.get(1).getName());
    verify(userService).deleteById(1, users.get(1).getName());
  }

  @Test
  void testDeleteUserByName() {
    systemUnderTest.deleteUserByName("Peter", "Hans");
    verify(userService).deleteByName("Peter", "Hans");
  }

  @Test
  void testDeleteAll() {
    systemUnderTest.deleteAll();
    verify(userService).deleteAll();
  }

  private List<User> addTestUser() {
    List<User> users = new ArrayList<>();
    users.add(
        User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(90.0)
            .height(1.85)
            .favouriteColor("yellow")
            .bmi(26.29)
            .build());

    users.add(
        User.builder()
            .id(2)
            .name("Florian")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(70.0)
            .height(1.85)
            .favouriteColor("yellow")
            .bmi(20.45)
            .build());
    return users;
  }
}
