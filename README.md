# TestJarSoft
## Запуск приложения
### Скачать из репозитория TestJarSoft 2 файла.
1. Java-архив **TestJarSoft-1.0-SNAPSHOT.jar** по адресу /target
2. Файл **application.properties** по адресу /src/main/resurces

#### В файле application.properties задать параметры подключения к базе данных MySQL:
1. spring.datasource.url=jdbc:mysql://~~**host:port**~~
2. spring.datasource.username=~~**username**~~
3. spring.datasource.password=~~**password**~~

#### Затем открыть командную строку и перейти в директорию куда были скачены файлы.

Запустить JAR файл как показано ниже:
___
java -jar -Dspring.config.location=application.properties TestJarSoft-1.0-SNAPSHOT.jar
___

## Тестирование приложения
Открыть браузер и ввести в адресной строке  
______
http://localhost:8080/category   или   http://localhost:8080/banner
______
После загрузки страницы можно редактировать/создавать/удалять категории и баннеры.

Чтобы посмотреть текст баннера нужно ввести в адресной строке 
______
http://localhost:8080/bid?category= имя категории
______
