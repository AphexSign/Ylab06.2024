package ru.yarm.coworking.Repositories;

import ru.yarm.coworking.Models.User;


/**
 * Интерфейс, содержащий прототипы CRUD-методов для работы с пользователями
 */
public interface UserDao {

    User getUser(Integer id);

    void updateUser(Integer id, User updatedUser);

    void addUser(User user);

    void deleteUser(Integer id);


}
