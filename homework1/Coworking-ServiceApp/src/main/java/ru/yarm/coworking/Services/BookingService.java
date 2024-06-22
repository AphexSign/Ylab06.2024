package ru.yarm.coworking.Services;

import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.Slot;
import ru.yarm.coworking.Models.SlotHistory;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Repositories.BookingRepository;
import ru.yarm.coworking.Repositories.PlaceRepository;
import ru.yarm.coworking.Repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Класс-сервис содержащий в себе бизнес-логику работы с бронированиями в системе
 */
public class BookingService {

    private final BookingRepository bookingRepository = new BookingRepository();
    private final PlaceRepository placeRepository = new PlaceRepository();
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService();

    /**
     * Метод, предназначенный для автоматического добавления слотов времени
     * за определенной площадкой. Предварительный вариант принудительно задает
     * текущую дату, отчет ведется с 10-18 часов. Метод требует модернизации.
     *
     * @param place_id - идентификатор площадки, которую требуется наполнить
     *                 слотами для бронирования
     */
    public void fillPlaceBySlots(Integer place_id) {
        if (placeRepository.isPlaceExist(place_id)) {
            Place place = placeRepository.getPlace(place_id);
            List<Slot> slotList = new ArrayList<>();
            LocalDateTime date = LocalDateTime.now();
            date = date.withHour(10);
            date = date.withMinute(0);
            LocalDateTime date2 = date.plusHours(1);
            for (int i = 0; i < 8; i++) {
                date = date.plusHours(1);
                date2 = date2.plusHours(1);

                if (i == 6) {
                    date = date.plusDays(1);
                    date2 = date.plusDays(1);
                }
                Slot slot = new Slot(date, date2);
                slotList.add(slot);
                place.addSlotCount();
            }
            bookingRepository.addBooking(place, slotList);
            System.out.println("Слоты успешно добавлены к площадке: " + place_id + " " + place.getPlaceType().getTitle());
        } else System.out.println("Добавить слоты невозможно! Площадка не найдена!");
    }

    /**
     * Метод, предназначенный для показа пользователю свободных слотов
     * времени для последующего бронирования конкретной площадки.
     *
     * @param place_id идентификатор площадки, по которой будут отображаться
     *                 только свободные слоты для бронирования
     * @return возвращает true, если список слотов не пустой, false - если пустой
     */
    public boolean showFreeSlotForPlace(Integer place_id) {
        boolean result = false;
        if (placeRepository.isPlaceExist(place_id)) {
            Place place = placeRepository.getPlace(place_id);
            List<Slot> slotList = bookingRepository.getBookings().get(place);
            System.out.println("Свободные окошки у места N" + place_id + " :" + place.getPlaceType().getTitle());
            if (slotList != null) {
                for (Slot slot : slotList) {
                    if (slot.getUser() == null) {
                        System.out.println(slot);
                        result = true;
                    }
                }
            } else {
                System.out.println("\n>>Окошек нет<<\n\n");
            }
        }
        return result;
    }


    /**
     * Метод, предназначенный для показа пользователю занятых слотов
     * времени всеми пользователями на всех площадках.
     *
     * @return n - число занятых слотов
     */
    public int showAllOccupySlotsForAllPlaces() {
        Map<Place, List<Slot>> listMap = bookingRepository.getBookings();
        System.out.println("Занятые окошки: ");
        Set<String> set = new HashSet<>();
        for (Place place : listMap.keySet()) {
            for (Slot slot : listMap.get(place)) {
                if (slot.getUser() != null) {
                    set.add(slot + " |id:" + place.getId() + " " + place.getPlaceType().getTitle());
                }
            }
        }
        for (String el : set) {
            System.out.println(el);
        }
        return set.size();
    }


    /**
     * Метод, предназначенный для показа пользователю занятых слотов
     * времени конкретным пользователем во всех площадках Системы.
     *
     * @param user_id идентификатор пользователя, по которому будут отображаться
     *                занятые слоты для бронирования
     *
     *  @return n - число занятых слотов
     */
    public int showAllOccupySlotsForAllFilterUser(Integer user_id) {
        Map<Place, List<Slot>> listMap = bookingRepository.getBookings();
        System.out.println("Занятые окошки (Фильтрация по пользователю): ");
        Set<String> set = new HashSet<>();
        for (Place place : listMap.keySet()) {
            for (Slot slot : listMap.get(place)) {
                if (slot.getUser() != null && slot.getUser().getId().equals(user_id)) {
                    set.add(slot + " |id:" + place.getId() + " " + place.getPlaceType().getTitle());
                }
            }
        }
        for (String el : set) {
            System.out.println(el);
        }
        return set.size();
    }

    /**
     * Метод, предназначенный для показа пользователю занятых слотов
     * времени пользователями в системе целевой площадки.
     *
     * @param place_id идентификатор площадки, по которой будут отображаться
     *                 занятые пользователями слоты для бронирования
     *
     *   @return n - число занятых слотов
     */
    public int showAllOccupySlotsForAllFilterPlace(Integer place_id) {
        Map<Place, List<Slot>> listMap = bookingRepository.getBookings();
        Place place = placeRepository.getPlace(place_id);
        Set<String> set = new HashSet<>();
        if (place != null) {
            List<Slot> slotList = bookingRepository.getBookings().get(place);
            System.out.println("Занятые окошки (Фильтрация по площадке): ");
            for (Slot slot : slotList) {
                if (slot.getUser() != null) {
                    set.add(slot + " |id:" + place.getId() + " " + place.getPlaceType().getTitle());
                }
            }
            for (String el : set) {
                System.out.println(el);
            }
        }
            return set.size();
    }


    /**
     * Метод, предназначенный для показа пользователю занятых слотов
     * времени пользователями в системе. Использован фильтр даты.
     *
     * @param datetime значение даты, которое передается в метода для осуществления
     *                 фильтрации и вывода на экран
     *
     * @return n - число занятых слотов
     */
    public int showAllOccupySlotsForAllFilterDate(LocalDate datetime) {
        Map<Place, List<Slot>> listMap = bookingRepository.getBookings();
        System.out.println("Занятые окошки, фильтрация по дате: ");
        Set<String> set = new HashSet<>();
        for (Place place : listMap.keySet()) {
            for (Slot slot : listMap.get(place)) {
                if (slot.getUser() != null) {
                    if (slot.getCheckInTime().toLocalDate().equals(datetime)) {
                        set.add(slot + " |id:" + place.getId() + " " + place.getPlaceType().getTitle());
                    }
                }
            }
        }
        for (String el : set) {
            System.out.println(el);
        }
        return set.size();
    }


    /**
     * Метод, предназначенный для бронирования конкретного
     * слота времени у площадки пользователем. Кроме того, в системе отдельно
     * будет сохраняться запись истории бронирования пользователем.
     *
     * @param place_id идентификатор площадки, по которой будет фиксация бронирования слота
     * @param user_id  идентификатор пользователя, который будет занимать слот для бронирования
     * @param slot_id  идентификатор слота, который будет занят пользователем у конкретной площадки
     *
     * @return Возвращает true - если получилось занять слот, false - если нет.
     */
    public boolean tryToOccupyPlace(Integer place_id, Integer user_id, Integer slot_id) {
        User user = userRepository.getUser(user_id);
        boolean result=false;
        Place place = placeRepository.getPlace(place_id);
        List<Slot> slotList = bookingRepository.getBookings().get(place);
        if (slotList != null) {
            for (Slot slot : slotList) {
                if (slot.getId().equals(slot_id) && slot.getUser() == null) {
                    slot.setUser(user);
                    place.subSlotCount();
                    result = true;
                    userService.addUserSlotHistory(user, place, slot);
                }
            }
            bookingRepository.addBooking(place, slotList);
        }
        return result;
    }


    /**
     * Метод, предназначенный для освобождения конкретного
     * слота времени у площадки пользователем. Одновременно с этим, из истории
     * записей бронирования данная запись удаляется.
     *
     * @param user_id        идентификатор пользователя, который освобождает слот времени у площадки
     * @param slotHistory_id идентификатор записи истории бронирования пользователя
     *
     *  @return Возвращает true - если получилось освбодить слот, false - если нет.
     */
    public boolean tryToFreePlace(Integer user_id, Integer slotHistory_id) {
        User user = userRepository.getUser(user_id);
        boolean result=false;
        SlotHistory slotHistory = user.getSlotHistoryMap().get(slotHistory_id);
        if (slotHistory != null) {
            Place placeFromHistory = slotHistory.getPlace();
            Slot slotFromHistory = slotHistory.getSlot();
            List<Slot> slotList = bookingRepository.getBookings().get(placeFromHistory);
            for (Slot slot : slotList) {
                if (slot.getId().equals(slotFromHistory.getId()) && slot.getUser() != null) {
                    slot.setUser(null);
                    result=true;
                    placeFromHistory.addSlotCount();
                }
            }
            bookingRepository.addBooking(placeFromHistory, slotList);
            userService.removeSlotHistoryFromUser(user, slotHistory_id);
        } else System.out.println("Недопустимый слот для удаления!");
        return true;
    }

}
