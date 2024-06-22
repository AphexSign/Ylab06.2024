package ru.yarm.coworking.Models;

import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий собой запись о бронировании
 */
@Data
@Builder
public class SlotHistory {

    private Place place;
    private Slot slot;
    /**
     * Параметр-счетчик на основании которого присвивается идентификатор записи истории бронирования для пользователя
     */
    private static Integer slotHistoryIdCounter = 0;


    /**
     * Метод автоинкрементирует счетчик идентификатора записи истории бронирования для пользователя
     */
    public static void addSlotHistoryCount() {
        slotHistoryIdCounter++;
    }

    /**
     * Метод возвращает значение счетчика идентификатора записи истории бронирования для пользователя
     */
    public static Integer getSlotHistoryCount() {
        return slotHistoryIdCounter;
    }

}
