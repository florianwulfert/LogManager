package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.books.BookDto;
import project.userFeaturePortal.common.dto.log.AddLogRequestDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.mapper.BookDtoMapper;
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
  private final BookDtoMapper bookDtoMapper;

  public List<Book> addBook(int erscheinungsjahr, String titel, String actor) {
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    bookValidationService.validateParameters(erscheinungsjahr, titel, true);

    bookRepository.save(buildBook(titel, erscheinungsjahr, new Book()));

    logService.addLog(LogRequestDto.builder()
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format("Book %s was added.", titel))
                    .severity("INFO")
                    .build())
            .user(actor)
            .build());
    LOGGER.info(String.format(InfoMessages.BOOK_CREATED, titel));
    return bookRepository.findAll();
  }

  private Book buildBook(String titel, int erscheinungsjahr, Book book) {
    book.setTitel(titel);
    book.setErscheinungsjahr(erscheinungsjahr);
    return book;
  }

  public String updateBook(String titel, int erscheinungsjahr, String actor) {
    bookValidationService.validateParameters(erscheinungsjahr, titel, false);
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    Book book = bookValidationService.checkIfBookExists(titel);

    bookRepository.save(buildBook(titel,erscheinungsjahr,book));

    LOGGER.info(String.format(InfoMessages.BOOK_UPDATED, titel));
    return String.format(InfoMessages.BOOK_UPDATED, titel);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public BookDto searchBooksByTitel(String titel) {
    List<Book> books = bookRepository.findByTitel(titel);
    return bookDtoMapper.bookToBookDto(books.get(0));
  }

  public String deleteById(int id, String actor) {
    userValidationService.checkIfNameExists(actor, true, ErrorMessages.USER_NOT_ALLOWED);
    bookValidationService.checkIfBookIsReferenced(id);

    bookRepository.deleteById(id);

    logService.addLog(LogRequestDto.builder()
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format(InfoMessages.BOOK_DELETED_ID, id))
                    .severity("WARNING")
                    .build())
            .user(actor)
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
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format(InfoMessages.BOOK_DELETED_TITLE, titel))
                    .severity("INFO")
                    .build())
            .user(actor)
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
            .addLogRequest(AddLogRequestDto.builder()
                    .message(InfoMessages.ALL_BOOKS_DELETED)
                    .severity("INFO")
                    .build())
            .user(actor)
            .build());
    return InfoMessages.ALL_BOOKS_DELETED;
  }
}
