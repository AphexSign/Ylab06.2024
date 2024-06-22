package ru.yarm.coworking.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий собой площадку, которую можно будет забронировать.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    /**
     * Параметр идентификатор площадки
     */
    private Integer id;
    /**
     * Параметр типа площадки, представляет собой enum
     * Площадки бывают: рабочее место, конференц-зал
     */
    private PlaceType placeType;

    /**
     * Параметр хранящий число свободных слотов за площадкой
     */
    private int slotsCount = 0;

    @Override
    public String toString() {
        return id + ". Тип: " + placeType.getTitle() + " N" + id + " слотов(" + slotsCount + " слотов)";
    }

    /**
     * Метод инкрементирующий число слотов, например, когда пользователь
     * освобождает площадку от себя.
     */
    public void addSlotCount() {
        this.slotsCount++;
    }

    /**
     * Метод для декрементации числа слотов, например, когда пользователь
     * занимает площадку собой.
     */
    public void subSlotCount() {
        this.slotsCount--;
    }


}
