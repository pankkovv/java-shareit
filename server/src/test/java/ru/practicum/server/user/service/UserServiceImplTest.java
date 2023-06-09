package ru.practicum.server.user.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.server.exception.NotOwnerException;
import ru.practicum.server.messages.ExceptionMessages;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class UserServiceImplTest {
    static UserServiceImpl userService = new UserServiceImpl();
    static UserRepository userRepository = Mockito.mock(UserRepository.class);

    static User user;
    static List<User> listUser;
    static UserDto userDto;

    @BeforeAll
    static void assistant() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);

        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        listUser = List.of(User.builder().id(1L).name("User").email("user@user.ru").build());
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

    @Test
    void findByIdTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@user.ru").build());
        Mockito.when(userRepository.findById(any()))
                .thenReturn(userOpt);

        assertEquals(user, userService.findById(1L));
    }

    @Test
    void getByUserIdTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@user.ru").build());
        Mockito.when(userRepository.findById(any()))
                .thenReturn(userOpt);

        assertEquals(userDto, userService.getByUserId(1L));
    }

    @Test
    void getAllUsersTest() {
        Mockito.when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(listUser);

        assertEquals(userDto, userService.getAllUsers().get(0));
    }

    //Reaction to erroneous data
    @Test
    void findByIdErrTest() {
        final NotOwnerException exception = assertThrows(NotOwnerException.class, () -> userService.findById(10L));

        assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_USER.label);
    }

    @Test
    void getByUserIdErrTest() {
        Mockito.when(userRepository.findById(anyLong()))
                .thenThrow(NotOwnerException.class);
        final NotOwnerException exception = assertThrows(NotOwnerException.class, () -> userService.getByUserId(10L));

        assertNull(exception.getMessage());
    }

}