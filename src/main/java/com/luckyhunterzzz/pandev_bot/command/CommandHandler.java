package com.luckyhunterzzz.pandev_bot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Обработчик команд для Telegram бота.
 *
 * Этот класс управляет доступными командами и их выполнением. Он хранит
 * команды в виде отображения (Map), где ключом является имя команды, а значением
 * — соответствующий объект команды. Обработчик позволяет легко добавлять и
 * вызывать команды в зависимости от входящих обновлений.
 */
@Component
public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор, который инициализирует обработчик команд.
     *
     * @param commandList список доступных команд, которые будут зарегистрированы
     */
    @Autowired
    public CommandHandler(List<Command> commandList) {
        for (Command command : commandList) {
            commands.put(command.getCommandName(), command);
        }
    }

    /**
     * Обрабатывает команду на основе её имени и входящего обновления.
     *
     * @param commandName имя команды, которую нужно обработать
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link BotApiMethod}, представляющий ответ на команду
     */
    public BotApiMethod<?> handleCommand(String commandName, Update update) {
        Command command = commands.getOrDefault(commandName, commands.get("/help"));
        return command.execute(update);
    }
}