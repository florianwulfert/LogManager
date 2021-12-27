package project.logManager.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.logManager.model.entity.BMI;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;
import project.logManager.service.validation.ValidationService;

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
    private final ValidationService logValidationService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public User addUser(String actor, String name, LocalDate geburtsdatum, double gewicht,
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
                    return user;
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
        return user;
    }

    public List<User> findUserList() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id).isPresent() ? userRepository.findById(id) : Optional.empty();
    }

    public User deleteById(Integer id, User actor) {
        Optional<User> user = findUserById(id);
        if (id.equals(actor.getId())) {
            throw new RuntimeException("Ein User kann sich nicht selbst löschen!");
        }
        if (user.isEmpty()) {
            throw new RuntimeException(String.format("User mit der ID %s konnte nicht gefunden werden", id));
        } else {
            if (logService.searchLogByActorId(user.get())) {
                throw new RuntimeException(String.format("User %s kann nicht gelöscht werden, " +
                        "da er in einer anderen Tabelle referenziert wird!", user.get().getName()));
            }
        }

        userRepository.deleteById(id);
        logService.addLog(String.format("User mit der ID %s wurde gelöscht", id), "WARNING", actor);
        return user.get();
    }

    public User findUserByName(String userName) {
        try {
            return userRepository.findUserByName(userName).get(0);
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn(String.format("User mit dem Namen %s konnte nicht gefunden werden", userName));
            return null;
        }
    }

    public String findUserAndCalculateBMI(String userName) {
        User user = findUserByName(userName);
        String bmi = berechneBMI(user.getGeburtsdatum(), user.getGroesse(), user.getGewicht());
        return bmi;
    }

    private void saveUser(User user, User actor) {
        logService.addLog(String.format("Der User %s wurde angelegt. " +
                berechneBMI(user.getGeburtsdatum(), user.getGroesse(), user.getGewicht()), user.getName()),
                "INFO", actor);
        userRepository.save(user);
    }


    public String berechneBMI(LocalDate geburtsDatum, Double groesse, Double gewicht) {
        BMI bmiUser = BMI.builder()
                .alter(getAgeFromBirthDate(geburtsDatum))
                .groesse(groesse)
                .gewicht(gewicht)
                .build();

        double bmi = bmiUser.getGewicht() / (bmiUser.getGroesse() * bmiUser.getGroesse());

        if (bmiUser.getAlter() < 18) {
            throw new RuntimeException("Der User ist zu jung für eine genaue Bestimmung des BMI");
        }

        if (bmiUser.getAlter() >= 18 || bmiUser.getAlter() <= 40) {

            if (bmi <= 18.5) {
                return String.format("Der User hat einen BMI von %s und ist somit untergewichtig",
                        bmi);
            } else if (bmi > 18.5) {
                return String.format("Der User hat einen BMI von %s und ist somit normalgewichtig",
                        bmi);
            } else if (bmi > 25) {
                return String.format("Der User hat einen BMI von %s und ist somit übergewichtig", bmi);
            } else {
                throw new IllegalStateException("Unexpected value: " + bmi);
            }
        }

        if (bmiUser.getAlter() > 40) {
            if (bmi <= 18.5) {
                return String.format("Der User hat einen BMI von %s und ist somit untergewichtig" +
                        "(aufgrund des Alters kann der eigentliche BMI-Wert leicht verschoben sein)", bmi);
            } else if (bmi > 18.5) {
                return String.format("Der User hat einen BMI von %s und ist somit normalgewichtig" +
                        "(aufgrund des Alters kann der eigentliche BMI-Wert leicht verschoben sein)", bmi);
            } else if (bmi > 25) {
                return String.format("Der User hat einen BMI von %s und ist somit übergewichtig" +
                        "(aufgrund des Alters kann der eigentliche BMI-Wert leicht verschoben sein)", bmi);
            } else {
                throw new IllegalStateException("Unexpected value: " + bmi);
            }

        }return bmiUser.getWeightMessage();

    }

    public Integer getAgeFromBirthDate(LocalDate geburtsDatum) {
        return geburtsDatum.getYear() - LocalDate.now().getYear();
    }






}



