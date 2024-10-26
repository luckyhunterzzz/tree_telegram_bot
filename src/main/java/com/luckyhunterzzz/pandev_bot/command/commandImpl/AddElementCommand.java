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
 * Команда для добавления элементов в дерево категорий.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для добавления новых элементов в систему. Команда может добавлять как корневые
 * элементы, так и дочерние элементы к существующим родительским категориям.
 */
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Component
public class AddElementCommand implements Command {

    private final CategoryService categoryService;

    /**
     * Выполняет команду добавления элемента.
     *
     * Этот метод обрабатывает входящее обновление от Telegram, извлекает текст сообщения
     * и добавляет элемент в систему. Он поддерживает два формата команды:
     * <ol>
     *     <li>/addElement &lt;название элемента&gt; - добавляет новый корневой элемент.</li>
     *     <li>/addElement &lt;родитель&gt; &lt;дочерний&gt; - добавляет дочерний элемент к родительскому.</li>
     * </ol>
     * Если команда имеет неправильный формат, возвращается сообщение об ошибке.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link SendMessage}, представляющий ответ о результате добавления.
     *         Если добавление прошло успешно, возвращается сообщение с результатом,
     *         в противном случае возвращается сообщение об ошибке.
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String[] messageText = update.getMessage().getText().split(" ");

        if (messageText.length == 2) {
            String elementName = messageText[1];
            String response = categoryService.addElement(elementName);
            return new SendMessage(chatId, response);
        }
        else if (messageText.length == 3) {
            String parentName = messageText[1];
            String childName = messageText[2];
            String response = categoryService.addElement(parentName, childName);
            return new SendMessage(chatId, response);
        }
        else {
            log.info("Неправильный формат команды. Используйте: /addElement <название элемента> или /addElement <родитель> <дочерний>");
            return new SendMessage(chatId, "Неправильный формат команды. Используйте: /addElement <название элемента> или /addElement <родитель> <дочерний>");
        }
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/addElement";
    }
}
