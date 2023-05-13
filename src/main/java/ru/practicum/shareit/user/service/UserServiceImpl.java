package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMap;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMap.mapToUserDto(userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Override
    public UserDto getByUserId(Long userId) {
        return UserMap.mapToUserDto(userRepository.findById(userId).orElseThrow(() -> new NotOwnerException(ExceptionMessages.NOT_FOUND_USER.label)));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return UserMap.mapToUserDto(userRepository.save(UserMap.mapToUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        UserDto userDtoOld = getByUserId(userId);
        if (userDto.getName() != null) {
            userDtoOld.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userDtoOld.setEmail(userDto.getEmail());
        }
        return saveUser(userDtoOld);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotOwnerException(ExceptionMessages.NOT_FOUND_USER.label));
    }
}
