package ru.yarm.coworking.Services;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.PlaceType;
import ru.yarm.coworking.Repositories.PlaceRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlaceServiceTest {

    @Mock
    private static PlaceRepository placeRepository = new PlaceRepository();


    @InjectMocks
    private static PlaceService placeService = new PlaceService();


    @BeforeAll
    static void setUp() {
        placeService.registerPlace(1);
        placeService.registerPlace(2);
    }

    @AfterAll
    static void endWork() {
        placeService.deletePlaceById(1);
        placeService.deletePlaceById(2);
    }


    @Test
    void getPlaceById() {
        Place place = placeService.getPlaceById(1);
        when(placeRepository.getPlace(1)).thenReturn(place);
        assertThat(place).isEqualTo(placeService.getPlaceById(1));
    }



    @Test
    void updatePlace() {
        Place place = placeService.getPlaceById(1);
        when(placeRepository.getPlace(1)).thenReturn(place);
        placeService.updatePlace(1,2);
        Place result=placeService.getPlaceById(1);
        assertThat(result).isEqualTo(place);
    }



    @Test
    void getPlaceByIdNotFound() {
        Place mockPlace = new Place(1, PlaceType.WORKSPACE, 0);
        placeService.registerPlace(1);
        when(placeRepository.getPlace(1)).thenReturn(mockPlace);
        Place retrievedPlace = placeService.getPlaceById(20);
        assertNull(retrievedPlace);
    }

    @Test
    void deletePlaceById() {
        Place mockPlace = new Place(1, PlaceType.WORKSPACE, 0);
        placeService.registerPlace(1);
        when(placeRepository.getPlace(1)).thenReturn(mockPlace);
        Place retrievedPlace = placeService.getPlaceById(1);
        assertThat(retrievedPlace.getId()).isEqualTo(1);
        placeService.deletePlaceById(1);
        Place retrievedPlace2 = placeService.getPlaceById(1);
        assertNull(retrievedPlace2);

    }

    @Test
    void registerPlaceWrong() {
        PlaceService placeService = mock(PlaceService.class);
        placeService.registerPlace(0);
        Place place = placeService.getPlaceById(1);
        assertNull(place);
    }

    @Test
    void registerPlaceTrueTwo() {
        placeService.registerPlace(2);
        Place mockPlace = placeService.getPlaceById(1);
        when(placeRepository.getPlace(1)).thenReturn(mockPlace);
    }


    @Test
    void updatePlaceWrong() {
        placeService.registerPlace(2);
        Place mockPlace = placeService.getPlaceById(1);
        placeService.updatePlace(1, 3);
        when(placeRepository.getPlace(1)).thenReturn(mockPlace).getMock();
    }

    @Test
    void showAllPlaces() {
       int result= placeService.showAllPlaces();
       assertThat(result).isEqualTo(4);
    }

}