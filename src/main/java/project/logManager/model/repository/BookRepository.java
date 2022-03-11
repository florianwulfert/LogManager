package project.logManager.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.Book;
import java.util.List;



@Repository
public interface BookRepository extends JpaRepository <Book,Integer> {

    List<Book> findBooks();


    List<Book> findByTitel(String titel);

    
}