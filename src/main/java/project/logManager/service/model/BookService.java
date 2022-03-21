package project.logManager.service.model;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.logManager.common.message.InfoMessages;
import project.logManager.model.entity.Book;
import project.logManager.model.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {
    @Autowired
    private final BookRepository bookRepository;

    private static final Logger LOGGER = LogManager.getLogger(BookService.class); {
      
    }
    public Book saveBook(Book book){
        return bookRepository.save(book);
    }

    public Book addBook(Integer erscheinungsjahr,String titel ) {
        Book book=new Book();
        book.setErscheinungsjahr(erscheinungsjahr);
        book.setTitel(titel);
        saveBook(book);
        LOGGER.info(String.format(InfoMessages.BOOK_CREATED,titel));
        return saveBook(book);
    }

    public List<Book> getAllBooks(){
        LOGGER.info("all Books founds");
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByTitel(String titel){
        LOGGER.info(String.format(InfoMessages.Book_CREATED_TITELE, titel));
        return bookRepository.findByTitel(titel);
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }
}
