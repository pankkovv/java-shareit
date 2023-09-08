# java-shareit
## Описание приложения
Бекэнд сервиса для обмена полезными вещами: инструментами, гаджетами, книгами и пр.

Приложение имеет следующие функции:
1. Создание пользователей и полезных вещи;
2. Хранение данных в БД;
3. Бронирование необходимой вещи;
4. Подтверждение запроса на бронирование вещи;
5. Создание запроса на вещь, которой нет в БД;
6. Создание комментария после использования вещи.
7. Поиск вещи по названию или описанию.

## Примеры запросов
User:
1. Создание нового пользователя: POST http://localhost:8080/users, в Request Body json с данными пользователя.
2. Обновление данных пользователя: PATCH http://localhost:8080/users, в Request Body json с данными пользователя.
3. Удаление пользователя по id: DELETE http://localhost:8080/users/{id}.
4. Получение списка пользователей: GET http://localhost:8080/users.
5. Получение пользователя по id: GET http://localhost:8080/users/{id}.

Item:
1. Создание новой вещи: POST http://localhost:8080/items, в Request Body json с данными вещи.
2. Обновление данных вещи: PATCH http://localhost:8080/items, в Request Body json с данными вещи.
4. Получение списка вещей: GET http://localhost:8080/items?from=&size= с указанием в параметрах запроса данных для пагинации.
5. Получение вещи по id: GET http://localhost:8080/items/{id}.
6. Поиск вещи по названию или описанию: http://localhost:8080/items/search?text&from=&size= с указанием в параметрах запроса текста и данных для пагинации.

Comment:
1. Создание комментария к вещи по id: POST http://localhost:8080/items/comment в Request Body с данными комментария.

Booking:
1. Создание бронирования вещи: POST http://localhost:8080/bookings в Request Body с данными вещи для бронирования.
2. Подтверждение бронирования владельцем вещи: PATCH http://localhost:8080/bookings/{bookingId}?approved= с указанием в параметрах запроса булевого значения.
3. Получения списка бронирований других пользователей: GET http://localhost:8080/bookings?state=&from=&size= с указанием в параметрах запроса категории бронирования и данных для пагинации. 
4. Получение списка бронирований для владельца вещей: GET http://localhost:8080/bookings/owner?state=&from=&size= с указанием в параметрах запроса категории бронирования и данных для пагинации.
5. Получение бронирования по id: GET http://localhost:8080/bookings/{bookingId}.

Request:
1. Создание запроса для отсутствующей вещи: POST http://localhost:8080/requests в Request Body с данными вещи для бронирования.
2. Получение списка всех запросов: GET http://localhost:8080/requests/all?from=&size= с указанием в параметрах запроса данных для пагинации.
3. Получение списка запросов для создателя запроса: GET http://localhost:8080/requests
4. Получение запроса по id: GET http://localhost:8080/requests/{requestId}.

## Пример запроса к БД
1. Получние списка, ранжированного по уменьшению даты создания, всех бронирований для создателя бронирований: 
```postgreSQL
@Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 " +
            "order by b.start desc")
```       
            
## Стек
- JDK 11
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker
- Mock
- JUnit

## Шаги по запуску приложения
- Склонировать репозиторий
- Синхронизировать pom.xml каждого модуля с локальным репозиторием
- Запустить билд через консоль с помощью команды (docker-compose up)
- Взаимодействовать с приложением через API http://localhost:8080/
- Взаимодействовать с БД через API http://localhost:8080/h2-console c username= sa и passwword= password, либо локально через UI для PostgreSQL c username= postgres и passwword= test
----
Приложение написано на Java и протестировано слайсами. Пример кода:
```java
@Transactional(readOnly = true)
    @Override
    public BookingDto getByIdBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotBookingException(ExceptionMessages.NOT_FOUND_BOOKING.label));
        if (Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.debug(LogMessages.BOOKING_ID.label, bookingId);
            return BookingMap.mapToBookingDto(bookingRepository.getByIdBooking(bookingId, userId));
        } else {
            throw new NotBookingException(ExceptionMessages.NOT_FOUND_BOOKING.label);
        }
    }
```
----
Приложение создано в рамках прохождения курса Java-разработчик от [Яндекс-Практикум](https://practicum.yandex.ru/java-developer/ "Тут учат Java!") 

