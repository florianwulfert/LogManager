package project.userFeaturePortal.service.model;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.userFeaturePortal.common.dto.log.AddLogRequestDto;
import project.userFeaturePortal.common.dto.log.LogRequestDto;
import project.userFeaturePortal.common.dto.user.UserDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.message.ErrorMessages;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.model.entity.Book;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.mapper.UserDtoMapper;
import project.userFeaturePortal.model.repository.BookRepository;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.validation.BookValidationService;
import project.userFeaturePortal.service.validation.UserValidationService;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
  private final BookRepository bookRepository;
  private final UserDtoMapper userDtoMapper;
  private final BookValidationService bookValidationService;

  public String addUser(UserRequestDto userRequestDto) {
    userValidationService.checkIfAnyEntriesAreNull(userRequestDto);
    userValidationService.validateUserToCreate(userRequestDto.name);
    userValidationService.validateActor(userRequestDto.name, userRequestDto.actor);

    userRepository.save(buildUser(userRequestDto, new User()));

    logService.addLog(LogRequestDto.builder()
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format(InfoMessages.USER_CREATED, userRequestDto.getName()))
                    .severity("INFO")
                    .build())
            .user(userRequestDto.actor)
            .build());
    LOGGER.info(String.format(InfoMessages.USER_CREATED, userRequestDto.getName()));
    return String.format(InfoMessages.USER_CREATED, userRequestDto.getName());
  }

  private User buildUser(UserRequestDto userRequestDto, User user) {
    List<Book> books = bookRepository.findByTitel(userRequestDto.favouriteBook);
    Book book = null;
    if (!books.isEmpty()) {
      book = books.get(0);
    }

    user.setName(userRequestDto.name);
    user.setBirthdate(userRequestDto.getBirthdateAsLocalDate());
    user.setWeight(userRequestDto.weight);
    user.setHeight(userRequestDto.height);
    user.setBmi(bmiService.calculateBMI(userRequestDto.weight, userRequestDto.height));
    user.setFavouriteBook(book);
    return user;
  }

  public String updateUser(UserRequestDto userRequestDto) {
    userValidationService.checkIfAnyEntriesAreNull(userRequestDto);
    User user = userValidationService.checkIfNameExists(userRequestDto.name, false, "");
    userValidationService.validateActor(userRequestDto.name, userRequestDto.actor);

    buildUser(userRequestDto, user);
    LOGGER.info(String.format(InfoMessages.USER_UPDATED, userRequestDto.name));
    return String.format(InfoMessages.USER_UPDATED, userRequestDto.name);
  }

  public String addFavouriteBookToUser(String titel, String userName) {
    User user = userValidationService.checkIfNameExists(userName, true, ErrorMessages.USER_NOT_ALLOWED);
    Book book = bookValidationService.checkIfBookExists(titel);
    user.setFavouriteBook(book);

    userRepository.save(user);

    LOGGER.info(String.format(InfoMessages.BOOK_BY_USER, titel, user.getName()));
    return user.getFavouriteBook().getTitel();
  }

  public String deleteFavouriteBook(String userName) {
    User user = userValidationService.checkIfNameExists(userName, false, String.format(ErrorMessages.USER_NOT_FOUND_NAME, userName));
    user.setFavouriteBook(null);

    userRepository.save(user);

    LOGGER.info(String.format(InfoMessages.FAV_BOOK_DELETED, userName));
    return "";
  }

  public String getFavouriteBook(String name) {
    User user = userValidationService.checkIfNameExists(name, false, String.format(ErrorMessages.USER_NOT_FOUND_NAME, name));
    if (user.getFavouriteBook() != null) {
      return user.getFavouriteBook().getTitel();
    }
    return "";
  }

  public List<UserDto> findUserList() {
    List<User> users = userRepository.findAll();

    List<String> userNames = new ArrayList<>();
    for (User user: users)  {
        userNames.add(user.getName());
    }
    LOGGER.info("Actual Users: " + userNames);

    return userDtoMapper.usersToUserDtos(users);
  }

  public Optional<User> findUserById(Integer id) {
    return userRepository.findById(id);
  }

  public UserDto findUserByName(String name) {
    User user = userRepository.findUserByName(name);
    return userDtoMapper.userToUserDto(user);
  }

  public boolean validateUserByName(String name) {
    User user = userRepository.findUserByName(name);

    if (user == null) {
      List<UserDto> users = findUserList();
      return users.isEmpty();
    }

    return true;
  }

  public void deleteById(int id, String actorName) {
    // validate actor
    userValidationService.checkIfNameExists(actorName, true, ErrorMessages.USER_NOT_ALLOWED_DELETE_USER);

    // proof that the ID you want to delete exists
    User userToDelete = userValidationService.checkIfIdExists(id);

    // validate user you want to delete
    userValidationService.validateUserToDelete(userToDelete.getName(), actorName);

    userRepository.deleteById(userToDelete.getId());

    logService.addLog(LogRequestDto.builder()
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format(InfoMessages.USER_DELETED_ID, userToDelete.getId()))
                    .severity("WARNING")
                    .build())
            .user(actorName)
            .build());
    LOGGER.info(String.format(InfoMessages.USER_DELETED_ID, userToDelete.getId()));
  }

  public String deleteByName(String name, String actorName) {
    // validate actor
    userValidationService.checkIfNameExists(actorName, true, String.format(ErrorMessages.USER_NOT_ALLOWED_DELETE_USER, actorName));

    // validate user you want to delete
    User userToDelete = userValidationService.validateUserToDelete(name, actorName);

    userRepository.deleteById(userToDelete.getId());

    logService.addLog(LogRequestDto.builder()
            .addLogRequest(AddLogRequestDto.builder()
                    .message(String.format(InfoMessages.USER_DELETED_NAME, name))
                    .severity("WARNING")
                    .build())
            .user(actorName)
            .build());
    LOGGER.info(String.format(InfoMessages.USER_DELETED_NAME, name));
    return String.format(InfoMessages.USER_DELETED_NAME, name);
  }

  public String deleteAll() {
    userValidationService.checkIfUsersAreReferenced();

    userRepository.deleteAll();

    LOGGER.info(InfoMessages.ALL_USERS_DELETED);
    return InfoMessages.ALL_USERS_DELETED;
  }
}
