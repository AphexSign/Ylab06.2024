package ru.yarm.coworking.Repositories;

import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.Slot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс отвечающий за хранилище всех бронирований в Системе.
 */
public class BookingRepository {

    /**
     * Хранилище всех записей о бронировании, свободных и занятых в Системе.
     * За каждой площадкой для бронирования может быть закреплен список слотов.
     */
    private static final Map<Place, List<Slot>> bookings = new HashMap<>();


    /**
     * Метод возвращающий коллекцию, содержащую все записи о бронировании
     * @return bookings - коллекция(карта), содержащая всю информацию о слотах
     * за конкретной площадкой
     */
    public Map<Place, List<Slot>> getBookings() {
        return bookings;
    }

    /**
     * Метод добавляющий конкретной площадке слоты с бронированием.
     *
     * @param place объект площадки, за которой закрепляются слоты
     * @param slots список слотов для бронирования
     */
    public void addBooking(Place place, List<Slot> slots) {
        bookings.put(place, slots);
    }


}
