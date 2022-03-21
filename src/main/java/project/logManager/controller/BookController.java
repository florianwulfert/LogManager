package project.logManager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public List<Book> getAllBooks(@RequestParam String actor){ 
    return bookService.getAllBooks(actor);
}

@PostMapping("/book")
public Book addBook(@RequestParam String titel, @RequestParam Integer erscheinungsjahr ,@RequestParam String actor){
    return bookService.addBook(erscheinungsjahr,titel,actor);
}

@GetMapping("/searchbook")
public List<Book> findBooksBytitel(@RequestParam String titel) {
    return bookService.searchBooksByTitel(titel);
}

@DeleteMapping("/deletebookById/{id}")
public String deleteBooksById(@PathVariable  Integer id ,@RequestParam String actor){
    return bookService.deleteById(id,actor);
}

@DeleteMapping("/deletebooksByTitel")
public String deleteBooksByTitel(@RequestParam String titel ,@RequestParam String actor){
    return bookService.deleteByTitel(titel,actor);
}

@DeleteMapping("allBooksdelete")
public String deleteAll(){
    return bookService.deleteBooks();
}
}
