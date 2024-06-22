package ru.yarm.coworking.Menus;

import ru.yarm.coworking.Models.Place;
import ru.yarm.coworking.Models.User;
import ru.yarm.coworking.Services.BookingService;
import ru.yarm.coworking.Services.PlaceService;
import ru.yarm.coworking.Services.UserService;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Класс отвечающий за работу консольного меню в приложении.
 */
@Data
public class MenuHandler {
    private static final Scanner sc = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final PlaceService placeService = new PlaceService();
    private static final BookingService bookingService = new BookingService();

    /**
     * Параметр, имитирующий кэш, хранящий текущего пользователя, прошедшего аутентификацию в системе
     */
    public static User userCached;
    /**
     * Параметр, дающий имя пользователям, не прошедших авторизацию
     */
    private static String userNameGreeting = "Гость";
    private static final String PLEASE_AUTHORIZE = "Просим вначале авторизоваться!";


    public static void startMainMenu() {
        int mainMenuChoice = showMainMenu();
        while (true) {
            switch (mainMenuChoice) {
                case 1:
                    showRegistrationMenu();
                    startMainMenu();
                    break;
                case 2:
                    userCached = showAuthorizationMenu();
                    startMainMenu();
                    break;
                case 3:
                    int place_id = showClientPlaceListMenu();
                    int slot_id = showClientSlotsForPlace(place_id);
                    if (userCached != null) {
                        bookingService.tryToOccupyPlace(place_id, userCached.getId(), slot_id);
                        startMainMenu();
                    } else {
                        System.out.println("\n\n" + PLEASE_AUTHORIZE + "\n\n");
                        startMainMenu();
                    }
                    break;
                case 4:
                    if (userCached != null) {
                        showClientHisSlots(userCached);
                        startMainMenu();
                    } else {
                        System.out.println("\n\n" + PLEASE_AUTHORIZE + "\n\n");
                        startMainMenu();
                    }
                    break;
                case 5:
                    if (userCached != null && userCached.getRole().equals("ROLE_ADMIN")) {
                        showAllPlacesMenuAdmin();
                    } else {
                        System.out.println("У вас нет прав!");
                        startMainMenu();
                    }
                    break;
                case 6:
                    if (userCached != null && userCached.getRole().equals("ROLE_ADMIN")) {
                        showAllBookingSlotsAdmin();
                    } else {
                        System.out.println("У вас нет прав!");
                        startMainMenu();
                    }
                    break;

                case 0:
                    System.out.println("Приложение завершает свою работу.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Меню не распознано.");
                    startMainMenu();
                    break;
            }
        }
    }

    public static int showMainMenu() {
        String role = "Незарегистрированный";
        if (userCached != null) {
            if (userCached.getRole().equals("ROLE_ADMIN")) {
                role = "Админ";
            } else if (userCached.getRole().equals("ROLE_CLIENT")) {
                role = "Клиент";
            }
        }
        System.out.println("==============================================");
        System.out.println("Главное меню приложения. Приветствуем, " + userNameGreeting + "!");
        System.out.println("Подсказка - login: admin \npassword: admin");
        System.out.println("Ваша роль: " + role);
        System.out.println("\n\n1. Страничка для регистрации");
        System.out.println("2. Страница для авторизация");
        System.out.println("3. Список коворкинг-площадок(клиент)");
        System.out.println("4. Управление бронью клиента(клиент)");

        if (userCached != null && userCached.getRole().equals("ROLE_ADMIN")) {
            System.out.println("5. Управление коворкинг-площадками(админ)");
            System.out.println("6. Список всех бронирований(админ)");
        }
        System.out.println("0. Выход");
        System.out.print("Ваш выбор: ");
        int result = 999;
        try {
            result = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверная команда");
        }

        return result;
    }


    public static void showRegistrationMenu() {
        System.out.println("Введите не пустые данные в поля для регистрации:\n\n");
        System.out.print("Логин: ");
        String login = sc.nextLine();
        System.out.print("Пароль: ");
        String password = sc.nextLine();
        User user = User.builder().login(login).password(password).build();
        userService.registerPerform(user);
    }

    public static User showAuthorizationMenu() {
        System.out.println("==============================================");
        System.out.println("\n\nМеню авторизации:\n");
        System.out.print("Логин: ");
        String login = sc.nextLine();
        System.out.print("Пароль: ");
        String password = sc.nextLine();
        User user = User.builder().login(login).password(password).build();
        User result = userService.performAuthenticate(user);
        if (result != null) {
            userCached = result;
            userNameGreeting = userCached.getLogin();
        }
        return result;
    }

    public static Integer showClientPlaceListMenu() {
        System.out.println("=============================");
        System.out.println("Выберите площадку для бронирования:\n\n");
        placeService.showAllPlaces();
        System.out.println();
        System.out.print("Ваш выбор выбор:");
        int result = 0;
        try {
            result = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверная команда");
        }
        return result;
    }

    public static Integer showClientSlotsForPlace(Integer place_id) {
        Place place = placeService.getPlaceById(place_id);
        if (place != null) {
            System.out.println("=============================");
            System.out.println("Выберите слот времени для бронирования у места:" + place_id + " " + place.getPlaceType().getTitle());
        } else System.out.println("Неверная информация");
        if (bookingService.showFreeSlotForPlace(place_id)) {
            System.out.println("");
            System.out.print("Ваш выбор окна: ");
            return Integer.parseInt(sc.nextLine());
        } else return 0;
    }

    public static void showClientHisSlots(User user) {
        System.out.println();
        System.out.println("=============================");
        System.out.println("Все слоты времени у пользователя " + user.getLogin() + "\n\n");
        userService.showAllSlotByUser(user);

        System.out.println("\nКоманды:");
        System.out.println("1: Удалить слот");
        System.out.println("2: Вернуться в главное меню ");
        System.out.print("Введите номер команды: ");
        Integer command_id = 0;
        try {
            command_id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверная команда!");
        }
        switch (command_id) {
            case 1:
                System.out.print("Какой слот прикажите удалить?: ");
                Integer slotHistory_id = -999;
                try {
                    slotHistory_id = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неверная команда!");
                }
                bookingService.tryToFreePlace(user.getId(), slotHistory_id);
                break;
            case 2:
                startMainMenu();
                break;
            default:
                System.out.println("Команда не распознана");
                startMainMenu();
                break;
        }

    }

    public static void showAllPlacesMenuAdmin() {
        System.out.println("=================================");
        System.out.println("Список всех площадок:\n");
        placeService.showAllPlaces();
        System.out.println("\nКоманды:");
        System.out.println("1: Добавить коворкинг-площадку");
        System.out.println("2: Добавить слоты времени на площадку( + id_площадки) ");
        System.out.println("3: Удаление площадки( + id_площадки) ");
        System.out.println("4: Изменение типа площадки( + id_площадки) ");
        System.out.println("5: Выход в главное меню");
        System.out.print("Введите номер команды: ");
        Integer command_id = -999;
        try {
            command_id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверная команда");
        }
        switch (command_id) {
            case 1:
                System.out.println("Выберите (1) Рабочее место (2) Конференц-зал");
                System.out.print("Итак, ваш выбор: ");
                Integer place_type_id = -999;
                try {
                    place_type_id = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый тип ввода!");
                }
                placeService.registerPlace(place_type_id);
                showAllPlacesMenuAdmin();
                break;
            case 2:
                System.out.println("Выберите id-площадки для добавления слотов");
                System.out.print("Итак, ваш выбор: ");
                Integer place_id = -999;
                try {
                    place_id = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый тип ввода");
                }
                bookingService.fillPlaceBySlots(place_id);
                showAllPlacesMenuAdmin();
                break;
            case 3:
                System.out.println("Выберите id-площадки для ее удаления из системы");
                System.out.print("Итак, ваш выбор: ");
                Integer place_id_deleted = -999;
                try {
                    place_id_deleted = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Неверный тип ввода!");
                }
                placeService.deletePlaceById(place_id_deleted);
                showAllPlacesMenuAdmin();
                break;
            case 4:
                Integer place_id_change = -999;
                Integer place_type = -999;
                try {
                    System.out.print("Выберите id-площадки для изменения ее статуса, ваш выбор: ");
                    place_id_change = Integer.parseInt(sc.nextLine());
                    System.out.print("Выберите тип площадки, ваш выбор:");
                    place_type = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый тип ввода");
                }
                placeService.updatePlace(place_id_change, place_type);
                showAllPlacesMenuAdmin();
                break;
            case 5:
                startMainMenu();
                break;
            default:
                showAllPlacesMenuAdmin();
                break;
        }
    }

    public static void showAllBookingSlotsAdmin() {
        System.out.println("=================================");
        System.out.println("Список всех площадок:\n");
        System.out.println("\nКоманды:");
        System.out.println("0. Показать без фильтра");
        System.out.println("1. Фильтрация по дате");
        System.out.println("2. Фильтрация по пользователю");
        System.out.println("3. Фильтрация по площадке");
        System.out.println("4: Выход в главное меню");

        Integer command_id = -999;
        try {
            System.out.print("Ваш выбор: ");
            command_id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверная команда");
        }
        switch (command_id) {

            case 0:
                System.out.println("=================================");
                System.out.println("Список всех площадок:\n");
                bookingService.showAllOccupySlotsForAllPlaces();
                break;


            case 1:
                System.out.println("=================================");
                System.out.println("Список всех площадок, фильтр по дате:\n");
                String date;
                try {
                    System.out.print("Введите дату дд.мм.гггг: ");
                    date = sc.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate dateTime = LocalDate.parse(date, formatter);
                    bookingService.showAllOccupySlotsForAllFilterDate(dateTime);
                } catch (DateTimeParseException e) {
                    System.out.println("Недопустимый тип ввода");
                }
                showAllBookingSlotsAdmin();
                break;
            case 2:
                System.out.println("=================================");
                System.out.println("Список всех площадок, фильтр по пользователю:\n");
                Integer user_id;
                try {
                    System.out.print("Выберите id-пользователя для фильтрации: ");
                    user_id = Integer.parseInt(sc.nextLine());
                    bookingService.showAllOccupySlotsForAllFilterUser(user_id);
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый тип ввода");
                }
                showAllBookingSlotsAdmin();
                break;
            case 3:
                System.out.println("=================================");
                System.out.println("Список всех площадок, фильтр по помещению:\n");
                Integer place_id;
                try {
                    System.out.print("Выберите id-площадки для фильтрации: ");
                    place_id = Integer.parseInt(sc.nextLine());
                    bookingService.showAllOccupySlotsForAllFilterPlace(place_id);
                } catch (NumberFormatException e) {
                    System.out.println("Недопустимый тип ввода");
                }
                showAllBookingSlotsAdmin();
                break;
            case 4:
                startMainMenu();
                break;
            default:
                startMainMenu();
                break;
        }
    }
}



