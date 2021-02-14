package project.logManager.service.validation;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author - EugenFriesen
 * 13.02.2021
 **/

@ExtendWith(MockitoExtension.class)
class LogValidationServiceTest {

    @InjectMocks
    LogValidationService systemUnderTest;

    @Test
    void validateSeverity() {
        assertTrue(systemUnderTest.validateSeverity("INFO"));
    }

    @Test
    void validateSeverityWrongSeverity() {
        assertFalse(systemUnderTest.validateSeverity("KATZE"));
    }
}