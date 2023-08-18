package ru.practicum.user;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto newUserDto);
    List<UserDto> getUsers(List<Integer> ids, int from, int size);
    void deleteUser(Integer userId);
}
