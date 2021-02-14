package project.logManager.model.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.Log;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author - EugenFriesen
 * 14.02.2021
 **/

@ExtendWith(MockitoExtension.class)
class LogDTOMapperTest {

    @InjectMocks
    LogDTOMapper systemUnderTest;

    @Test
    void mapLogsToLogDTOs() {
        List<Log> logs = new ArrayList<>();
        systemUnderTest.mapLogsToLogDTOs(logs);
    }
}