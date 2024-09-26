package project.userFeaturePortal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import project.userFeaturePortal.common.dto.user.*;
import project.userFeaturePortal.common.message.InfoMessages;
import project.userFeaturePortal.controller.API.UserAPI;
import project.userFeaturePortal.exception.LoginUserEmptyException;
import project.userFeaturePortal.model.entity.User;
import project.userFeaturePortal.service.model.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@AllArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@CrossOrigin
@Tag(name = "User")
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public boolean login(User user) {
      if (user.getName() == null) {
        throw new LoginUserEmptyException("Please fill name for user!");
      }
      return user.getName().equals("devs") && user.getPassword().equals("Test");
    }

    @Override
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder()
                .decode(authToken)).split(":")[0];
    }

    @Override
    public ResponseEntity<UserResponseDto> addUser(UserRequestDto allParameters) {
        String returnMessage = userService.addUser(allParameters);
        return ResponseEntity.status(201).body(new UserResponseDto(userService.findUserList(), returnMessage));
    }

    @Override
    public ResponseEntity<GetFavouriteBookResponseDto> addFavouriteBookToUser(String bookTitel, String actor) {
        String returnMessage = String.format(InfoMessages.BOOK_BY_USER, bookTitel, actor);
        return ResponseEntity.status(HttpStatus.OK).body(new GetFavouriteBookResponseDto(userService.addFavouriteBookToUser(bookTitel, actor), returnMessage));
    }

    @Override
    public ResponseEntity<GetFavouriteBookResponseDto> deleteFavouriteBook(String name) {
        String returnMessage = String.format(InfoMessages.FAV_BOOK_DELETED, name);
        return ResponseEntity.status(HttpStatus.OK).body(new GetFavouriteBookResponseDto(userService.deleteFavouriteBook(name), returnMessage));
    }

    @Override
    public ResponseEntity<GetFavouriteBookResponseDto> getFavouriteBook(String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetFavouriteBookResponseDto(userService.getFavouriteBook(name), null));
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
