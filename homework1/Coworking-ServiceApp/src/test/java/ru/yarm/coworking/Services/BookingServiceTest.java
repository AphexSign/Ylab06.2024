package ru.yarm.coworking.Services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.Slot;
import ru.yarm.coworking.Models.SlotHistory;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Repositories.BookingRepository;
import ru.yarm.coworking.Repositories.PlaceRepository;
import ru.yarm.coworking.Repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository = new BookingRepository();

    @Mock
    private UserRepository userRepository = new UserRepository();

    @Mock
    private PlaceRepository placeRepository = new PlaceRepository();

    @InjectMocks
    private BookingService bookingService = new BookingService();

    @InjectMocks
    private static PlaceService placeService = new PlaceService();

    @InjectMocks
    private static UserService userService = new UserService();

    @BeforeAll
    static void setUp() {
        placeService.registerPlace(1);
        userService.registerPerform(new User("A","B","ROLE_ADMIN"));
    }

    @Test
    void fillPlaceBySlots() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        Place place = placeService.getPlaceById(1);
        int result = place.getSlotsCount();
        assertEquals(56, result);
    }

    @Test
    void showAllSlotsForPlace() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        Place place = placeRepository.getPlace(1);
        boolean result = bookingService.showFreeSlotForPlace(1);
        assertFalse(result);
    }

    @Test
    void showFreeSlotForPlace() {


    }

    @Test
    void showAllOccupySlotsForAllPlaces() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        Place place = placeRepository.getPlace(1);
        int result = bookingService.showAllOccupySlotsForAllPlaces();
        assertEquals(result, 0);
    }

    @Test
    void showAllOccupySlotsForAllFilterUser() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        Place place = placeRepository.getPlace(1);
        int result = bookingService.showAllOccupySlotsForAllFilterUser(1);
        assertEquals(result, 0);
    }

    @Test
    void showAllOccupySlotsForAllFilterPlace() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        Place place = placeRepository.getPlace(1);
        int result = bookingService.showAllOccupySlotsForAllFilterPlace(1);
        assertEquals(result, 0);
    }

    @Test
    void showAllOccupySlotsForAllFilterDate() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        int result = bookingService.showAllOccupySlotsForAllFilterDate(LocalDate.now());
        assertEquals(result, 0);

    }

    @Test
    void tryToOccupyPlace() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        User user = new User("A", "B", "ROLE_ADMIN");
        userService.registerPerform(user);
        boolean result = bookingService.tryToOccupyPlace(1, user.getId(), 1);
        assertFalse(result);
    }

    @Test
    void tryToFreePlace() {
        placeService.registerPlace(1);
        bookingService.fillPlaceBySlots(1);
        User user = new User("A", "B", "ROLE_ADMIN");
        user.setId(1);
        boolean result = bookingService.tryToFreePlace(1, 1);
        assertTrue(result);
    }

    @Test
    void tryToFreePlaceWithHistory() {
        placeService.registerPlace(1);
        Place place = placeRepository.getPlace(1);
        bookingService.fillPlaceBySlots(1);
        User user = userService.getUserById(1);
        Slot slot = Slot.builder().id(1).user(user).checkInTime(LocalDateTime.now()).checkOutTime(LocalDateTime.now()).build();
        SlotHistory slotHistory = SlotHistory.builder().slot(slot).place(place).build();
        user.addSlotHistory(1, slotHistory);

        boolean result = bookingService.tryToFreePlace(1, 1);
        assertTrue(result);
    }
}