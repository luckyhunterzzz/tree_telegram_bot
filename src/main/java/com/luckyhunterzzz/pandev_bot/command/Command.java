package com.luckyhunterzzz.pandev_bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для команд Telegram бота.
 *
 * Этот интерфейс определяет структуру для команд, которые могут быть выполнены
 * ботом. Каждая команда должна предоставлять своё имя и реализацию метода
 * для обработки входящих обновлений.
 */
public interface Command {
    /**
     * Получает имя команды.
     *
     * @return имя команды, которое будет использоваться для её идентификации
     */
    String getCommandName();
    /**
     * Выполняет команду на основе входящего обновления.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link BotApiMethod}, представляющий ответ на команду
     */
    BotApiMethod<?> execute(Update update);
}
