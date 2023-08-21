package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(NewUserDto newUserDto) {
        User user = userMapper.newUserDtoToUser(newUserDto);
        user = userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        List<User> result = new ArrayList<>();
        int page = from > 0 ? from / size : 0;
        Pageable userPageable = PageRequest.of(page, size);
        if (ids == null || ids.isEmpty()) {
            result.addAll(userRepository.findAll(userPageable).getContent());
        } else {
            result.addAll(userRepository.findByIdIn(ids, userPageable));
        }
        return userMapper.toListUserDto(result);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        userRepository.deleteById(userId);
    }
}
