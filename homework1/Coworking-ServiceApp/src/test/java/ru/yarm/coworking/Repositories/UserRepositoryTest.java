package ru.yarm.coworking.Repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yarm.coworking.Models.User;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserRepositoryTest {

    @InjectMocks
    private static UserRepository userRepository = new UserRepository();

    @BeforeAll
    public static void setUp() {
        User user = User.builder().id(1).login("1").password("1").build();
        userRepository.addUser(user);
    }

    @AfterAll
    public static void tearDown() {
        userRepository.deleteUser(1);
    }


    @Test
    void getUser() {
        User result = userRepository.getUser(1);
        assertThat(result.getId()).isEqualTo(2);
    }

    @Test
    void updateUser() {
        User result = userRepository.getUser(1);
        result.setLogin("new");
        userRepository.updateUser(1, result);
        assertThat(userRepository.getUser(1).getLogin()).isEqualTo("new");
    }

    @Test
    void addUser() {
        User result = userRepository.getUser(1);
        result.setId(2);
        userRepository.addUser(result);
        assertEquals(5, userRepository.getUsers().size());
    }


    @Test
    void isUserExistFalse() {
        assertFalse(userRepository.isUserExist(5));
    }

    @Test
    void isUserExistTrue() {
        assertTrue(userRepository.isUserExist(1));
    }

    @Test
    void getUserCount() {
        int result = userRepository.getUsers().size();
        assertThat(result).isEqualTo(5);
    }

    @Test
    void getUserIdCounter() {
        assertEquals(7, userRepository.getUserIdCounter());
    }

    @Test
    void getDeleteUser() {
        userRepository.deleteUser(1);
        int result = userRepository.getUsers().size();
        assertThat(result).isEqualTo(4);
    }



}