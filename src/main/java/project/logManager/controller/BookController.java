package project.logManager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.logManager.model.entity.Book;
import project.logManager.service.model.BookService;

@RestController
@RequiredArgsConstructor
public class BookController {

@Autowired
private final BookService bookService;

@GetMapping("/books")
public List<Book> getAllBooks(){ 
    return bookService.getAllBooks();
}

@PostMapping("/book")
public Book addBook(@RequestParam String titel, @RequestParam Integer erscheinungsjahr){
    return bookService.addBook(erscheinungsjahr,titel);
}

@GetMapping("/searchBook")
public List<Book> findBooksBytitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
}
}




