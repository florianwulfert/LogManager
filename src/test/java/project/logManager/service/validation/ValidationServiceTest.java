package project.logManager.service.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.common.dto.LogMessageDto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @InjectMocks
    ValidationService systemUnderTest;

    @Test
    void validateSeverity() {
        assertTrue(systemUnderTest.validateSeverity("INFO"));
    }

    @Test
    void validateSeverityWrongSeverity() {
        assertFalse(systemUnderTest.validateSeverity("KATZE"));
    }

    @Test
    void validateUserFarben() {
        assertTrue(systemUnderTest.validateFarbenEnum("blau"));
    }

    @Test
    void validateWrongUserFarben() {
        assertFalse(systemUnderTest.validateFarbenEnum("gold"));
    }

    @Test
    void validateMessageIsKatze() {
        LogMessageDto customLogMessageDto = LogMessageDto.builder()
                .message("Hund")
                .returnMessage("Katze wurde in Hund übersetzt!\n")
                .build();
        Assertions.assertEquals(customLogMessageDto, systemUnderTest.validateMessage("Katze"));
    }

    @Test
    void validateMessageIsNotKatze() {
        LogMessageDto customLogMessageDto = LogMessageDto.builder()
                .message("Apfel")
                .returnMessage("")
                .build();
        Assertions.assertEquals(customLogMessageDto, systemUnderTest.validateMessage("Apfel"));
    }
}
