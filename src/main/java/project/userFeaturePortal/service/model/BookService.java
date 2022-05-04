package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.service.validation.BookValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

  private static final Logger LOGGER = LogManager.getLogger(BookService.class);
  @Autowired
  private final BookRepository bookRepository;
  private final LogService logService;
  private final BookValidationService bookValidationService;

  public List<Book> addBook(Integer erscheinungsjahr, String titel, String actor) {
    bookValidationService.validateActor(actor);
    bookValidationService.validateErscheinungsjahrAndTitel(erscheinungsjahr, titel);

    Book book = Book.builder().erscheinungsjahr(erscheinungsjahr).titel(titel).build();
    bookRepository.save(book);

    logService.addLog(LogRequestDto.builder()
        .message(String.format("Book %s was added.", titel)).severity("INFO").user(actor)
        .build());
    LOGGER.info(String.format(InfoMessages.BOOK_CREATED, titel));
    return bookRepository.findAll();
  }

  public List<Book> getAllBooks() {
    List<Book> books = bookRepository.findAll();
    List<String> bookNames = new ArrayList<>();
    for (Book book : books) {
      bookNames.add(book.getTitel());
    }
    LOGGER.info("Books found: " + bookNames);
    return books;
  }

  public List<Book> searchBooksByTitel(String titel) {
    return bookRepository.findByTitel(titel);
  }

  public String deleteById(int id, String actor) {
    bookValidationService.validateActor(actor);
    bookValidationService.checkIfBookIsReferenced(id);
    bookRepository.deleteById(id);
    logService.addLog(LogRequestDto.builder()
        .message(String.format(InfoMessages.BOOK_DELETED_ID, id)).severity("WARNING").user(actor)
        .build());
    return String.format(InfoMessages.BOOK_DELETED_ID, id);
  }

  public String deleteByTitel(String titel, String actor) {
    List<Book> booksToDelete = bookRepository.findByTitel(titel);

    if (booksToDelete.isEmpty()) {
      LOGGER.warn(String.format(InfoMessages.NO_BOOKS_FOUNDS, titel));
      return String.format(InfoMessages.NO_BOOKS_FOUNDS, titel);
    }

    if (booksToDelete.size() == 1) {
      bookRepository.deleteById(booksToDelete.get(0).getId());
      logService.addLog(LogRequestDto.builder()
          .message(String.format(InfoMessages.BOOK_DELETED_TITLE, titel)).severity("INFO").user(actor)
          .build());
      return String.format(InfoMessages.BOOK_DELETED_TITLE, titel);
    }

    LOGGER.warn(String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel));
    return String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel);
  }

  public String deleteBooks() {
    bookRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_BOOKS_DELETED);
    logService.addLog(LogRequestDto.builder()
        .message(InfoMessages.ALL_BOOKS_DELETED).severity("INFO").user(null)
        .build());
    return InfoMessages.ALL_BOOKS_DELETED;
  }
}
