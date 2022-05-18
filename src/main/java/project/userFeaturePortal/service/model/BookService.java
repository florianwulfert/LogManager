package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.books.BookRequestDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.service.validation.BookValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

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
  private final UserValidationService userValidationService;

  public List<Book> addBook(Integer erscheinungsjahr, String titel, String actor) {
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    bookValidationService.validateParameters(erscheinungsjahr, titel, actor);

    Book book = buildBook(titel, erscheinungsjahr);
    bookRepository.save(book);

    logService.addLog(LogRequestDto.builder()
        .message(String.format("Book %s was added.", titel)).severity("INFO").user(actor)
        .build());
    LOGGER.info(String.format(InfoMessages.BOOK_CREATED, titel));
    return bookRepository.findAll();
  }

  private Book buildBook(String titel, int erscheinungsjahr) {
    Book book = new Book();
    book.setTitel(titel);
    book.setErscheinungsjahr(erscheinungsjahr);
    return book;
  }

  public String updateUser(BookRequestDto allParameters) {
    bookValidationService.validateParameters(allParameters.erscheinungsjahr,
            allParameters.titel, allParameters.actor);
    userValidationService.checkIfNameExists(allParameters.actor, true, ErrorMessages.USER_NOT_ALLOWED);
    buildBook(allParameters.titel, allParameters.erscheinungsjahr);
    LOGGER.info(String.format(InfoMessages.BOOK_UPDATED, allParameters.titel));
    return String.format(InfoMessages.BOOK_UPDATED, allParameters.titel);
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
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    bookValidationService.checkIfBookIsReferenced(id);

    bookRepository.deleteById(id);

    logService.addLog(LogRequestDto.builder()
        .message(String.format(InfoMessages.BOOK_DELETED_ID, id)).severity("WARNING").user(actor)
        .build());
    return String.format(InfoMessages.BOOK_DELETED_ID, id);
  }

  public String  deleteByTitel(String titel, String actor) {
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    List<Book> booksToDelete = bookRepository.findByTitel(titel);

    if (booksToDelete.isEmpty()) {
      LOGGER.warn(String.format(InfoMessages.NO_BOOKS_FOUND, titel));
      return String.format(InfoMessages.NO_BOOKS_FOUND, titel);
    }

    bookValidationService.checkIfBookIsReferenced(booksToDelete.get(0).getId());

    bookRepository.deleteById(booksToDelete.get(0).getId());

    logService.addLog(LogRequestDto.builder()
        .message(String.format(InfoMessages.BOOK_DELETED_TITLE, titel)).severity("INFO").user(actor)
        .build());
    return String.format(InfoMessages.BOOK_DELETED_TITLE, titel);
  }

  public String deleteBooks(String actor) {
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    for (Book book: bookRepository.findAll()) {
      bookValidationService.checkIfBookIsReferenced(book.getId());
    }
    bookRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_BOOKS_DELETED);
    logService.addLog(LogRequestDto.builder()
        .message(InfoMessages.ALL_BOOKS_DELETED).severity("INFO").user(actor)
        .build());
    return InfoMessages.ALL_BOOKS_DELETED;
  }
}
