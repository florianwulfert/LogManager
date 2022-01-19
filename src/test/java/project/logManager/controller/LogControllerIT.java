package project.logManager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import project.logManager.model.entity.Log;
import project.logManager.model.entity.User;
import project.logManager.model.repository.LogRepository;
import project.logManager.model.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureDataJpa
@ComponentScan(basePackages = {"project.logManager"})
@Transactional
@TestPropertySource("/application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setup() {
        createLogs();
    }

    private Stream<Arguments> getLogsArgument() {
        String jahr1999 = "12.12.1999 12:12:12";
        String jahr2005 = "14.01.2005 12:10:10";
        String jahr2004 = "15.02.2004 15:12:14";
        String jahr1995 = "16.04.1995 18:12:14";
        String jahr2015 = "25.11.2015 17:18:15";
        String jahr2002 = "12.03.2002 18:19:20";

        return Stream.of(
                Arguments.of("AllLogs", null, null, null, null, status().isOk(), 10),
                Arguments.of("FilterLogsBySeverity", "WARNING", null, null, null, status().isOk(), 2),
                Arguments.of("FilterLogsByMessage", null, "Test", null, null, status().isOk(), 5),
                Arguments.of("FilterLogsByStartTime", null, null, jahr2004, null, status().isOk(), 6),
                Arguments.of("FilterLogsByEndTime", null, null, null, jahr2002, status().isOk(), 2),
                Arguments.of("FilterLogsBySeverityAndStartTime", "INFO", null, jahr1999, null, status().isOk(), 2),
                Arguments.of("FilterLogsBySeverityAndEndTime", "INFO", null, null, jahr2005, status().isOk(), 2),
                Arguments.of("FilterLogsBySeverityAndMessage", "WARNING", "Warning", null, null, status().isOk(), 1),
                Arguments.of("FilterLogsByMessageAnStartTime", null, "Test", jahr1995, null, status().isOk(), 5),
                Arguments.of("FilterLogsByMessageAndEndTime", null, "Debug", null, jahr2015, status().isOk(), 1),
                Arguments.of("FilterLogsByStartTimeAndEndTime", null, null, jahr1999, jahr2005, status().isOk(), 5),
                Arguments.of("FilterLogsBySeverityAndMessageAndStartTime", "TRACE", "Trace", jahr1999, null, status().isOk(), 1),
                Arguments.of("FilterLogsBySeverityAndMessageAndEndTime", "TRACE", "Trace", null, jahr2015, status().isOk(), 1),
                Arguments.of("FilterLogsBySeverityAndStartTimeAndEndTime", "TRACE", null, jahr1999, jahr2015, status().isOk(), 2),
                Arguments.of("FilterLogsByMessageAndStartTimeAndEndTime", null, "Debug", jahr1999, jahr2005, status().isOk(), 1),
                Arguments.of("FilterLogsBySeverityAndMessageAndStartTimeAndEndTime", "ERROR", "Test", jahr1999, jahr2015, status().isOk(), 1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getLogsArgument")
    void testFilterLogs(String testName, String severity, String message, String startDateTime, String endDateTime,
                        ResultMatcher status, Integer logNumber) throws Exception {
        mockMvc.perform(get("/logs")
                        .param("severity", severity)
                        .param("message", message)
                        .param("startDateTime", startDateTime)
                        .param("endDateTime", endDateTime))
                .andDo(print())
                .andExpect(status)
                .andExpect(jsonPath("$.*", hasSize(logNumber)))
                .andReturn();
    }

    @Test
    void testSeverityIsFalseAtGetLogs() throws Exception {
        MvcResult result = mockMvc.perform(get("/logs")
                        .param("severity", "hi"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("Severity falsch!", result.getResponse().getContentAsString());
    }


    private static Stream<Arguments> addLogArguments() {
        return Stream.of(
                Arguments.of("PostLog", "INFO", "Test", "Petra", status().isOk(), "Es wurde die Nachricht \"Test\" als INFO abgespeichert!"),
                Arguments.of("MessageIsMissing", "DEBUG", null, "Petra", status().isBadRequest(), "Required String parameter 'message' is not present"),
                Arguments.of("SeverityIsMissing", null, "Severity fehlt", "Petra", status().isBadRequest(), "Required String parameter 'severity' is not present"),
                Arguments.of("UserIsMissing", "INFO", "Test", null, status().isBadRequest(), "Required String parameter 'nameUser' is not present"),
                Arguments.of("SeverityIsFalse", "hi", "Test", "Petra", status().isInternalServerError(), "Severity falsch!"),
                Arguments.of("UserIsFalse", "INFO", "Test", "Hans", status().isInternalServerError(), "User Hans nicht gefunden"),
                Arguments.of("KatzeInHund", "INFO", "Katze", "Petra", status().isOk(), "Katze wurde in Hund übersetzt!\nEs wurde die Nachricht \"Hund\" als INFO abgespeichert!")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("addLogArguments")
    void testAddLog(String testName, String severity, String message, String nameUser, ResultMatcher status,
                    String returnMessage) throws Exception {
        createUser("Petra");
        MvcResult result = mockMvc.perform(post("/log")
                        .param("severity", severity)
                        .param("message", message)
                        .param("nameUser", nameUser))
                .andDo(print())
                .andExpect(status)
                .andReturn();

        Assertions.assertEquals(returnMessage, result.getResponse().getContentAsString());
    }


    @Test
    void testGetLogsById() throws Exception {
        MvcResult result = mockMvc.perform(get("/logs/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("[{\"severity\":\"INFO\",\"message\":\"Test\",\"timestamp\":\"2000-12-12T12:12:12\"}]",
                result.getResponse().getContentAsString());
    }

    @Test
    void testIdForGetLogsNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/logs/50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("[null]", result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteLogsById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/logs/delete/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Eintrag mit der ID 2 wurde aus der Datenbank gelöscht",
                result.getResponse().getContentAsString());
    }

    @Test
    void testIdForDeleteLogsNotFound() throws Exception {
        MvcResult result = mockMvc.perform(delete("/logs/delete/20"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        Assertions.assertEquals("No class project.logManager.model.entity.Log entity with id 20 exists!",
                result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteLogsBySeverity() throws Exception {
        MvcResult result = mockMvc.perform(delete("/logs/delete/severity")
                .param("severity", "INFO"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Es wurden die Einträge mit den IDs 1, 2 aus der Datenbank gelöscht",
                result.getResponse().getContentAsString());
    }

    @Test
    void testWrongSeverityForDelete() throws Exception {
        MvcResult result = mockMvc.perform(delete("/logs/delete/severity")
                        .param("severity", "hi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Keine Einträge gefunden!", result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteAll() throws Exception {
        MvcResult result = mockMvc.perform(delete("/logs/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Alle Logs wurden aus der Datenbank gelöscht!",
                result.getResponse().getContentAsString());
    }


    private void createLog(Integer id, String severity, String message, LocalDateTime timestamp) {
        logRepository.save(Log.builder()
                .id(id)
                .severity(severity)
                .message(message)
                .timestamp(timestamp)
                .build());
    }

    private void createUser(String user) {
        switch (user) {
            case "Petra":
                User petra = User
                        .builder()
                        .id(1)
                        .name("Petra")
                        .geburtsdatum(LocalDate.of(1999, 12, 13))
                        .bmi(25.39)
                        .gewicht(65)
                        .groesse(1.60)
                        .lieblingsfarbe("Rot")
                        .build();
                userRepository.save(petra);
                break;
            case "Torsten":
                User torsten = User
                        .builder()
                        .name("Torsten")
                        .geburtsdatum(LocalDate.of(1985, 12, 5))
                        .bmi(18.3)
                        .gewicht(61.3)
                        .groesse(1.83)
                        .id(2)
                        .lieblingsfarbe("Blau")
                        .build();
                userRepository.save(torsten);
                break;
            case "Hans":
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

    private void createLogs() {
        createLog(1, "INFO", "Test", LocalDateTime.of(2000, 12, 12, 12, 12, 12));
        createLog(2, "INFO", "Info", LocalDateTime.of(2001, 12, 12, 12, 12, 12));
        createLog(3, "WARNING", "Warning", LocalDateTime.of(2002, 12, 12, 12, 12, 12));
        createLog(4, "WARNING", "Test", LocalDateTime.of(2003, 12, 12, 12, 12, 12));
        createLog(5, "DEBUG", "Debug", LocalDateTime.of(2004, 12, 12, 12, 12, 12));
        createLog(6, "DEBUG", "Test", LocalDateTime.of(2005, 12, 12, 12, 12, 12));
        createLog(7, "ERROR", "Error", LocalDateTime.of(2006, 12, 12, 12, 12, 12));
        createLog(8, "ERROR", "Test", LocalDateTime.of(2007, 12, 12, 12, 12, 12));
        createLog(9, "TRACE", "Trace", LocalDateTime.of(2008, 12, 12, 12, 12, 12));
        createLog(10, "TRACE", "Test", LocalDateTime.of(2009, 12, 12, 12, 12, 12));
    }
}
