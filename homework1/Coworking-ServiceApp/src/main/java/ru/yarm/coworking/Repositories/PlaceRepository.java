package ru.yarm.coworking.Repositories;

import ru.yarm.coworking.Models.Place;

import java.util.HashMap;
import java.util.Map;


/**
 * Класс отвечающий за хранилище всех площадок в системе, имплентирует интерфейс PlaceDao.
 * Реализующий стандартные CRUD-методы.
 */
public class PlaceRepository implements PlaceDao {
    /**
     * Хранилище всех записей о коммерческих площадках в Системе, которые можно бронировать.
     */
    private static final Map<Integer, Place> places = new HashMap<>();
    /**
     * placeIdCounter - параметр по которому происходит присвоение идентификатора площадкам
     */
    private static Integer placeIdCounter = 1;


    /**
     * Метод, предназначенный для возврата площадки по ее
     * идентификатору
     *
     * @param place_id идентификатор площадки
     * @return Place - объект площадки
     */
    @Override
    public Place getPlace(Integer place_id) {
        return places.get(place_id);
    }


    /**
     * Метод, предназначенный для обновления площадки значением нового
     * объекта площадки
     *
     * @param place_id     идентификатор площадки
     * @param updatedPlace новый объект площадки, который будет обновлять
     *                     старое значение площадки
     */
    @Override
    public void updatePlace(Integer place_id, Place updatedPlace) {
        places.put(place_id, updatedPlace);
    }


    /**
     * Метод, который заносит площадку в систему. Вместе
     * с этим происходит автоинкрементация идентификатора площадки
     *
     * @param place объект площадки, который будет заноситься в систему
     */
    @Override
    public void addPlace(Place place) {
        places.put(place.getId(), place);
        placeIdCounter++;
    }


    /**
     * Метод, который удаляет площадку из системы по ее идентификатору
     *
     * @param place_id объект площадки, которая будет удаляться из системы
     */
    @Override
    public void deletePlace(Integer place_id) {
        places.remove(place_id);
    }


    /**
     * Метод, который возвращает из системы коллекцию(карту) всех площадок.
     *
     * @return places - коллекция(карта) всех площадок в системе
     */
    public Map<Integer, Place> getPlaces() {
        return places;
    }

    /**
     * Метод, который проверяет, есть ли в Системе площадка
     * с искомым идентификатором
     *
     * @return true - если площадка есть, false - если его нет.
     */
    public boolean isPlaceExist(Integer id) {
        return places.containsKey(id);
    }


    /**
     * Метод, который возвращает число, на основании которого присваивается
     * идентификатор площадки.
     *
     * @return getPlaceIdCounter - целое число, на основании которого присваивается
     * идентификатор площадки.
     */
    public Integer getPlaceIdCounter() {
        return placeIdCounter;
    }


}
