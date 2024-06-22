package ru.yarm.coworking.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс представляющий собой слот, с временем начала и конца аренды
 * конкретной площадки, которая может быть закреплена за определенным пользователем
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slot {

    /**
     * Идентификатор слота
     */
    private Integer id;
    private User user;
    /**
     * Время начала аренды площадки
     */
    private LocalDateTime checkInTime;
    /**
     * Время конца аренды площадки
     */
    private LocalDateTime checkOutTime;
    /**
     * Счетчик на основании которого присвивается идентификационный номер слота
     */
    private static Integer slotIdCounter = 1;

    public Slot(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        this.id = slotIdCounter++;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm");
        String userName;
        userName = user == null ? " Свободно" : " Клиент id:" + user.getId() + " " + user.getLogin();
        return id + ":" + userName +
                ", Начало: (" + checkInTime.format(formatter) +
                ", Конец:" + checkOutTime.format(formatter)+")";
    }
}
