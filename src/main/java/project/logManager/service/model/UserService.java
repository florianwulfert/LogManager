package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Transactional
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {
    private final UserRepository userRepository;
    public void addUser(String name, LocalDate geburtsdatum, double gewicht,
                          double groesse, String lieblingsfarbe) {
        User user = User.builder()
                .name(name)
                .geburtsdatum(geburtsdatum)
                .gewicht(gewicht)
                .groesse(groesse)
                .lieblingsfarbe(lieblingsfarbe)
                .build();
        userRepository.save(user);
    }

}

