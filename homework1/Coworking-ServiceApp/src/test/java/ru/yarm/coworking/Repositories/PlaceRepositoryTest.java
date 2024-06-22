package ru.yarm.coworking.Repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.PlaceType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlaceRepositoryTest {

    @InjectMocks
    private static PlaceRepository placeRepository = new PlaceRepository();

    @BeforeAll
    static void setUp() {
        Place place = Place.builder().id(1).placeType(PlaceType.WORKSPACE).slotsCount(0).build();
        placeRepository.addPlace(place);
    }

    @Test
    void getPlace() {
        Place result = placeRepository.getPlace(1);
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void updatePlace() {
        Place result = placeRepository.getPlace(1);
        result.setPlaceType(PlaceType.CONFERENCE_HALL);
        placeRepository.updatePlace(1, result);
        Place retieved = placeRepository.getPlace(1);
        assertThat(retieved.getPlaceType()).isEqualTo(PlaceType.CONFERENCE_HALL);
    }

    @Test
    void addPlace() {
        placeRepository.addPlace(Place.builder().id(2).placeType(PlaceType.WORKSPACE).build());
        Place result = placeRepository.getPlace(2);
        assertThat(result.getPlaceType()).isEqualTo(PlaceType.WORKSPACE);
    }

    @Test
    void deletePlace() {
        placeRepository.deletePlace(2);
        Place result = placeRepository.getPlace(2);
        assertNull(result);

    }

    @Test
    void getPlaces() {
        int result = placeRepository.getPlaces().size();
        assertThat(result).isEqualTo(1);
    }

    @Test
    void isPlaceExist() {
        boolean result = placeRepository.isPlaceExist(1);
        assertTrue(result);
    }

    @Test
    void getPlaceIdCounter() {
        int result = placeRepository.getPlaceIdCounter();
        assertThat(result).isEqualTo(3);
    }
}