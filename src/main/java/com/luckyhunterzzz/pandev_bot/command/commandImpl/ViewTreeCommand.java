package com.luckyhunterzzz.pandev_bot.command.commandImpl;

import com.luckyhunterzzz.pandev_bot.command.Command;
import com.luckyhunterzzz.pandev_bot.service.CategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Команда для отображения дерева категорий.
 *
 * Этот класс реализует интерфейс {@link Command} и предоставляет функциональность
 * для получения и отображения дерева категорий в ответ на команду /viewTree.
 */
@Setter
@Getter
@RequiredArgsConstructor
@Component
public class ViewTreeCommand implements Command {

    private final CategoryService categoryService;

    /**
     * Выполняет команду и возвращает строковое представление дерева категорий.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении
     * @return объект {@link SendMessage}, представляющий ответ с деревом категорий
     */
    @Override
    public BotApiMethod<?> execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String treeView = categoryService.getCategoryTree();
        return new SendMessage(chatId, treeView);
    }

    /**
     * Получает имя команды.
     *
     * @return имя команды, используемое для её идентификации
     */
    @Override
    public String getCommandName() {
        return "/viewTree";
    }

}
