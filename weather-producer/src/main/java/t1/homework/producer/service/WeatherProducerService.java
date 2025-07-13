package t1.homework.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import t1.homework.common.model.WeatherCondition;
import t1.homework.common.model.WeatherMessage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class WeatherProducerService {

    @Value("${app.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, WeatherMessage> kafkaTemplate;
    private LocalDate simulatedCurrentDate = LocalDate.now();
    private final Random random = new Random();

    private final List<String> cities = Arrays.asList("Москва", "Томск", "Новосибирск");
    private final WeatherCondition[] weatherConditions = WeatherCondition.values();

    public WeatherProducerService(KafkaTemplate<String, WeatherMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(WeatherMessage message) {
        System.out.println("Produced: " + message);
        kafkaTemplate.send(topic, message.getCity(), message);
    }

    @Scheduled(fixedRate = 60000) // 1 min
    public void sendWeatherMessages() {
        for (String city : cities) {
            int temperature = random.nextInt(36);
            WeatherCondition condition = weatherConditions[random.nextInt(weatherConditions.length)];
            WeatherMessage message = new WeatherMessage(city, temperature, condition, simulatedCurrentDate);
            sendMessage(message);
        }
        simulatedCurrentDate = simulatedCurrentDate.plusDays(1);
    }
}
