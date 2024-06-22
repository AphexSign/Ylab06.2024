package ru.yarm.coworking.Services;

import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.PlaceType;
import ru.yarm.coworking.Repositories.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс-сервис содержащий в себе бизнес-логику работы с площадками в системе
 */
public class PlaceService {

    private final PlaceRepository placeRepository = new PlaceRepository();

    /**
     * Метод, предназначенный для возврата объекта на площадку
     * из Системы по ее идентифиактору.
     *
     * @param place_id идентифактор площадки для бронирования
     * @return Place возвращает объект площадки для бронирования, если он есть,
     * и null, если он не будет найден в системе
     */
    public Place getPlaceById(Integer place_id) {
        if (placeRepository.isPlaceExist(place_id)) {
            return placeRepository.getPlace(place_id);
        } else {
            System.out.println("Место, id:" + place_id + " не существует в БД!");
            return null;
        }
    }

    /**
     * Метод, предназначенный для удаления площадки
     * из Системы по ее идентификатору. Удаление не будет происходить,
     * если площадка не будет найдена в Системе.
     *
     * @param place_id идентификатор площадки для бронирования, которую требуется
     *                 удалить
     */
    public void deletePlaceById(Integer place_id) {
        if (placeRepository.isPlaceExist(place_id)) {
            placeRepository.deletePlace(place_id);
            System.out.println("Площадка " + place_id + " - успешной удалена!");
        } else System.out.println("Такой площадки не существует!");
    }

    /**
     * Метод, предназначенный для добавления площадки в систему. Для успешного создания требуется выбрать конкретный тип
     *
     * @param placeType значение типа помещения,
     *                  при его создании, аргумент числовой:
     *                     1. Рабочее место
     *                     2. Конференц-зал
     */
    public void registerPlace(Integer placeType) {
        Place place = Place.builder().id(placeRepository.getPlaceIdCounter()).build();
        switch (placeType) {
            case 1:
                place.setPlaceType(PlaceType.WORKSPACE);
                placeRepository.addPlace(place);
                System.out.println("Площадка " + place.getId() + " (" + place.getPlaceType().getTitle() + ") успешно добавлена!");
                break;
            case 2:
                place.setPlaceType(PlaceType.CONFERENCE_HALL);
                placeRepository.addPlace(place);
                System.out.println("Площадка " + place.getId() + " (" + place.getPlaceType().getTitle() + ") успешно добавлена!");
                break;
            default:
                System.out.println("Прошу ввести правильный тип!");
                break;
        }
    }


    /**
     * Метод, предназначенный для изменения типа желаемой площадки. В метод требуется
     * передать идентификатор изменяемого площадки и желаемый новый тип для площадки.
     *
     * @param place_id - идентификатор площадки, у которой требуется изменить тип
     * @param placeType значение типа помещения,
     *                  при обновлении его значения:
     *                     1. Рабочее место
     *                     2. Конференц-зал
     */
    public void updatePlace(Integer place_id, Integer placeType) {
        if (getPlaceById(place_id) != null) {
            Place oldPlace = getPlaceById(place_id);
            switch (placeType) {
                case 1:
                    oldPlace.setPlaceType(PlaceType.WORKSPACE);
                    placeRepository.updatePlace(place_id, oldPlace);
                    System.out.println("Место " + oldPlace.getId() + " - обновило свой тип до: " + oldPlace.getPlaceType().getTitle());
                    break;
                case 2:
                    oldPlace.setPlaceType(PlaceType.CONFERENCE_HALL);
                    placeRepository.updatePlace(place_id, oldPlace);
                    System.out.println("Place " + oldPlace.getId() + " - обновило свой тип до: " + oldPlace.getPlaceType().getTitle());
                    break;
                default:
                    System.out.println("Выбранный тип неправильный!");
                    break;
            }
        }
    }

    /**
     * Вспомогательный метод для возврата списка всех площадок из системы.
     *
     * @return List<Place> возвращает список всех доступных площадок в системе
     */
    private List<Place> getAllPlaces() {
        return new ArrayList<>(placeRepository.getPlaces().values());
    }

    /**
     * Метод для отображения всех площадок в системе.
     */
    public int showAllPlaces() {
        for (Place place : getAllPlaces()) {
            System.out.println(place.toString());
        }
        return getAllPlaces().size();
    }


}
