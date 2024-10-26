package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Команда для отображения справки по доступным командам.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для отображения списка доступных команд бота. При вызове команды /help
 * пользователю отправляется сообщение с описанием всех доступных команд.
 */
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Component
public class HelpCommand implements Command {

    /**
     * Выполняет команду и возвращает список доступных команд.
     *
     * Этот метод обрабатывает входящее обновление от Telegram, формирует сообщение
     * со списком доступных команд и отправляет его пользователю.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link SendMessage}, представляющий ответ с помощью
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String helpMessage = """
            Доступные команды:
            /viewTree - Показать дерево категорий.
            /addElement <название элемента> - Добавить новый элемент.
            /addElement <родитель> <дочерний> - Добавить дочерний элемент.
            /removeElement <название элемента> - Удалить элемент и его дочерние.
            /download - Скачать файл с деревом категорий.
            /upload - Загрузить файл с деревом категорий.
            /help - Показать список команд.
            """;
        log.info("Вызов /help");
        return new SendMessage(chatId, helpMessage);
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/help";
    }
}
