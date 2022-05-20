package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.userFeaturePortal.common.dto.user.FindUserResponseDto;
import project.userFeaturePortal.common.dto.user.UserRequestDto;
import project.userFeaturePortal.common.dto.user.UserResponseDto;
import project.userFeaturePortal.common.dto.user.ValidateUserResponseDto;
import project.userFeaturePortal.controller.API.UserAPI;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.model.repository.UserRepository;
import project.userFeaturePortal.service.model.UserService;

import java.util.Optional;

@AllArgsConstructor(onConstructor_ = { @Autowired })
@RestController
@CrossOrigin
@Tag(name = "User")
public class UserController implements UserAPI {

  private final UserService userService;
  private final UserRepository userRepository;

  @Override
  public ResponseEntity<UserResponseDto> addUser(UserRequestDto allParameters) {
    String returnMessage = userService.addUser(allParameters);
    return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }

  @Override
  public String addFavouriteBookToUser(String bookTitel, String actor) {
    return userService.addFavouriteBookToUser(bookTitel, actor);
  }

  @Override
  public String deleteFavouriteBook(String name) {
    return userService.deleteFavouriteBook(name);
  }

  @Override
  public ResponseEntity<UserResponseDto> updateUser(UserRequestDto allParameters) {
    String returnMessage = userService.updateUser(allParameters);
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }

  @Override
  public ResponseEntity<UserResponseDto> findUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), null));
  }

  @Override
  public Optional<User> findUserByID(Integer id) {
    return userService.findUserById(id);
  }

  @Override
  public ResponseEntity<FindUserResponseDto> findUserByName(String name) {
    return ResponseEntity.status(HttpStatus.OK).body(new FindUserResponseDto(userService.findUserByName(name)));
  }

  @Override
    public ResponseEntity<ValidateUserResponseDto> validateUserByName(String name) {
      return ResponseEntity.status(HttpStatus.OK).body(new ValidateUserResponseDto(userService.validateUserByName(name)));
  }

  @Override
  public ResponseEntity<UserResponseDto> deleteUserByID(Integer id, String actor) {
    userService.deleteById(id, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), null));
  }

  @Override
  public ResponseEntity<UserResponseDto> deleteUserByName(String name, String actor) {
    String returnMessage = userService.deleteByName(name, actor);
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }

  @Override
  public ResponseEntity<UserResponseDto> deleteAll() {
    String returnMessage = userService.deleteAll();
    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(userService.findUserList(), returnMessage));
  }
}
