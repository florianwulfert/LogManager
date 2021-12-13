package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;

    public String addUser(String name, LocalDate geburtsdatum, double gewicht,
                        double groesse, String lieblingsfarbe) {
        User user = User.builder()
                .name(name)
                .geburtsdatum(geburtsdatum)
                .gewicht(gewicht)
                .groesse(groesse)
                .lieblingsfarbe(lieblingsfarbe)
                .build();


        try {
            String message = String.format(

                    "INFO",
                    "Peter"
            );
            return String.format(logService.addLog("Der User %s wurde angelegt", "INFO", User.builder().name("Peter").build()));

        }catch (RuntimeException ex) {
            logService.addLog("Der User konnte nicht angelegt werden", "ERROR", User.builder().name("Peter").build());
            throw new RuntimeException(ex.getMessage());

        }
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : null;
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    //public Optional<Log> findUserByName(String userName) {
     //   return userRepository.findUserByName(userName);
    //}
}

