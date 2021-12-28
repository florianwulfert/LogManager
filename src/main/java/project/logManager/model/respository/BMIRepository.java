package project.logManager.model.respository;


import org.springframework.stereotype.Repository;
import project.logManager.model.entity.BMI;

import java.util.List;

@Repository
public interface BMIRepository {
    List<BMI> calculateBMI(Double groesse, Double gewicht);
}
