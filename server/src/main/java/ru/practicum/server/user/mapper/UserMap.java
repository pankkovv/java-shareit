package ru.practicum.server.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMap {
    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> mapToUserDto(List<User> listUser) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : listUser) {
            userDtoList.add(mapToUserDto(user));
        }
        return userDtoList;
    }
}
