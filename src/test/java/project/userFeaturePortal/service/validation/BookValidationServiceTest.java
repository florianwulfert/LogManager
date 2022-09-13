package project.userFeaturePortal.service.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookValidationServiceTest {

    @InjectMocks
    BookValidationService systemUnderTest;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    List<Book> books;

    @BeforeEach
    void init() {
        books = addTestBook();
    }

    @Test
    void testParameterIsNull() {
        assertThrows(ParameterNotPresentException.class,
                () -> systemUnderTest.validateParameters(null, "Test", true));
    }

    @Test
    void testBookAlreadyExists() {
        when(bookRepository.findByTitel("TestBook")).thenReturn(books);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> systemUnderTest.validateParameters(123,"TestBook", true));
        assertEquals("Book with the title TestBook already exists.", ex.getMessage());
    }

    @Test
    void testBookIsReferenced() {
        List<User> testUsers = addTestUser();
        when(userRepository.findByFavouriteBookId(1)).thenReturn(testUsers);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> systemUnderTest.checkIfBookIsReferenced(1));
        assertEquals("Book with ID 1 is assigned to at least one user.",ex.getMessage());
    }

    private List<Book> addTestBook() {
        List<Book> books = new ArrayList<>();
        books.add(
                Book.builder()
                        .id(1)
                        .erscheinungsjahr(1998)
                        .titel("TestBook")
                        .build());

        books.add(
                Book.builder()
                        .id(2)
                        .erscheinungsjahr(1900)
                        .titel("peter")
                        .build());
        return books;
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
                        .favouriteBook(null)
                        .build());
        return users;
    }
}
