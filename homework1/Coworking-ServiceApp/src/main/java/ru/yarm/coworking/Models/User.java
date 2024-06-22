package ru.yarm.coworking.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс представляющий собой пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    /**
     * Параметр идентификатор пользователя
     */
    private Integer id;
    /**
     * Параметр логин пользователя
     */
    private String login;
    /**
     * Параметр пароль пользователя
     */
    private String password;
    /**
     * Параметр роль пользователя в Системе.
     * В системе используется роль КЛИЕНТ - после авторизации
     * или АДМИН - аккаунт с особым статусом и правами на управления сущностями
     */
    private String role;

    /**
     * Хранилище записей бронирования за пользователем
     */
    private Map<Integer, SlotHistory> SlotHistoryMap=new HashMap<>();


    public User(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    /**
     * Метод, который позволяет добавить запись о бронировании
     * @param slotHistory_id идентификатор записи о бронировании
     * @param slotHistory запись о бронировании, содержащая в себе данные о площадке и слот
     */
    public void addSlotHistory(Integer slotHistory_id, SlotHistory slotHistory) {
        this.SlotHistoryMap.put(slotHistory_id, slotHistory);
    }

    /**
     * Метод, удаляющий запись о бронировании у пользователя
     */
    public void removeSlotHistory(Integer slotHistory_id) {
        this.SlotHistoryMap.remove(slotHistory_id);
    }


}
