package project.logManager.service.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import project.logManager.model.entity.User;
import project.logManager.model.respository.UserRepository;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)


class UserServiceTest {

    @InjectMocks
    UserService systemUnderTest;

    @Mock
    UserRepository userRepository;

    @Captor
    ArgumentCaptor<User> arg;

    @Test
    void testAddUser() {
        systemUnderTest.addUser("Peter", LocalDate.of(1988, 12, 12), 90,
                1.85, "blau");
        Mockito.verify(userRepository).save(Mockito.any());
    }
}