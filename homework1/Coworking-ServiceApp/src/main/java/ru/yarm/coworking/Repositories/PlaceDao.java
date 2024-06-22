package ru.yarm.coworking.Repositories;

import ru.yarm.coworking.Models.Place;


/**
 * Интерфейс, содержащий прототипы CRUD-методов для работы с площадками
 */
public interface PlaceDao {

    Place getPlace(Integer place_id);

    void updatePlace(Integer place_id, Place updatedPlace);

    void addPlace(Place place);

    void deletePlace(Integer place_id);


}
