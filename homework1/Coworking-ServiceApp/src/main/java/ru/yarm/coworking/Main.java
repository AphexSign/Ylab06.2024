package ru.yarm.coworking;

import ru.yarm.coworking.Menus.MenuHandler;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Services.BookingService;
import ru.yarm.coworking.Services.PlaceService;
import ru.yarm.coworking.Services.UserService;

public class Main {

    static {
        PlaceService placeService = new PlaceService();
        UserService userService = new UserService();
        BookingService bookingService = new BookingService();
        userService.registerPerform(new User("admin", "admin", "ROLE_ADMIN"));
        userService.registerPerform(new User("a", "a","ROLE_CLIENT"));
        placeService.registerPlace(1);
        placeService.registerPlace(1);
        placeService.registerPlace(2);
        placeService.registerPlace(2);
        bookingService.fillPlaceBySlots(1);
        bookingService.fillPlaceBySlots(2);

        bookingService.tryToOccupyPlace(2,2,12);
        bookingService.tryToOccupyPlace(1,1,3);
        bookingService.tryToOccupyPlace(2,2,10);
        bookingService.tryToOccupyPlace(1,2,7);
    }

    public static void main(String[] args) {
       MenuHandler.startMainMenu();
//        BookingService bookingService = new BookingService();
//        UserService userService = new UserService();
//
//
//        bookingService.showAllOccupySlotsForAllPlaces();
//        bookingService.showAllOccupySlotsForAllFilterUser(2);
//        bookingService.showAllOccupySlotsForAllFilterPlace(1);
//        bookingService.showAllOccupySlotsForAllFilterDate("23.06.2024");

    }
}
