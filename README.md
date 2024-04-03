### Hexlet tests and linter status:
[![Actions Status](https://github.com/d1z3d/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/d1z3d/java-project-72/actions) [![Maintainability](https://api.codeclimate.com/v1/badges/b9c4f6c9d8a6c9381f4d/maintainability)](https://codeclimate.com/github/d1z3d/java-project-72/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/b9c4f6c9d8a6c9381f4d/test_coverage)](https://codeclimate.com/github/d1z3d/java-project-72/test_coverage)

### Описание проекта
[Page analyzer](https://seo-page-analyzer-j5l0.onrender.com) - сайт, который анализирует указанные страницы на SEO пригодность.

### Используемые инструменты
* ЯП: Java 20
* Сборщик: Gradle 8.4
* Фреймворк: Javalin
* Фронт: JTE, Bootstrap
* HTTP-клиент: Unirest
* Парсер: Jsoup
* ОРМ: Отсутствует.
 * Подключение к БД: Hikari
 * Создание БД: DDL
 * Получение данных: DML
* БД:
  * DEV: H2
  * PROD: PostgreSQL
* Тесты: JUnit 5, MockWebServer
* Тест репортер: Jacoco
* Деплой: PaaS на [render.com](https://render.com)

Также в проекте используется Docker при деплое на PRODUCTION.

### Сборка проекта
Вам необходимо установить утилиту [make](https://guides.hexlet.io/ru/makefile-as-task-runner/?_gl=1*1b2sh59*_ga*NzQ5MzAxNTIzLjE2OTkyOTM2MTc.*_ga_PM3R85EKHN*MTcwMjIyNTQ0MS4xMDguMS4xNzAyMjI3OTYzLjYwLjAuMA..*_ga_WWGZ6EVHEY*MTcwMjIyNTQ0MS4xMTEuMS4xNzAyMjI3OTYzLjYwLjAuMA..)
```
make build
```

### Запуск проекта
```
make run-dist
```
