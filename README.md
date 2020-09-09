# StickerSorterBot

Телеграм-бот для удобной сортировки стикеров, написанный на Kotlin+Spring

## Конфигурация

Требования: JDK/JRE 11+, прямые руки.

Отредактируйте `src/main/resources/application.properties`

- *spring.data.mongodb.uri* - строка для соединения с MongoDB
- *spring.data.mongodb.database* - название БД
- *bot.token* - токен бота в телеграме
- *bot.username* - @юзернейм бота в телеграме
- *website.cacheDirectory* - папка в которой хранить кеш стикеров на сервере
- *website.url* - адрес сайта на котором будет висеть бот
- *server.port* - HTTP порт сервера. Например, 8080.

По умолчанию конфиг тянет значения из переменных окружения, т.е. например, чтобы получить значение PATH, впишите ${PATH}

Имеется поддержка профилей, для этого в том же каталоге создайте файл application-PROFILENAME.properties


## Сборка

**Linux/Mac**

`./gradlew build`

**Windows**

`gradlew.bat build`

Исполняемый jar-файл будет лежать в `build/libs`

## Запуск

`java -jar build/libs/stickersorterbot-1.0.0.jar`

Чтобы использовать профили, запускайте так:

`java -Dspring.profiles.active=PROFILENAME -jar build/libs/stickersorterbot-1.0.0.jar`

## Справка по использованию

Смотрите [тут](src/main/resources/help.txt) или отправьте боту команду /help
