Репозиторий содержит проект для домашних заданий курса Highload Architect.

Проект использует Java, Spring boot, Gradle

Для запуска сервиса необходимо:
собрать приложение
gradle -Dskip.tests build

собрать образ и запустить сервисы в докере
docker-compose build
docker-compose up  

Постман коллекция лежит в корне проекта:
Homework1.postman_collection.json
