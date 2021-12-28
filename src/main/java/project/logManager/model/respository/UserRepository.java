package project.logManager.model.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.logManager.model.entity.BMI;
import project.logManager.model.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByName(String name);

    List<User> findUserAndCalculateBMI(String name);

    List<BMI> calculateBMI(Double groesse, Double gewicht);


}
