package ru.yarm.coworking.Repositories;

import ru.yarm.coworking.Models.User;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс отвечающий за хранилище всех пользователей в системе, имплентирует интерфейс UserDao.
 * Реализующий стандартные CRUD-методы.
 */
public class UserRepository implements UserDao {

    /**
     * users - хранилище всех пользователей в системе.
     */
    private static final Map<Integer, User> users = new HashMap<>();

    /**
     * userIdCounter - параметр по которому происходит присвоение идентификатора пользователям
     */
    private static Integer userIdCounter = 1;

    /**
     * Метод, предназначенный для возврата пользователя по его
     * идентификатору
     *
     * @param user_id идентификатор пользователя
     * @return User - объект пользователя
     */
    @Override
    public User getUser(Integer user_id) {
        return users.get(user_id);
    }


    /**
     * Метод, предназначенный для обновления пользователя значением нового
     * объекта пользователя
     *
     * @param user_id     идентификатор пользователя
     * @param updatedUser новый объект пользователя, который будет обновлять
     *                    старое значение пользователя
     */
    @Override
    public void updateUser(Integer user_id, User updatedUser) {
        users.put(user_id, updatedUser);
    }


    /**
     * Метод, который заносит пользователя в систему. Вместе
     * с этим происходит автоинкрементация идентификатора пользователя
     *
     * @param user объект пользователя, который будет заноситься в систему
     */
    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
        userIdCounter++;
    }

    /**
     * Метод, который удаляет пользователя из систему по его идентификатору
     *
     * @param user_id объект пользователя, который будет удаляться из системы
     */
    @Override
    public void deleteUser(Integer user_id) {
        users.remove(user_id);
    }

    /**
     * Метод, который возвращает из системы коллекцию(карту) всех пользователей.
     *
     * @return users - коллекция всех пользователей в системе
     */
    public Map<Integer, User> getUsers() {
        return users;
    }

    /**
     * Метод, который проверяет, есть ли в Системе пользователь
     * с искомым идентификатором
     *
     * @return true - если пользователь есть, false - если его нет.
     */

    public boolean isUserExist(Integer user_id) {
        return users.containsKey(user_id);
    }

    /**
     * Метод, который возвращает число, на основании которого присваивается
     * идентификатор пользователю.
     *
     * @return userIdCounter - целое число, на основании которого присваивается
     * идентификатор пользователя.
     */
    public Integer getUserIdCounter() {
        return userIdCounter;
    }

}
