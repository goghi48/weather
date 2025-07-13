package t1.homework.consumer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import t1.homework.common.model.Period;
import t1.homework.common.model.TemperatureType;
import t1.homework.consumer.service.WeatherConsumerService;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherConsumerController {

    private final WeatherConsumerService weatherConsumerService;

    public WeatherConsumerController(WeatherConsumerService weatherConsumerService) {
        this.weatherConsumerService = weatherConsumerService;
    }

    @Operation(
        summary = "Получить min/max/avg температуру в городе за определённый период", 
        description = "Возвращает минимальную, максимальную или средн температуру в указанном городе за указанный период.")
    @GetMapping("{city}/{type}-temperature")
    public ResponseEntity<String> getTemperature(
            @Parameter(description = "Название города", example = "Новосибирск", 
                schema = @Schema(type = "string", allowableValues = {"Москва", "Томск", "Новосибирск" })) @PathVariable String city,
            @Parameter(description = "Тип температуры: min, max или avg", example = "MIN") @PathVariable TemperatureType type,
            @Parameter(description = "Период: week, month или year(WEEK по умолчанию)", example = "WEEK") @RequestParam(defaultValue = "WEEK") Period period) {

        Integer temperature = weatherConsumerService.getTemperatureForPeriod(city, type, period);

        if (temperature != null) {
            return ResponseEntity.ok(
                "\"" + type + "\" температура в городе " + city + " за следующий " + period + ": " + temperature + "℃");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Получить максимальную продолжительность дождей за выбранный период", 
        description = "Возвращает количество дней подряд, когда шёл дождь в указанном городе.")
    @GetMapping("{city}/max-rain-streak")
    public ResponseEntity<String> getMaxRainStreak(
            @Parameter(description = "Название города", example = "Москва", 
                schema = @Schema(type = "string", allowableValues = {"Москва", "Томск", "Новосибирск" })) @PathVariable String city,
            @Parameter(description = "Период: week, month или year (MONTH по умолчанию)", example = "MONTH") @RequestParam(defaultValue = "MONTH") Period period) {

        Integer maxStreak = weatherConsumerService.getMaxRainStreakForPeriod(city, period);

        if (maxStreak != null) {
            return ResponseEntity.ok(
                    "Максимальная продолжительность дождей в городе " + city + " за следующий " + period + ": "
                            + maxStreak);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
