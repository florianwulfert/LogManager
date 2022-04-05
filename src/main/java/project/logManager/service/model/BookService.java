package project.logManager.service.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.common.dto.log.LogRequestDto;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.entity.Book;
import project.logManager.model.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {

  private static final Logger LOGGER = LogManager.getLogger(BookService.class);
  @Autowired
  private final BookRepository bookRepository;
  private final LogService logService;

  public Book saveBook(Book book) {
    return bookRepository.save(book);
  }

  public Book addBook(Integer erscheinungsjahr, String titel, String actor) {
    Book book = new Book();
    book.setErscheinungsjahr(erscheinungsjahr);
    book.setTitel(titel);
    saveBook(book);
    logService.addLog(LogRequestDto.builder()
        .message("New book was added.")
        .severity("INFO")
        .user(actor)
        .build());
    LOGGER.info(String.format(InfoMessages.BOOK_CREATED, titel));
    return book;
  }

  public List<Book> getAllBooks(String actor) {
    logService.addLog(LogRequestDto.builder()
        .message("All books founds.")
        .severity("INFO")
        .user(actor)
        .build());
    LOGGER.info("All books founds");
    return bookRepository.findAll();
  }

  public String searchBooksByTitel(String titel) {
    LOGGER.info(String.format(InfoMessages.Book_FOUND_TITLE, titel));
    bookRepository.findByTitel(titel);
    return String.format(InfoMessages.Book_FOUND_TITLE, titel);
  }

  public String deleteById(Integer id, String actor) {
    Book book = bookRepository.getOne(id);
    bookRepository.delete(book);
    saveLog(String.format(InfoMessages.BOOK_DELETED_ID, id), "WARNING", actor);
    LOGGER.info(String.format(InfoMessages.BOOK_DELETED_ID, id));
    return String.format(InfoMessages.BOOK_DELETED_ID, id);
  }

  public String deleteByTitel(String titel, String actor) {
    List<Book> deleteBooks = bookRepository.findByTitel(titel);
    if (deleteBooks.isEmpty()) {
      LOGGER.info(InfoMessages.NO_BOOKS_FOUNDS, titel);
      return String.format(InfoMessages.NO_BOOKS_FOUNDS, titel);
    } else if (deleteBooks.size() == 1) {
      bookRepository.deleteById(deleteBooks.get(0).getId());
      saveLog(String.format(InfoMessages.BOOK_DELETED_TITLE, titel), "INFO", actor);
      return String.format(InfoMessages.BOOK_DELETED_TITLE, titel);
    } else {
      String listString = "";
      for (Book b : deleteBooks) {
        if (!listString.equals("")) {
          listString = listString + ", ";
        }
        listString = listString + "{Titel:" + b.getTitel() + ", Erscheinungsjahr:" + b.getErscheinungsjahr()
            + ",ID:" + b.getId() + "}";
      }
      LOGGER.info(String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel, listString));
      return String.format(InfoMessages.BOOK_CAN_NOT_BE_IDENTIFIED, titel, listString);
    }

  }

  public String deleteBooks() {
    bookRepository.deleteAll();
    LOGGER.info(InfoMessages.ALL_BOOKS_DELETED);
    return InfoMessages.ALL_BOOKS_DELETED;
  }

  public void saveLog(String message, String severity, String actor) {
    LogRequestDto logRequestDto = LogRequestDto.builder()
        .message(message)
        .severity(severity)
        .user(actor)
        .build();
    LOGGER.info(message);
    logService.addLog(logRequestDto);
  }
}
