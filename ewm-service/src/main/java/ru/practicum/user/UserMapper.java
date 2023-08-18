package ru.practicum.user;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User newUserDtoToUser(NewUserDto newUserDto);

    UserDto userToUserDto(User user);

    List<UserDto> toListUserDto(List<User> users);
}
