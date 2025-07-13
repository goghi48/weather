package t1.homework.consumer.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import t1.homework.common.model.WeatherCondition;
import t1.homework.common.model.WeatherMessage;
import t1.homework.common.model.Period;
import t1.homework.common.model.TemperatureType;

@Service
public class WeatherConsumerService {

    private final Map<String, List<WeatherMessage>> weatherData = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(WeatherMessage message) {
        System.out.println("Consumed: " + message);
        weatherData
            .computeIfAbsent(message.getCity(), k -> new CopyOnWriteArrayList<>())
            .add(message);
    }

    private List<WeatherMessage> getWeatherDataForPeriod(String city, Period period) {
        List<WeatherMessage> cityWeather = weatherData.get(city);

        if (cityWeather == null || cityWeather.isEmpty()) {
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate periodEnd;

        switch (period) {
            case WEEK -> periodEnd = today.plusWeeks(1);
            case MONTH -> periodEnd = today.plusMonths(1);
            case YEAR -> periodEnd = today.plusYears(1);
            default -> throw new IllegalArgumentException();
        }

        return cityWeather.stream()
                .filter(message -> !message.getDate().isBefore(today) && !message.getDate().isAfter(periodEnd))
                .collect(Collectors.toList());
    }

    public Integer getTemperatureForPeriod(String city, TemperatureType type, Period period) {
        List<WeatherMessage> filtredData = getWeatherDataForPeriod(city, period);

        if (filtredData.isEmpty()) {
            return null;
        }

        switch (type) {
            case MIN:
                Optional<Integer> minTemperature = filtredData.stream()
                        .map(WeatherMessage::getTemperature)
                        .min(Comparator.naturalOrder());
                return minTemperature.orElse(null);
            case MAX:
                Optional<Integer> maxTemperature = filtredData.stream()
                        .map(WeatherMessage::getTemperature)
                        .max(Comparator.naturalOrder());
                return maxTemperature.orElse(null);
            case AVG:
                OptionalDouble avgTemperature = filtredData.stream()
                        .mapToInt(WeatherMessage::getTemperature)
                        .average();
                return avgTemperature.isPresent() ? (int) avgTemperature.getAsDouble() : null;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Integer getMaxRainStreakForPeriod(String city, Period period) {
        List<WeatherMessage> filtredData = getWeatherDataForPeriod(city, period);

        if (filtredData.isEmpty()) {
            return null;
        }

        List<WeatherMessage> sortedData = filtredData.stream()
                .sorted(Comparator.comparing(WeatherMessage::getDate))
                .toList();

        int maxStreak = 0;
        int currStreak = 0;

        for (WeatherMessage msg : sortedData) {
            if (msg.getWeatherCondition() == WeatherCondition.RAINY) {
                currStreak++;
                maxStreak = Math.max(maxStreak, currStreak);
            } else {
                currStreak = 0;
            }
        }
        return maxStreak;
    }

}
