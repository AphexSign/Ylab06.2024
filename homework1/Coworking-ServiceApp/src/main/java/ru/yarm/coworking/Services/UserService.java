package ru.yarm.coworking.Services;

import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.Slot;
import ru.yarm.coworking.Models.SlotHistory;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс-сервис содержащий в себе бизнес-логику работы с пользователями в системе.
 */
public class UserService {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Метод, предназначенный для возврата класса "Пользователь"
     * по его идентификатору.
     *
     * @param user_id идентификатор пользователя
     * @return User - пользователя
     */
    public User getUserById(Integer user_id) {
        if (userRepository.isUserExist(user_id)) {
            return userRepository.getUser(user_id);
        } else {
            System.err.println("User, id:" + user_id + " is not exist!");
            return null;
        }
    }

    /**
     * Метод, предназначенный для изменения у пользователя его логина.
     * В метод требуется передать идентифактор пользователя и
     * строку с новым значением логина. Логин должен быть не пустым и уникальным, т.е
     * не должен встречаться в базе данных.
     *
     * @param user_id идентификатор пользователя
     * @param login   строчное значение нового логина
     */
    public void updateUserLogin(Integer user_id, String login) {
        if (getUserById(user_id) != null) {
            User oldUser = getUserById(user_id);
            if ((getUserByLogin(login) == null) && isUserLoginValid(login)) {
                oldUser.setLogin(login);
                userRepository.updateUser(user_id, oldUser);
            } else System.out.println("Неверное значение для логина!");
        }
    }

    /**
     * Метод, предназначенный для изменения у пользователя его пароля.
     * В метод требуется передать идентифактор пользователя и
     * строку с новым значением пароля. Пароль должен быть не пустым.
     *
     * @param user_id  идентификатор пользователя
     * @param password строчное значение нового пароля
     */
    public void updateUserPassword(Integer user_id, String password) {
        if (getUserById(user_id) != null) {
            User oldUser = getUserById(user_id);
            if (isUserPasswordValid(password)) {
                oldUser.setPassword(password);
                userRepository.updateUser(user_id, oldUser);
            } else System.err.println("Неверное значение для пароля!");
        }
    }

    /**
     * Метод, предназначенный для регистрации пользователя в системе.
     * Пользователь должен ввести в систему валидные значения логина и пароля.
     * Логин не должен быть пустым и уникальным. Пароль должен быть не пустым.
     * По-умолчанию каждому пользователю выдается роль "Клиента", которая дает
     * права на выбор бронирования площадки и отказ от нее. Роль админа доступна
     * админу, админ внедряется в систему посредством "инъекции".
     *
     * @param checkedUser объект пользователя, подлежащий проверке
     */
    public void registerPerform(User checkedUser) {
        if (getUserByLogin(checkedUser.getLogin()) == null) {
            if (isUserLoginValid(checkedUser.getLogin())) {
                checkedUser.setId(userRepository.getUserIdCounter());
                if (StringUtils.isBlank(checkedUser.getRole())) {
                    checkedUser.setRole("ROLE_CLIENT");
                    checkedUser.setSlotHistoryMap(new HashMap<>());
                }
                userRepository.addUser(checkedUser);
                System.out.println("Пользователь зарегистрирован, логин: " + checkedUser.getLogin());
            } else System.out.println("\u001B[31m" + "Значение логина неверно!" + "\u001B[0m");
        } else System.out.println("\u001B[31m" + "Пользователь с таким логином уже есть в системе!" + "\u001B[0m");
    }

    /**
     * Метод, предназначенный для аутентификации пользователя
     * в систему. Требуется введение валидных логина и пароля.
     *
     * @param checkedUser объект пользователя, подлежащий проверке
     */
    public User performAuthenticate(User checkedUser) {
        User findUser = getUserByLogin(checkedUser.getLogin());
        User result = null;
        if (findUser != null) {
            if (findUser.getPassword().equals(checkedUser.getPassword())) {
                result = findUser;
            }
        }
        return result;
    }

    /**
     * Метод, предназначенный для удаления пользователя
     * из системы по его идентификатору
     *
     * @param user_id идентификатор пользователя
     */
    public void deleteUserById(Integer user_id) {
        if (userRepository.isUserExist(user_id)) {
            userRepository.deleteUser(user_id);
        } else System.err.println("Пользователя с таким идентифактором нет в системе!");
    }


    /**
     * Метод, предназначенный для возврата списка
     * пользователей из системы;
     *
     * @return List<User> - список всех пользователей
     */
    private List<User> getAllUsers() {
        return new ArrayList<>(userRepository.getUsers().values());
    }

    /**
     * Метод, предназначенный для поиска и возврата пользователя
     * по его логину. В системе не допускается наличие пользователей
     * с одинаковым логином.
     *
     * @param login - строковое значение логина, по которому будет произведен поиск пользователя
     * @return User - объект пользователя, по его логину
     */
    public User getUserByLogin(String login) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Метод, предназначенный для отображения на экран всех пользователей
     * в системе.
     */
    public void showAllUsers() {
        for (User user : getAllUsers()) {
            System.out.println(user.toString());
        }
    }


    /**
     * Метод, предназначенный для проверки валидности логина
     * на соблюдение правила не быть пустым.
     *
     * @param login - строковое значение логина, который подлежит проверке
     * @return true, если логин - валидный, в противном случае - false
     */
    public boolean isUserLoginValid(String login) {
        return !StringUtils.isBlank(login);
    }

    /**
     * Метод, предназначенный для проверки валидности пароля
     * на соблюдение правила не быть пустым.
     *
     * @param password - строковое значение пароля, который подлежит проверке
     * @return true, если пароль - валидный, в противном случае - false
     */
    public boolean isUserPasswordValid(String password) {
        return !StringUtils.isBlank(password);
    }

    /**
     * Метод, предназначенный для добавления к пользователю
     * истории его бронирования площадок в системе.
     *
     * @param user объект пользователя, в отношении которого составляется запись истории бронирования
     * @param place объект площадки для аренды, которая будет присутствовать в истории записи бронирования
     * @param slot объект слота-времени, который будет занесен в историю записи бронирования
     */
    public void addUserSlotHistory(User user, Place place, Slot slot) {
        SlotHistory.addSlotHistoryCount();
        SlotHistory slotHistory = SlotHistory.builder().slot(slot).place(place).build();
        user.addSlotHistory(SlotHistory.getSlotHistoryCount(), slotHistory);
        userRepository.updateUser(user.getId(), user);
    }

    /**
     * Метод, предназначенный для удаления истории бронирования пользователю
     * в системе.
     *
     * @param user объект пользователя, с которого будет снята запись истории о бронировании
     * @param slotHistory_id идентификатор записи о бронировании
     */
    public void removeSlotHistoryFromUser(User user, Integer slotHistory_id) {
        user.removeSlotHistory(slotHistory_id);
        userRepository.updateUser(user.getId(), user);
    }

    /**
     * Метод, предназначенный для отображения всех действующих слотов, которые пользователь
     * забронировал в системе. В метод требуется передать ссылку на целевого пользователя.
     *
     * @param user объект пользователя, по которому мы будем получать в консоли
     *             все его доступные записи о бронировании
     */
    public void showAllSlotByUser(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
        Map<Integer, SlotHistory> slotHistoryMap = user.getSlotHistoryMap();
        for (Map.Entry<Integer, SlotHistory> entry : slotHistoryMap.entrySet()) {
            System.out.println(entry.getKey()
                    + ". Место: "
                    + entry.getValue().getPlace().getPlaceType().getTitle() + " N"
                    + entry.getValue().getPlace().getId() + " ("
                    + entry.getValue().getSlot().getCheckInTime().format(formatter) + " -- "
                    + entry.getValue().getSlot().getCheckOutTime().format(formatter) + ")"
            );
        }

    }

}




