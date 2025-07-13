package t1.homework.common.model;

public enum WeatherCondition {
    SUNNY("солнечно"),
    CLOUDY("облачно"),
    RAINY("дождь");

    private final String name;

    WeatherCondition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
