package t1.homework.common.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherMessage {
    private String city;
    private int temperature;
    private WeatherCondition weatherCondition;
    private LocalDate date;
}
