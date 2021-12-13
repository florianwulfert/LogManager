package project.logManager.service.model;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);


    public void addUser(String actor, String name, LocalDate geburtsdatum, double gewicht,
                        double groesse, String lieblingsfarbe) {
        User user = User.builder()
                .name(name)
                .geburtsdatum(geburtsdatum)
                .gewicht(gewicht)
                .groesse(groesse)
                .lieblingsfarbe(lieblingsfarbe)
                .build();


        try {
            // prüfe, ob user bereits vorhanden ist
            if (findUserByName(name) != null) {
                throw new RuntimeException(String.format("User %s bereits vorhanden", name));
            }
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                saveUser(user, null);
                return;
            }
            User activeUser = findUserByName(actor);
            if (activeUser == null) {
                throw new RuntimeException(String.format("User %s nicht gefunden", actor));
            }
            saveUser(user, activeUser);
        }catch (RuntimeException ex) {
            logService.addLog("Der User konnte nicht angelegt werden", "ERROR", null);
            throw new RuntimeException(ex.getMessage());

        }
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : null;
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserByName(String userName) {
       try {
           return userRepository.findUserByName(userName).get(0);

       } catch (IndexOutOfBoundsException e) {
           LOGGER.warn(String.format("User mit dem Namen %s konnte nicht gefunden werden", userName));
           return null;
       }

    }

    private void saveUser(User user, User actor) {
        logService.addLog(String.format("Der User %s wurde angelegt", user.getName()), "INFO", actor);
        userRepository.save(user);
    }
}

