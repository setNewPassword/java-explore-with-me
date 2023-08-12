package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.NameAlreadyExistException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new NameAlreadyExistException(String
                    .format("Невозможно создать пользователя с именем: %s, такой пользователь уже существует.",
                            userDto.getName()));
        }
        return userMapper.toUserDto(userRepository.save(userMapper.toUserModel(userDto)));

    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        return ids != null ? userMapper.toUserDtoList(userRepository.findAllById(ids))
                : userMapper.toUserDtoList(userRepository.findAll(pageable).toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}