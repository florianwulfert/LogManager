package project.userFeaturePortal.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.userFeaturePortal.common.dto.user.UserDto;
import project.userFeaturePortal.model.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
  @Mapping(target = "favouriteBookTitel", source = "favouriteBook.titel")
  UserDto userToUserDto(User user);

  List<UserDto> usersToUserDtos(List<User> users);
}
