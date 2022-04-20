package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.exception.ParameterNotPresentException;
import project.userFeaturePortal.exception.UserNotAllowedException;
import project.userFeaturePortal.exception.UserNotFoundException;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

  private static final Logger LOGGER = LogManager.getLogger(BookService.class);
  @Autowired private final BookRepository bookRepository;
  private final LogService logService;
  private final UserRepository userRepository;

  public void saveBook(Book book) {
    bookRepository.save(book);
  }

  public List<Book> addBook(Integer erscheinungsjahr, String titel, String actor) {
    validateActor(actor);
    validateErscheinungsjahrAndTitel(erscheinungsjahr,titel);
    Book book = new Book();
    book.setErscheinungsjahr(erscheinungsjahr);
    book.setTitel(titel);
    saveBook(book);
    logService.addLog(
        LogRequestDto.builder()
            .message("New book was added.")
            .severity("INFO")
            .user(actor)
            .build());
    LOGGER.info(String.format(InfoMessages.BOOK_CREATED, titel));
    return bookRepository.findAll();
  }

  private void validateActor(String actor) {
    if (actor == null) {
      throw new UserNotFoundException("null");
    }
    User user = userRepository.findUserByName(actor);
    if (user == null) {
      throw new UserNotAllowedException(ErrorMessages.USER_NOT_ALLOWED);
    }
  }

  private void validateErscheinungsjahrAndTitel(Integer erscheinungsjahr, String titel) {
    if (erscheinungsjahr == null || titel == null) {
      throw new ParameterNotPresentException();
    }
  }

  public List<Book> getAllBooks() {
    LOGGER.debug("All books founds");
    return bookRepository.findAll();
  }

  public List<Book> searchBooksByTitel(String titel) {
    LOGGER.info(String.format(InfoMessages.Book_FOUND_TITLE, titel));
    return bookRepository.findByTitel(titel);
  }

  public String deleteById(int id, String actor) {
    validateActor(actor);
    checkIfBookIsReferenced(id);
    Book book = bookRepository.getOne(id);
    bookRepository.delete(book);
    saveLog(String.format(InfoMessages.BOOK_DELETED_ID, id), "WARNING", actor);
    LOGGER.info(String.format(InfoMessages.BOOK_DELETED_ID, id));
    return String.format(InfoMessages.BOOK_DELETED_ID, id);
  }

  private void checkIfBookIsReferenced(int id) {
    List<User> users = userRepository.findByFavouriteBookId(id);
    if (!users.isEmpty()) {
      throw new RuntimeException(String.format(String.format(InfoMessages.USER_WITH_BOOK, id)));
    }
  }

  public String deleteByTitel(String titel, String actor) {
    List<Book> deleteBooks = bookRepository.findByTitel(titel);
    if (deleteBooks.isEmpty()) {
      LOGGER.info(String.format(InfoMessages.NO_BOOKS_FOUNDS, titel));
      return String.format(InfoMessages.NO_BOOKS_FOUNDS, titel);
    } else if (deleteBooks.size() == 1) {
      bookRepository.deleteById(deleteBooks.get(0).getId());
      saveLog(String.format(InfoMessages.BOOK_DELETED_TITLE, titel), "INFO", actor);
      return String.format(InfoMessages.BOOK_DELETED_TITLE, titel);
    } else {
      String listString = "";
      for (Book b : deleteBooks) {
        if (!listString.equals("")) {
          listString = listString.concat(", ");
        }
        listString = listString.concat("{Titel:").concat(b.getTitel()).concat(", Erscheinungsjahr:")
            .concat(b.getErscheinungsjahr().toString())
            .concat(", Id:").concat(b.getId().toString());
      }
      LOGGER.info(String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel));
      return String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel);
    }
  }

  public String deleteBooks() {
    bookRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_BOOKS_DELETED);
    return InfoMessages.ALL_BOOKS_DELETED;
  }

  public void saveLog(String message, String severity, String actor) {
    LogRequestDto logRequestDto =
        LogRequestDto.builder().message(message).severity(severity).user(actor).build();
    LOGGER.info(message);
    logService.addLog(logRequestDto);
  }
}
