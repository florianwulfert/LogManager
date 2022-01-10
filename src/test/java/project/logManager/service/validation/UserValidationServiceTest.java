package project.logManager.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @InjectMocks
    UserValidationService systemUnderTest;

    @Test
    void validateUserFarben() {
        assertTrue(systemUnderTest.validateFarbenEnum("blau"));
    }

    @Test
    void validateWrongUserFarben() {
        assertFalse(systemUnderTest.validateFarbenEnum("gold"));
    }


}