package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMap;
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
        return UserMap.mapToUserDto(userRepository.findAll());
    }

    @Override
    public UserDto getByUserId(Long userId) {
        return UserMap.mapToUserDto(userRepository.getById(userId));
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
}
