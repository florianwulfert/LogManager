package project.logManager.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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

}