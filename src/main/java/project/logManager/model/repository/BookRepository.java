package project.logManager.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.logManager.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository <Book,Integer> {
   
    List<Book> findByTitel(String titel);
    
    List<Book> deleteByTitel(String titel);
}