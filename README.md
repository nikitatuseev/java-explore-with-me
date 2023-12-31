https://github.com/nikitatuseev/java-explore-with-me/pull/6

# ExploreWithMe

ExploreWithMe - это приложение, созданное для тех, кто ищет интересные события и компанию для их участия. Мы предоставляем удобный и простой способ делиться информацией о интересных мероприятиях и находить единомышленников, готовых присоединиться к ним.

## Основной функционал

### Афиша

Основной сервис ExploreWithMe - это афиша, где пользователи могут предлагать различные события - от выставок до походов в кино. Вы можете добавлять информацию о событиях, включая дату, время, место и описание. Просматривайте предложенные события и присоединяйтесь к тем, которые вас интересуют.

### Сервис статистики

Дополнительный сервис статистики хранит информацию о количестве просмотров событий и позволяет выполнять различные запросы для анализа работы приложения. Этот инструмент предназначен для разработчиков и администраторов приложения, чтобы получать данные о популярности событий и эффективности работы приложения в целом.

## Технические детали

ExploreWithMe разработан с использованием следующего стека технологий:

- Java 11
- Spring Boot
- Maven
- JPA (Java Persistence API)
- PostgreSQL (база данных)
- JUnit 5 (для тестирования)
- Lombok (для упрощения кода)

## API

Приложение предоставляет API для управления основными сущностями, обеспечивая операции создания, чтения, обновления и удаления данных через соответствующие HTTP-методы и пути запросов.

## Как начать

1. Установить Docker на локальную машину.
2. Склонировать  репозиторий ExploreWithMe.
3. Перейти в директорию склонированного проекта.
4. Запустить Docker Compose команду:
   docker compose up --build