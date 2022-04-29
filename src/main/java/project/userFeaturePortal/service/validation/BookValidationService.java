package project.userFeaturePortal.service.validation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.service.model.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookValidationService {

    private final BookRepository bookRepository;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public Book checkIfBookExists(String bookTitel) {
        List<Book> books = bookRepository.findByTitel(bookTitel);
        if (books.isEmpty()) {
            LOGGER.warn(String.format(ErrorMessages.BOOK_NOT_FOUND_TITEL, bookTitel));
            throw new RuntimeException(String.format(ErrorMessages.BOOK_NOT_FOUND_TITEL, bookTitel));
        }
        return books.get(0);
    }
}
