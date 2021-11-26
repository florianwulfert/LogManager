package project.logManager.model.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.User;

/**
 * @author - Florian Wulfert
 * 25.11.2021
 **/

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {




}
