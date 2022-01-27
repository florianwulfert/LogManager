package project.logManager.controller;

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

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static project.logManager.common.message.Messages.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BmiController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {"project.logManager"})
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
                Arguments.of("01.01.2003", "75.7", "1.85", status().isOk(), USER_NORMAL_WEIGHT),
                Arguments.of("01.01.1987", "95.2", "1.82", status().isOk(), USER_OVERWEIGHT),
                Arguments.of("01.01.2003", "61.3", "1.83", status().isOk(), USER_UNDERWEIGHT),
                Arguments.of("01.01.1987", "0", "0", status().isInternalServerError(), "Infinite or NaN"),
                Arguments.of("01.01.1987", "-1", "-1", status().isInternalServerError(), COULD_NOT_CALCULATE),
                Arguments.of("01.01.2000", "75.0", null, status().isBadRequest(), HEIGHT_NOT_PRESENT),
                Arguments.of("01.01.2000", null, "1.75", status().isBadRequest(), WEIGHT_NOT_PRESENT),
                Arguments.of(null, "75.0", "1.75", status().isBadRequest(), BIRTHDATE_NOT_PRESENT),
                Arguments.of("01.01.2008", "75.0", "1.75", status().isOk(), USER_TOO_YOUNG)
        );
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource("getBmiArguments")
    void testGetBmi(
            String date, String weight, String height, ResultMatcher status, String message) throws Exception {
        MvcResult result = mockMvc.perform(get("/bmi")
                        .param("birthdate", date)
                        .param("weight", weight)
                        .param("height", height))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        Assertions.assertEquals(message, result.getResponse().getContentAsString());
    }

    private static Stream<Arguments> findUserAndCalculateBmiArguments() {
        return Stream.of(
                Arguments.of("underweight", "/bmi/Torsten", status().isOk(), USER_UNDERWEIGHT),
                Arguments.of("overweight", "/bmi/Peter", status().isOk(), USER_OVERWEIGHT),
                Arguments.of("normalWeight", "/bmi/Hans", status().isOk(), USER_NORMAL_WEIGHT),
                Arguments.of("userNotIdentified", "/bmi/ActorNichtVorhanden", status().isInternalServerError(),
                        ACTOR_NOT_IDENTIFIED)
        );
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("findUserAndCalculateBmiArguments")
    void testFindUserAndCalculateBmi(
            String weightStatus, String url, ResultMatcher status, String message) throws Exception {
        createUser(weightStatus);
        MvcResult result = mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        Assertions.assertEquals(message, result.getResponse().getContentAsString());
    }

    private void createUser(String weightStatus) {
        switch (weightStatus) {
            case "underweight":
                User torsten = User
                        .builder()
                        .name("Torsten")
                        .birthdate(LocalDate.of(1985, 12, 5))
                        .bmi(18.3)
                        .weight(61.3)
                        .height(1.83)
                        .id(1)
                        .favouriteColor("Blue")
                        .build();
                userRepository.save(torsten);
                break;
            case "overweight":
                User peter = User
                        .builder()
                        .name("Peter")
                        .birthdate(LocalDate.of(1975, 5, 30))
                        .bmi(28.74)
                        .weight(95.2)
                        .height(1.82)
                        .id(2)
                        .favouriteColor("Yellow")
                        .build();
                userRepository.save(peter);
                break;
            case "normalWeight":
                User hans = User
                        .builder()
                        .name("Hans")
                        .birthdate(LocalDate.of(1993, 2, 3))
                        .bmi(22.11)
                        .weight(75.7)
                        .height(1.85)
                        .id(3)
                        .favouriteColor("Red")
                        .build();
                userRepository.save(hans);
                break;
            default:
                break;
        }
    }
}