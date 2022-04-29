package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.dto.user.UserDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.mapper.UserDtoMapper;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BookValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

  private static final Logger LOGGER = LogManager.getLogger(UserService.class);
  private final LogService logService;
  private final UserRepository userRepository;
  private final BmiService bmiService;
  private final UserValidationService userValidationService;
  private final BookService bookService;
  private final UserDtoMapper userDtoMapper;
  private final BookValidationService bookValidationService;

  public String addUser(UserRequestDto userRequestDto) {
    userValidationService.checkIfAnyEntriesAreNull(userRequestDto);
    User user = buildUserToCreate(userRequestDto);

    if (userValidationService.validateUserToCreate(user.getName(), userRequestDto.actor)) {
      saveUser(user, userRequestDto.actor);
      return String.format(InfoMessages.USER_CREATED, userRequestDto.name);
    }

    if (userValidationService.validateActor(userRequestDto.actor, ErrorMessages.USER_NOT_ALLOWED_CREATE_USER)) {
      saveUser(user, userRequestDto.actor);
      return String.format(InfoMessages.USER_CREATED, userRequestDto.name);
    }

    return ErrorMessages.USER_CREATION_NOT_SUCCEED;
  }

  private User buildUserToCreate(UserRequestDto userRequestDto) {
    List<Book> books = bookService.searchBooksByTitel(userRequestDto.favouriteBook);
    Book book = null;
    if (!books.isEmpty()) {
      book = books.get(0);
    }
    return User.builder()
        .name(userRequestDto.name)
        .birthdate(userRequestDto.getBirthdateAsLocalDate())
        .weight(userRequestDto.weight)
        .height(userRequestDto.height)
        .bmi(bmiService.calculateBMI(userRequestDto.weight, userRequestDto.height))
        .favouriteBook(book)
        .build();
  }

  public String addFavouriteBookToUser(String titel, String userName) {
    User user = userValidationService.checkIfNameExists(userName, true, ErrorMessages.USER_NOT_ALLOWED);
    Book book = bookValidationService.checkIfBookExists(titel);
    user.setFavouriteBook(book);

    userRepository.save(user);

    LOGGER.info(String.format(InfoMessages.BOOK_BY_USER, titel, user.getName()));
    return String.format(InfoMessages.BOOK_BY_USER, titel, user.getName());
  }

  public List<UserDto> findUserList() {
    List<User> users = userRepository.findAll();
    LOGGER.info("Actual Users: users");
    return userDtoMapper.usersToUserDtos(users);
  }

  public Optional<User> findUserById(Integer id) {
    Optional<User> user = userRepository.findById(id);
    LOGGER.info(user);
    return user.isPresent() ? userRepository.findById(id) : Optional.empty();
  }

  public boolean findUserByName(String name) {
    User user = userRepository.findUserByName(name);

    if (user == null) {
      List<UserDto> users = findUserList();
      return users.isEmpty();
    }

    LOGGER.debug(String.format(InfoMessages.USER_FOUND, name));
    return true;
  }

  public void deleteById(Integer id, String actorName) {
    userValidationService.validateDeletingById(id, actorName);

    userRepository.deleteById(id);

    saveLog(String.format(InfoMessages.USER_DELETED_ID, id), "WARNING", actorName);
    LOGGER.info(String.format(InfoMessages.USER_DELETED_ID, id));
  }

  public String deleteByName(String name, String actorName) {
    userValidationService.validateActor(actorName, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER);
    User user = userValidationService.validateUserToDelete(name, actorName);

    userRepository.deleteById(user.getId());

    saveLog(String.format(InfoMessages.USER_DELETED_NAME, name), "WARNING", actorName);
    LOGGER.info(String.format(InfoMessages.USER_DELETED_NAME, name));
    return String.format(InfoMessages.USER_DELETED_NAME, name);
  }

  public String deleteAll() {
    userValidationService.checkIfUsersAreReferenced();

    userRepository.deleteAll();

    LOGGER.info(InfoMessages.ALL_USERS_DELETED);
    return InfoMessages.ALL_USERS_DELETED;
  }

  private void saveUser(User user, String actor) {
    userRepository.save(user);

    saveLog(String.format(InfoMessages.USER_CREATED, user.getName()), "INFO", actor);
    LOGGER.info(String.format(InfoMessages.USER_CREATED, user.getName()));
  }

  private void saveLog(String message, String severity, String actor) {
    LogRequestDto logRequestDto = LogRequestDto.builder()
        .message(message)
        .severity(severity)
        .user(actor)
        .build();
    LOGGER.info("Log " + logRequestDto + String.format(" was saved as %s", severity));
    logService.addLog(logRequestDto);
  }
}
