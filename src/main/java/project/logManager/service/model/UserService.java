package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.exception.UserNotFoundException;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;
import project.logManager.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final LogService logService;
    private final UserRepository userRepository;
    private final BmiService bmiService;
    private final LogRepository logRepository;
    private final UserValidationService userValidationService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public String addUser(String actor, String name, LocalDate geburtsdatum, double gewicht,
                          double groesse, String lieblingsFarbe) {

        User user = User.builder()
                .name(name)
                .geburtsdatum(geburtsdatum)
                .gewicht(gewicht)
                .groesse(groesse)
                .lieblingsfarbe(lieblingsFarbe.toLowerCase())
                .bmi(bmiService.berechneBMI(gewicht, groesse))
                .build();

        userValidationService.validateFarbenEnum(lieblingsFarbe.toLowerCase());
        userValidationService.checkIfUserToPostExists(name);
        if (userValidationService.checkIfUsersListIsEmpty(actor, user)) {
            saveUser(user, actor);
        } else {
            User activeUser = userValidationService.checkIfActorExists(actor);
            saveUser(user, activeUser.getName());
        }
        return bmiService.getBmiMessage(geburtsdatum, gewicht, groesse);
    }

    public List<User> findUserList() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    }

    public String deleteById(Integer id, String actorName) {
        Optional<User> user = findUserById(id);
        User actor = userRepository.findUserByName(actorName);
        if (actor == null) {
            throw new UserNotFoundException(actorName);
        }
        if (id.equals(actor.getId())) {
            LOGGER.error("Ein User kann sich nicht selbst löschen!");
            throw new RuntimeException("Ein User kann sich nicht selbst löschen!");
        }
        if (user.isEmpty()) {
            LOGGER.error(String.format("User mit der ID %s konnte nicht gefunden werden", id));
            throw new RuntimeException(String.format("User mit der ID %s konnte nicht gefunden werden", id));
        } else {
            if (logService.existLogByActorId(user.get())) {
                LOGGER.error(String.format("User %s kann nicht gelöscht werden, " +
                        "da er in einer anderen Tabelle referenziert wird!", user.get().getName()));
                throw new RuntimeException(String.format("User %s kann nicht gelöscht werden, " +
                        "da er in einer anderen Tabelle referenziert wird!", user.get().getName()));
            }
        }

        userRepository.deleteById(id);
        logService.addLog(String.format("User mit der ID %s wurde gelöscht.", id), "WARNING", actorName);
        LOGGER.info(String.format("User mit der ID %s wurde gelöscht.", id));
        return String.format("User mit der ID %s wurde gelöscht!", id);
    }

    public String deleteByName(String name, String actorName) {
        if (name.equals(actorName)) {
            LOGGER.error("Ein User kann sich nicht selbst löschen!");
            throw new RuntimeException("Ein User kann sich nicht selbst löschen!");
        }
        User userToDelete = userRepository.findUserByName(name);
        User actor = userRepository.findUserByName(actorName);
        if (userToDelete == null) {
            LOGGER.error(String.format("User mit dem Namen %s konnte nicht gefunden werden", name));
            throw new RuntimeException(String.format("User mit dem Namen %s konnte nicht gefunden werden", name));
        }
        if (logService.existLogByActorId(userToDelete)) {
            LOGGER.error(String.format("User %s kann nicht gelöscht werden, " +
                    "da er in einer anderen Tabelle referenziert wird!", name));
            throw new RuntimeException(String.format("User %s kann nicht gelöscht werden, " +
                    "da er in einer anderen Tabelle referenziert wird!", userToDelete.getName()));
        }
        if (actor == null) {
            LOGGER.error(String.format("User mit dem Namen %s konnte nicht gefunden werden", actorName));
            throw new RuntimeException(String.format("User mit dem Namen %s konnte nicht gefunden werden", actorName));
        }

        userRepository.deleteById(userToDelete.getId());
        logService.addLog(String.format("User mit dem Namen %s wurde gelöscht", name), "WARNING", actorName);
        LOGGER.info(String.format("User mit dem Namen %s wurde gelöscht", name));
        return String.format("User %s wurde gelöscht!", name);
    }

    public String deleteAll() {
        if (!logRepository.findAll().isEmpty()) {
            LOGGER.warn("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
                    "referenziert werden");
            throw new RuntimeException("User können nicht gelöscht werden, da sie in einer anderen Tabelle " +
                    "referenziert werden");
        }
        userRepository.deleteAll();
        LOGGER.info("Alle User wurden aus der Datenbank gelöscht!");
        return "Alle User wurden aus der Datenbank gelöscht";
    }

    public void saveUser(User user, String actor) {
        userRepository.save(user);
        String bmi = bmiService.getBmiMessage(user.getGeburtsdatum(), user.getGewicht(), user.getGroesse());
        logService.addLog(String.format("Der User %s wurde angelegt. %s", user.getName(), bmi),
                "INFO", actor);
        LOGGER.info(String.format("Der User %s wurde angelegt. " +
                        bmiService.getBmiMessage(user.getGeburtsdatum(), user.getGewicht(), user.getGroesse()),
                user.getName()));
    }
}
