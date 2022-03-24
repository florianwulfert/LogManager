package project.logManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import project.logManager.model.entity.Book;
import project.logManager.model.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIT {

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private MockMvc mockMvc;

        List<Book> books = new ArrayList<>();

        @BeforeAll
        public void setup() {
                books = creatBook();

        }

        private List<Book> creatBook() {

                List<Book> books = new ArrayList<>();
                Book haya = Book.builder()
                                .id(5)
                                .erscheinungsjahr(1789)
                                .titel("haya")
                                .build();
                bookRepository.save(haya);

                Book petra = Book.builder()
                                .id(7)
                                .erscheinungsjahr(1089)
                                .titel("petra")
                                .build();
                bookRepository.save(petra);

                Book peter = Book.builder()
                                .id(9)
                                .erscheinungsjahr(1589)
                                .titel("peter")
                                .build();
                bookRepository.save(peter);
                Book lina = Book.builder()
                                .id(12)
                                .erscheinungsjahr(2002)
                                .titel("lina")
                                .build();
                bookRepository.save(lina);
                Book omar = Book.builder()
                                .id(12)
                                .erscheinungsjahr(2002)
                                .titel("omar")
                                .build();
                bookRepository.save(omar);
                books.add(haya);
                books.add(petra);
                books.add(peter);
                books.add(lina);
                books.add(omar);
                return books;
        }

}
