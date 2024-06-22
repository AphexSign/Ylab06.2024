package ru.yarm.coworking.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.Slot;
import ru.yarm.coworking.Models.SlotHistory;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void getUserById() {
        User mockUser = new User("mylogin5", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User retrievedUser = userService.getUserById(1);
        assertThat(retrievedUser.getId()).isEqualTo(1);
    }

    @Test
    void getUserByIdNotPresent() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(2)).thenReturn(mockUser);
        User retrievedUser = userService.getUserById(2);
        assertNull(retrievedUser);
    }

    @Test
    void updateUserLogin() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        String newLogin = "newlogin";
        when(userRepository.getUser(1)).thenReturn(mockUser);
        userService.updateUserLogin(1, newLogin);
        User updatedUser = userService.getUserById(1);
        assertThat(updatedUser.getLogin()).isEqualTo(newLogin);
    }

    @Test
    void updateUserLoginBadFormat() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        String newLogin = " ";
        when(userRepository.getUser(1)).thenReturn(mockUser);
        userService.updateUserLogin(1, newLogin);
        User updatedUser = userService.getUserById(1);
        assertThat(updatedUser.getLogin()).isEqualTo("mylogin");
    }

    @Test
    void updateUserPassword() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        String newPassword = "newpassword";
        when(userRepository.getUser(1)).thenReturn(mockUser);
        userService.updateUserPassword(1, newPassword);
        User updatedUser = userService.getUserById(1);
        assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void updateUserPasswordBadFormat() {
        User mockUser = new User("mylogin", "newpassword", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        String newPassword5=" ";
        when(userRepository.getUser(1)).thenReturn(mockUser);
        userService.updateUserPassword(1, newPassword5);
        User updatedUser = userService.getUserById(1);
        assertThat(updatedUser.getPassword()).isEqualTo("newpassword");
    }



    @Test
    void registerPerform() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User retrievedUser = userService.getUserById(1);
        assertThat(retrievedUser.getId()).isEqualTo(1);
        assertThat(retrievedUser.getLogin()).isEqualTo("mylogin");
    }

    @Test
    void performAuthenticate() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User authUser = userService.performAuthenticate(mockUser);
        assertThat(authUser.getLogin()).isEqualTo(mockUser.getLogin());
    }

    @Test
    void performAuthenticateFalse() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        User testUser = new User("mylogin", "123", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User result=userService.performAuthenticate(testUser);
        assertNull(result);
    }


    @Test
    void deleteUserById() {
        User mockUser = new User("mylogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        userService.deleteUserById(1);
        User deletedUser = userService.getUserById(1);
        assertThat(deletedUser == null).isTrue();
    }

    @Test
    void getUserByLogin() {
        User mockUser = new User("myLogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User test = userService.getUserByLogin("myLogin");
        assertThat(test.getLogin().equals("myLogin")).isTrue();
    }

    @Test
    void getUserByLoginNotPresent() {
        User mockUser = new User("myLogin", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        User test = userService.getUserByLogin("myLoginNot");
        assertNull(test);
    }


    @Test
    void isUserLoginValid() {
        assertFalse(userService.isUserLoginValid(""));
    }

    @Test
    void isUserLoginNotValid() {
        assertTrue(userService.isUserLoginValid("qqq"));
    }

    @Test
    void isUserPasswordValid() {
        assertFalse(userService.isUserPasswordValid(""));
    }

    @Test
    void isUserPasswordNotValidNull() {
        assertFalse(userService.isUserPasswordValid(null));
    }

    @Test
    void isUserPasswordNotValid() {
        assertTrue(userService.isUserPasswordValid("qqq"));
    }

    @Test
    void addUserSlotHistory() {
        User mockUser = new User("mylogin5", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        SlotHistory slotHistory=SlotHistory.builder().place(new Place()).slot(new Slot()).build();
        userService.addUserSlotHistory(mockUser,new Place(),new Slot());
        assertEquals(1, mockUser.getSlotHistoryMap().size());
    }

    @Test
    void removeSlotHistoryFromUser() {
        User mockUser = new User("mylogin5", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        SlotHistory slotHistory=SlotHistory.builder().place(new Place()).slot(new Slot()).build();
        userService.addUserSlotHistory(mockUser,new Place(),new Slot());
        userService.removeSlotHistoryFromUser(mockUser,1);
        assertTrue(mockUser.getSlotHistoryMap().isEmpty());
    }

    @Test
    void removeOneSlotHistoryFromUser() {
        User mockUser = new User("mylogin5", "pass", "ROLE_CLIENT");
        userService.registerPerform(mockUser);
        when(userRepository.getUser(1)).thenReturn(mockUser);
        SlotHistory slotHistory=SlotHistory.builder().place(new Place()).slot(new Slot()).build();
        userService.addUserSlotHistory(mockUser,new Place(),new Slot());
        userService.addUserSlotHistory(mockUser,new Place(),new Slot());
        userService.removeSlotHistoryFromUser(mockUser,2);
        assertEquals(1, mockUser.getSlotHistoryMap().size());
    }


}