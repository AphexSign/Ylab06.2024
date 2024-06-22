package ru.yarm.coworking.Repositories;

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
import ru.yarm.coworking.Models.Slot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingRepositoryTest {

    @InjectMocks
    private static BookingRepository bookingRepository = new BookingRepository();

    @Mock
    private static PlaceRepository placeRepository = new PlaceRepository();

    @BeforeAll
    static void setUp() {
        Place place = Place.builder().id(1).placeType(PlaceType.WORKSPACE).slotsCount(0).build();
        placeRepository.addPlace(place);
    }

    @Test
    void getBookingsSizeNull() {
        int result = bookingRepository.getBookings().size();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void addBooking() {
        Place myPlace = placeRepository.getPlace(1);
        Slot slot = Slot.builder().checkOutTime(LocalDateTime.now()).checkInTime(LocalDateTime.now()).user(null).id(1).build();
        List<Slot> slotList = new ArrayList<>();
        slotList.add(slot);
        bookingRepository.addBooking(myPlace, slotList);
        int result = bookingRepository.getBookings().size();
        assertThat(result).isEqualTo(1);
    }
}