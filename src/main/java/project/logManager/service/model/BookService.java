package project.logManager.service.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.logManager.model.entity.Book;
import project.logManager.model.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;


    public Book saveBook(Book book){

        return bookRepository.save(book);
    }


    public Book addBook(Integer erscheinungsjahr,String titel ) {


        Book book=new Book();
        book.setErscheinungsjahr(erscheinungsjahr);
        book.setTitel(titel);


        return saveBook(book);

        
    }

    public List<Book> getAllBooks(){

        return bookRepository.findAll();
    }


    public List<Book> searchBooksByTitel(String titel){

        return bookRepository.findByTitel(titel);



    }


}
