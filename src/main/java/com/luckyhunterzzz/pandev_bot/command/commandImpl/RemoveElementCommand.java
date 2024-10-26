package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.command.Command;
import com.luckyhunterzzz.pandev_bot.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Команда для удаления элемента из категории.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для удаления элемента из системы на основе его имени. Команда принимает
 * текстовое сообщение, извлекает имя элемента и вызывает соответствующий метод
 * в {@link CategoryService}.
 */
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Component
public class RemoveElementCommand implements Command {

    private final CategoryService categoryService;

    /**
     * Выполняет команду удаления элемента.
     *
     * Этот метод обрабатывает входящее обновление от Telegram, извлекает имя
     * элемента из сообщения и вызывает метод {@link CategoryService#removeElement(String)}
     * для удаления элемента. Если команда имеет неправильный формат, возвращается
     * сообщение об ошибке.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link SendMessage}, представляющий ответ о результате удаления
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String[] messageText = update.getMessage().getText().split(" ");

        if (messageText.length == 2) {
            String elementName = messageText[1];
            String response = categoryService.removeElement(elementName);
            return new SendMessage(chatId, response);
        } else {
            log.info("Неправильный формат команды. Используйте: /removeElement <название элемента>");
            return new SendMessage(chatId, "Неправильный формат команды. Используйте: /removeElement <название элемента>");
        }
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/removeElement";
    }
}
