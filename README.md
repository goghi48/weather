# Weather Application

Микросервисная система на Java + Spring Boot, моделирующая данные о погоде и предоставляющая аналитику через Kafka.

## Состав проекта

-   `weather-producer` — генерирует случайные погодные данные и публикует их в Kafka.
-   `weather-consumer` — получает данные из Kafka и предоставляет API для анализа.
-   `weather-common` — общий модуль с моделями и перечислениями.
-   `Kafka` — брокер сообщений.
-   `Kafka UI` — визуальный интерфейс для мониторинга Kafka.

---

## Запуск проекта

Требования:
> -   Java 17+
> -   Maven
> -   Docker

---

## Доступы к сервисам

| Сервис              | Адрес                                       |
| ------------------- | ------------------------------------------- |
| Kafka UI            | http://localhost:8080                       |
| Consumer Swagger UI | http://localhost:8081/swagger-ui/index.html |

---

## Docker порты

| Сервис           | Порт |
| ---------------- | ---- |
| Kafka            | 9092 |
| Kafka UI         | 8080 |
| weather-consumer | 8081 |
| weather-producer | 8082 |
