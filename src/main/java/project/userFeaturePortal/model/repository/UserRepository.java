package project.userFeaturePortal.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.userFeaturePortal.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findUserByName(String name);
}
