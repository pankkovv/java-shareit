package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

class UserServiceImplTest {
    static UserServiceImpl userService = new UserServiceImpl();
    static UserRepository userRepository = Mockito.mock(UserRepository.class);

    static User user;
    static UserDto userDto;

    @BeforeAll
    static void assistant(){
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);

        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
    }


    //normal behavior
    @Test
    void saveUserTest() {
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        assertEquals(userDto, userService.saveUser(userDto));
    }

    @Test
    void userUpdateTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@user.ru").build());
        User userUpdate = User.builder().id(1L).name("Update").email("Update@user.ru").build();
        UserDto userDtoUpdate = UserDto.builder().id(1L).name("Update").email("Update@user.ru").build();
        Mockito.when(userRepository.findById(anyLong()))
                        .thenReturn(userOpt);
        Mockito.when(userRepository.save(userUpdate))
                .thenReturn(userUpdate);

        assertEquals(userDtoUpdate, userService.updateUser(1L, userDtoUpdate));
    }

    //Reaction to erroneous data
    @Test
    void getByUserIdErrTest() {
        final NotOwnerException exception = assertThrows(NotOwnerException.class, () -> userService.getByUserId(1L));

        assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_USER.label);
    }

    @Test
    void findByIdErrTest() {
        final NotOwnerException exception = assertThrows(NotOwnerException.class, () -> userService.findById(1L));

        assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_USER.label);
    }
}