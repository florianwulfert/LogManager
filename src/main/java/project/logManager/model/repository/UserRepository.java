package project.logManager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findUserByName(String name);
}
