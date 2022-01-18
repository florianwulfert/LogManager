package project.logManager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import project.logManager.model.entity.User;
import project.logManager.model.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BmiController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {
        "project.logManager"
})
@Transactional
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BmiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> getBmiArguments() {
        return Stream.of(
                Arguments.of("01.01.2003", "75.7", "1.85", status().isOk(),
                        "Der User hat einen BMI von 22.11 und ist somit normalgewichtig."),
                Arguments.of("01.01.1987", "95.2", "1.82", status().isOk(),
                        "Der User hat einen BMI von 28.74 und ist somit übergewichtig."),
                Arguments.of("01.01.2003", "61.3", "1.83", status().isOk(),
                        "Der User hat einen BMI von 18.3 und ist somit untergewichtig."),
                Arguments.of("01.01.1987", "0", "0", status().isInternalServerError(),
                        "Infinite or NaN"),
                Arguments.of("01.01.1987", "-1", "-1", status().isInternalServerError(),
                        "BMI konnte nicht berechnet werden"),
                Arguments.of("01.01.2000", "75.0", null, status().isBadRequest(),
                        "Required double parameter 'groesse' is not present"),
                Arguments.of("01.01.2000", null, "1.75", status().isBadRequest(),
                        "Required double parameter 'gewicht' is not present"),
                Arguments.of(null, "75.0", "1.75", status().isBadRequest(),
                        "Required LocalDate parameter 'geburtsdatum' is not present"),
                Arguments.of("01.01.2008", "75.0", "1.75", status().isOk(),
                        "Der User ist zu jung für eine genaue Bestimmung des BMI.")
        );
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("getBmiArguments")
    void testGetBmi(
            String date, String weight, String height, ResultMatcher status, String message)
            throws Exception {
        MvcResult result = mockMvc.perform(get("/bmi")
                        .param("geburtsdatum", date)
                        .param("gewicht", weight)
                        .param("groesse", height))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        Assertions.assertEquals(message, result.getResponse().getContentAsString());
    }

    private static Stream<Arguments> findUserAndCalculateBmiArguments() {
        return Stream.of(
                Arguments.of("untergewichtig", "/bmi/Torsten", status().isOk(),
                        "Der User hat einen BMI von 18.3 und ist somit untergewichtig."),
                Arguments.of("uebergewichtig", "/bmi/Peter", status().isOk(),
                        "Der User hat einen BMI von 28.74 und ist somit übergewichtig."),
                Arguments.of("normalgewichtig", "/bmi/Hans", status().isOk(),
                        "Der User hat einen BMI von 22.11 und ist somit normalgewichtig."),
                Arguments.of("userNichtBekannt", "/bmi/ActorNichtVorhanden", status().isInternalServerError(),
                        "User ActorNichtVorhanden konnte nicht identifiziert werden!")
        );
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("findUserAndCalculateBmiArguments")
    void testFindUserAndCalculateBmi(
            String weightStatus, String url, ResultMatcher status, String message
    ) throws Exception {
        createUser(weightStatus);
        MvcResult result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        Assertions.assertEquals(message, result.getResponse().getContentAsString());
    }

    private void createUser(String bewertung) {
        switch (bewertung) {
            case "untergewichtig":
                User torsten = User
                        .builder()
                        .name("Torsten")
                        .geburtsdatum(LocalDate.of(1985, 12, 5))
                        .bmi(18.3)
                        .gewicht(61.3)
                        .groesse(1.83)
                        .id(1)
                        .lieblingsfarbe("Blau")
                        .build();
                userRepository.save(torsten);
                break;
            case "uebergewichtig":
                User peter = User
                        .builder()
                        .name("Peter")
                        .geburtsdatum(LocalDate.of(1975, 5, 30))
                        .bmi(28.74)
                        .gewicht(95.2)
                        .groesse(1.82)
                        .id(2)
                        .lieblingsfarbe("Gelb")
                        .build();
                userRepository.save(peter);
                break;
            case "normalgewichtig":
                User hans = User
                        .builder()
                        .name("Hans")
                        .geburtsdatum(LocalDate.of(1993, 2, 3))
                        .bmi(22.11)
                        .gewicht(75.7)
                        .groesse(1.85)
                        .id(3)
                        .lieblingsfarbe("Rot")
                        .build();
                userRepository.save(hans);
                break;
            default:
                break;
        }
    }
}