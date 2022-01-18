package project.logManager.controller;

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
import project.logManager.model.repository.LogRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static Stream<Arguments> getLogsArgument() {
        LocalDateTime dateTime;
        return Stream.of(
                Arguments.of("AllLogs", null, null, null, null, status().isOk(), 10),
                Arguments.of("FilterLogsBySeverity", "WARNING", null, null, null, status().isOk(), 2),
                Arguments.of("WARN", "Achtung", null, null, status().isOk(), 2),
                Arguments.of("WARN", "Achtung", null, null, status().isOk(), 2),
                Arguments.of("WARN", null, null, null, status().isOk(), 2),
                Arguments.of("WARN", null, null, null, status().isOk(), 2)

                );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("getLogsArgument")
    void testGetLogs(
            String testName, String severity, String message, String startDateTime, String endDateTime,
            ResultMatcher status, Integer logNumber
    ) throws Exception {
        createLogs();
        MvcResult result = mockMvc.perform(get("/logs")
                .param("severity", severity)
                .param("message", message)
                .param("startDateTime", startDateTime)
                .param("endDateTime", endDateTime))
                .andDo(print())
                .andExpect(status)
                .andExpect(jsonPath("$.*", hasSize(logNumber)))
                .andReturn();
    }

    private void createLogs() {
        logRepository.save(Log.builder()
                .id(1)
                .severity("INFO")
                .message("Test")
                .timestamp(LocalDateTime.of(2000,12,12,12,12,12))
                .build());
        logRepository.save(Log.builder()
                .id(2)
                .severity("WARNING")
                .message("Achtung Test")
                .timestamp(LocalDateTime.of(1999, 12,16,15,12,16))
                .build());
        logRepository.save(Log.builder()
                .id(3)
                .severity("FATAL")
                .message("Test5")
                .timestamp(LocalDateTime.of(2000,12,12,12,12,12))
                .build());
        logRepository.save(Log.builder()
                .id(4)
                .severity("TRACE")
                .message("Achtung")
                .timestamp(LocalDateTime.of(1999, 12,16,15,12,16))
                .build());
        logRepository.save(Log.builder()
                .id(5)
                .severity("INFO")
                .message("Katze")
                .timestamp(LocalDateTime.of(1974,12,12,12,12,12))
                .build());
        logRepository.save(Log.builder()
                .id(6)
                .severity("WARNING")
                .message("Achtung")
                .timestamp(LocalDateTime.of(2010, 12,16,15,12,16))
                .build());
        logRepository.save(Log.builder()
                .id(7)
                .severity("ERROR")
                .message("Fehler")
                .timestamp(LocalDateTime.of(1977,12,12,12,12,12))
                .build());
        logRepository.save(Log.builder()
                .id(8)
                .severity("ERROR")
                .message("Fehler")
                .timestamp(LocalDateTime.of(1968, 12,16,15,12,16))
                .build());
        logRepository.save(Log.builder()
                .id(9)
                .severity("INFO")
                .message("Test")
                .timestamp(LocalDateTime.of(1985,12,12,12,12,12))
                .build());
        logRepository.save(Log.builder()
                .id(10)
                .severity("DEBUG")
                .message("debug")
                .timestamp(LocalDateTime.of(1995, 12,16,15,12,16))
                .build());

    }
}
