package ru.practicum.server.user.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.NotOwnerException;
import ru.practicum.server.messages.ExceptionMessages;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.mapper.UserMap;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@NoArgsConstructor
public
class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        return UserMap.mapToUserDto(userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotOwnerException(ExceptionMessages.NOT_FOUND_USER.label));
    }
}
