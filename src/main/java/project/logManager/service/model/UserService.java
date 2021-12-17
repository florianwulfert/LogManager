package project.logManager.service.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final ValidationService logValidationService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public void addUser(String actor, String name, LocalDate geburtsdatum, double gewicht,
                        double groesse, String lieblingsFarbe) {
        User user = User.builder()
                .name(name)
                .geburtsdatum(geburtsdatum)
                .gewicht(gewicht)
                .groesse(groesse)
                .lieblingsfarbe(lieblingsFarbe.toLowerCase())
                .build();
        if (logValidationService.validateFarbenEnum(lieblingsFarbe.toLowerCase())) {
            try {
                // prüfe, ob user bereits vorhanden ist
                if (findUserByName(name) != null) {
                    throw new RuntimeException(String.format("User %s bereits vorhanden", name));
                }
                // prüfe, ob noch kein User vorhanden ist
                List<User> users = userRepository.findAll();
                if (users.isEmpty()) {
                    saveUser(user, null);
                    return;
                }
                // prüfe, ob ausführender User vorhanden ist
                User activeUser = findUserByName(actor);
                if (activeUser == null) {
                    throw new RuntimeException(String.format("User %s nicht gefunden", actor));
                }
                saveUser(user, activeUser);

            } catch (RuntimeException ex) {
                logService.addLog("Der User konnte nicht angelegt werden", "ERROR", null);
                throw new RuntimeException(ex.getMessage());
            }
        } else {
            LOGGER.error("Given color '{}' is not allowed!", lieblingsFarbe);
            throw new IllegalArgumentException("Illegal color!");
        }
    }

    public List<User> findUserList() {
      return userRepository.findAll();
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : null;
    }

    public void deleteById(Integer id, User actor) {
        userRepository.deleteById(id);
        logService.addLog(String.format("User mit der id %s wurde gelöscht", id),"WARNING", actor);
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



