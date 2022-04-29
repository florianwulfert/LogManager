package project.userFeaturePortal.service.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.common.dto.user.UserDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.mapper.UserDtoMapper;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BookValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  UserService systemUnderTest;

  @Mock
  UserRepository userRepository;

  @Mock
  BmiService bmiService;

  @Mock
  UserValidationService userValidationService;

  @Mock
  LogService logService;

  @Mock
  BookRepository bookRepository;

  @Mock
  BookService bookService;

  @Mock
  UserDtoMapper userDtoMapper;

  @Mock
  BookValidationService bookValidationService;

  List<User> users;

  @BeforeEach
  void init() {
    users = addTestUser();
  }

  @Test
  void testAddUser() {
    List<Book> testBook = testBook();
    when(userValidationService.validateUserToCreate(anyString(), anyString()))
        .thenReturn(true);
    systemUnderTest.addUser(
        UserRequestDto.builder()
            .actor("Torsten")
            .name("Hugo")
            .birthdate("1994-10-05")
            .weight(75.0)
            .height(1.65)
            .favouriteBook(testBook.get(0).getTitel())
            .build());
    verify(logService).addLog(any());
    verify(bookService).searchBooksByTitel("TestBook");
    verify(userRepository).save(any());
  }

  @Test
  void whenBooksListNotEmpty_ThenReturnFirstBookOfList() {
    List<Book> bookList = testBook();
    systemUnderTest.addUser(UserRequestDto.builder()
            .actor("Torsten")
            .name("Hugo")
            .birthdate("1994-10-05")
            .weight(75.0)
            .height(1.65)
            .favouriteBook(bookList.get(0).getTitel())
            .build());
    verify(bookService).searchBooksByTitel("TestBook");
  }

  @Test
  void testAddFavouriteBookToUser() {
    List<Book> books = testBook();
    when(bookValidationService.checkIfBookExists(anyString())).thenReturn(books.get(0));
    when(userValidationService.checkIfNameExists(anyString(), anyBoolean(), anyString())).thenReturn(users.get(0));
    systemUnderTest.addFavouriteBookToUser("TestBook", users.get(0).getName());
  }

  @Test
  void testUsersListIsEmpty() {
    List<Book> testBook = testBook();
    when(bmiService.calculateBmiAndGetBmiMessage(any(), any(), any()))
        .thenReturn("User has a BMI of 24.07 and therewith he has normal weight.");
    when(userValidationService.validateUserToCreate(anyString(),anyString())).thenReturn(false);
    when(userValidationService.validateActor(anyString(), anyString())).thenReturn(true);
    systemUnderTest.addUser(
        UserRequestDto.builder()
            .actor("Torsten")
            .name("Hugo")
            .birthdate("1994-10-05")
            .weight(75.0)
            .height(1.65)
            .favouriteBook(testBook.get(0).getTitel())
            .build());
    verify(logService).addLog(any());
    verify(userRepository).save(any());
  }

  @Test
  void testFindUserList() {
    List<UserDto> userDtoList = addListOfDtos();
    when(userRepository.findAll()).thenReturn(users);
    when(userDtoMapper.usersToUserDtos(anyList())).thenReturn(userDtoList);
    systemUnderTest.findUserList();
    verify(userRepository).findAll();
  }

  @Test
  void testFindUserById() {
    systemUnderTest.findUserById(1);
    verify(userRepository).findById(1);
  }

  @Test
  void testFindUserByName() {
    userRepository.findUserByName(anyString());
    verify(userRepository).findUserByName(anyString());
  }

  @Test
  void whenUserNotFoundButUserListIsEmpty_ThenReturnTrue() {
    assertTrue(systemUnderTest.findUserByName("Peter"));
  }

  @Test
  void whenUserNotFoundAndUserListIsNotEmpty_ThenReturnFalse() {
    List<UserDto> userDtoList = addListOfDtos();
    when(userDtoMapper.usersToUserDtos(anyList())).thenReturn(userDtoList);
    assertFalse(systemUnderTest.findUserByName("Heini"));
  }

  @Test
  void testDeleteById() {
    systemUnderTest.deleteById(1, "Florian");
    verify(userRepository).deleteById(1);
    verify(logService).addLog(any());
  }

  @Test
  void testDeleteByName() {
    when(userValidationService.validateUserToDelete(anyString(), anyString())).thenReturn(users.get(0));
    Assertions.assertEquals(
        String.format(InfoMessages.USER_DELETED_NAME, "Peter"),
        systemUnderTest.deleteByName("Peter", "Florian"));
    verify(logService).addLog(any());
  }

  @Test
  void testDeleteAll() {
    Assertions.assertEquals(InfoMessages.ALL_USERS_DELETED, systemUnderTest.deleteAll());
    verify(userRepository).deleteAll();
  }

  private List<User> addTestUser() {
    Book testBook = Book.builder().id(1).titel("TestBook").erscheinungsjahr(2020).build();
    List<User> users = new ArrayList<>();
    users.add(
        User.builder()
            .id(1)
            .name("Peter")
            .birthdate(LocalDate.of(2005, 12, 12))
            .weight(90.0)
            .height(1.85)
            .bmi(26.29)
            .favouriteBook(testBook)
            .build());

    users.add(
        User.builder()
            .id(2)
            .name("Florian")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(70.0)
            .height(1.85)
            .bmi(20.45)
            .favouriteBook(testBook)
            .build());
    return users;
  }

  private List<Book> testBook() {
    List<Book> booksList = new ArrayList<>();
    booksList.add(Book.builder().titel("TestBook").erscheinungsjahr(2020).build());
    return booksList;
  }

  private List<UserDto> addListOfDtos() {
    List<UserDto> dtoList = new ArrayList<>();
    dtoList.add(UserDto.builder().id(2)
            .name("Florian")
            .birthdate(LocalDate.of(1988, 12, 12))
            .weight(70.0)
            .height(1.85)
            .bmi(20.45)
            .favouriteBookTitel("testBook")
            .build());
    return dtoList;
  }
}
