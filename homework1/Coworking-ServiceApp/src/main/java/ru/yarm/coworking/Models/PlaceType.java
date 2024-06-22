package ru.yarm.coworking.Models;

public enum PlaceType {
    WORKSPACE("Рабочее место"), CONFERENCE_HALL("Конференц-зал");

    private String title;

    PlaceType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
