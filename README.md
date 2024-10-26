# tree_telegram_bot
Telegram-бот, разработанный на Spring Boot и PostgreSQL, который позволяет пользователям создавать, просматривать и управлять древовидной структурой категорий. Бот поддерживает команды для добавления, удаления элементов, а также выгрузки и загрузки дерева категорий в формате Excel.

## 📋 Функции бота

1. **Создание и управление деревом категорий** — поддержка команд для добавления, удаления и отображения категорий.
2. **Работа с документами** — загрузка и выгрузка дерева категорий в формате Excel.
3. **Справка** — предоставляет список доступных команд и краткое описание их функционала.

## 🚀 Основные технологии

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL**
- **Telegram API** с использованием библиотеки TelegramBots
- **Apache POI** для работы с Excel-файлами
- **SLF4J** для логирования

## 📂 Структура проекта

- `bot/` — содержит основной класс бота и обработку сообщений.
- `command/` — включает все команды, доступные в боте, такие как добавление, удаление элементов и справка.
- `service/` — слой сервисов, включающий логику работы с категориями.
- `model/` — классы, представляющие структуру данных, такие как `Category`.
- `repository/` — интерфейс репозитория для работы с базой данных PostgreSQL.
- `config/` — конфигурационные файлы и настройки проекта.

## 🛠 Установка и настройка

### 1. Клонирование репозитория
Клонируйте репозиторий на локальную машину:
git clone https://github.com/luckyhunterzzz/tree_telegram_bot.git
Перейдите в директорию проекта:
cd tree_telegram_bot
### 2.Установка зависимостей
Для работы проекта потребуется настроить базу данных PostgreSQL. 
Создайте базу данных. Создайте и отредактируйте параметры подключения в файле application.properties по шаблону application.properties.origin
### 2.Запуск проекта
./gradlew bootRun

📜 **Использование**
После запуска бота можно взаимодействовать с ним через следующие команды:

/viewTree — Показать текущее дерево категорий в иерархическом виде.
/addElement <название элемента> — Добавить корневой элемент.
/addElement <родитель> <дочерний> — Добавить дочерний элемент к существующему родителю.
/removeElement <название элемента> — Удалить элемент и его дочерние элементы.
/download — Сохранить текущее дерево категорий в Excel-файле и получить его.
/upload — Загрузить Excel-файл с деревом категорий и сохранить его в базе данных.
/help — Получить справку по доступным командам.
