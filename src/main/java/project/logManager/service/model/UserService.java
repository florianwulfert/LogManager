package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
                // prüfe, ob zu erstellender User bereits vorhanden ist
                if (userRepository.findUserByName(name) != null) {
                    LOGGER.warn(String.format("User %s bereits vorhanden", name));
                    throw new RuntimeException(String.format("User %s bereits vorhanden", name));
                }
                // prüfe, ob noch kein User vorhanden ist
                List<User> users = userRepository.findAll();
                if (users.isEmpty()) {
                    saveUser(user, null);
                    return;
                }
                // prüfe, ob ausführender User vorhanden ist
                User activeUser = userRepository.findUserByName(actor);
                if (activeUser == null) {
                    LOGGER.error("Active User (actor) ist null!");
                    throw new RuntimeException(String.format("User %s nicht gefunden", actor));
                }
                saveUser(user, activeUser.getName());

            } catch (RuntimeException ex) {
                LOGGER.error("Der User konnte nicht angelegt werden");
                logService.addLog("Der User konnte nicht angelegt werden", "ERROR", name);
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
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    }

    public void deleteById(Integer id, String actorName) {
        Optional<User> user = findUserById(id);
        User actor = userRepository.findUserByName(actorName);
        if (id.equals(actor.getId())) {
            LOGGER.error("Ein User kann sich nicht selbst löschen!");
            throw new RuntimeException("Ein User kann sich nicht selbst löschen!");
        }
        if (user.isEmpty()) {
            LOGGER.error(String.format("User mit der ID %s konnte nicht gefunden werden",id));
            throw new RuntimeException(String.format("User mit der ID %s konnte nicht gefunden werden", id));
        } else {
            if (logService.existLogByActorId(user.get())) {
                LOGGER.error(String.format( "User %s kann nicht gelöscht werden, " +
                        "da er in einer anderen Tabelle referenziert wird!", user.get().getName()));
                throw new RuntimeException(String.format("User %s kann nicht gelöscht werden, " +
                        "da er in einer anderen Tabelle referenziert wird!", user.get().getName()));
            }
        }

        userRepository.deleteById(id);
        logService.addLog(String.format("User mit der ID %s wurde gelöscht.", id), "WARNING", actorName);
        LOGGER.info(String.format("User mit der ID %s wurde gelöscht.", id ));
    }

    public void deleteByName(String name, String actorName) {
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
            LOGGER.error(String.format( "User %s kann nicht gelöscht werden, " +
                    "da er in einer anderen Tabelle referenziert wird!", name));
            throw new RuntimeException(String.format("User %s kann nicht gelöscht werden, " +
                    "da er in einer anderen Tabelle referenziert wird!", userToDelete.getName()));
        }
        if (actor == null) {
            LOGGER.error(String.format("User mit dem Namen %s konnte nicht gefunden werden", actorName ));
            throw new RuntimeException(String.format("User mit dem Namen %s konnte werden", actorName));
        }

        userRepository.deleteById(userToDelete.getId());
        logService.addLog(String.format("User mit dem Namen %s wurde gelöscht", name), "WARNING", actorName);
        LOGGER.info(String.format("User mit dem Namen %s wurde gelöscht", name));
    }

    public Double findUserAndCalculateBMI(String userName, Double gewicht, Double groesse) {
        userRepository.findUserByName(userName);
        return berechneBMI(gewicht, groesse);
    }

    private void saveUser(User user, String actor) {
        logService.addLog(String.format("Der User %s wurde angelegt. " +
                        berechneBmiWithMessage(user.getGeburtsdatum(), user.getGewicht(), user.getGroesse()), user.getName()),
                "INFO", actor);
        LOGGER.info(String.format("Der User %s wurde angelegt. " +
                berechneBmiWithMessage(user.getGeburtsdatum(), user.getGewicht(), user.getGroesse()),
                user.getName()));
        userRepository.save(user);
    }


    public Double berechneBMI(Double gewicht, Double groesse) {
        BigDecimal bigDecimal = new BigDecimal(gewicht / (groesse * groesse)).
                setScale(2, RoundingMode.DOWN);
        return bigDecimal.doubleValue();
    }

    public String berechneBmiWithMessage(LocalDate geburtsDatum, Double gewicht, Double groesse) {
        Double bmi = berechneBMI(gewicht, groesse);
        int alterUser = getAgeFromBirthDate(geburtsDatum);

        if (alterUser < 18) {
            LOGGER.warn("Der User ist zu jung für eine genaue Bestimmung des BMI.");
            return "Der User ist zu jung für eine genaue Bestimmung des BMI.";
        }

        if (bmi > 18.5 && bmi <= 25) {
            return String.format("Der User hat einen BMI von %s und ist somit normalgewichtig.", bmi);
        } else if (bmi <= 18.5 && bmi > 0) {
            return String.format("Der User hat einen BMI von %s und ist somit untergewichtig.", bmi);
        } else if (bmi > 25) {
            return String.format("Der User hat einen BMI von %s und ist somit übergewichtig.", bmi);
        } else {
            LOGGER.error("Unexpected value");
            throw new IllegalStateException("Unexpected value");
        }
    }

    public Integer getAgeFromBirthDate(LocalDate geburtsDatum) {
        return LocalDate.now().getYear() - geburtsDatum.getYear();
    }
}



