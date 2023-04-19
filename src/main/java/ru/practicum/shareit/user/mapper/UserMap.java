package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMap {
    public UserDto transferToObj(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDto> transferToObj(List<User> listUser) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : listUser) {
            userDtoList.add(UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build());
        }
        return userDtoList;
    }

    public User transferFromObj(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

}
