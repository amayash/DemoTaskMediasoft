# Инструкция по запуску приложения без Docker

1. В файле application.properties следует выбрать профиль для запуска: dev или prod (путь к файлу: src/main/resources/)
2. Стоит создать PostgreSQL базу данных и роль с паролем с названиями, указанными в файле application-dev.properties или application-prod.properties в зависимости от выбранного профиля (путь к файлу: src/main/resources/)
3. В папке проекта в терминале стоит ввести команду ````./gradlew bootRun````

# Инструкция по запуску приложения c Docker

1. В папке проекта в терминале стоит ввести команды: ````./gradlew build ```` и ````docker-compose up````

**_Swagger будет доступен по следующей ссылке:_** http://localhost:8080/swagger-ui/index.html#/
