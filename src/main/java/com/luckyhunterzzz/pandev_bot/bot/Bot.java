package com.luckyhunterzzz.pandev_bot.bot;

import com.luckyhunterzzz.pandev_bot.command.CommandHandler;
import com.luckyhunterzzz.pandev_bot.command.DocumentSender;
import com.luckyhunterzzz.pandev_bot.command.MessageSender;
import com.luckyhunterzzz.pandev_bot.command.commandImpl.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Основной класс бота для Telegram.
 *
 * Этот класс расширяет {@link TelegramLongPollingBot} и реализует интерфейсы
 * {@link MessageSender} и {@link DocumentSender}. Он отвечает за обработку
 * входящих обновлений, выполнение команд и отправку сообщений и документов
 * пользователям.
 */
@Slf4j
@Component
public class Bot extends TelegramLongPollingBot implements MessageSender, DocumentSender {
    @Getter
    private final String botUsername;
    @Getter
    private final String botToken;
    private final CommandHandler commandHandler;

    /**
     * Конструктор, который инициализирует бота и регистрирует его в Telegram API.
     *
     * @param telegramBotsApi объект для регистрации бота в Telegram API
     * @param botUsername имя бота, используемое для идентификации
     * @param botToken токен бота для доступа к Telegram API
     * @param commandHandler обработчик команд, который управляет выполнением команд
     * @param downloadCommand команда для скачивания данных
     * @throws TelegramApiException если возникает ошибка при регистрации бота
     */
    public Bot(TelegramBotsApi telegramBotsApi,
               @Value("${bot.name}")String botUsername,
               @Value("${bot.token}")String botToken,
               CommandHandler commandHandler,
               DownloadCommand downloadCommand) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;

        this.commandHandler = commandHandler;

        telegramBotsApi.registerBot(this);

        downloadCommand.setDocumentSender(this);
    }

    /**
     * Обрабатывает входящие обновления от Telegram.
     *
     * Этот метод вызывается при получении обновления. Он проверяет, содержит ли
     * обновление сообщение, и обрабатывает текстовые сообщения и документы.
     * Метод выполняет следующие шаги:
     * <ol>
     *     <li>Проверяет, содержит ли обновление сообщение.</li>
     *     <li> Извлекает идентификатор чата из сообщения.</li>
     *     <li>Если сообщение содержит текст, выполняет следующие действия:
     *         <ol>
     *             <li> Извлекает текст сообщения и разбивает его на команду и параметры.</li>
     *             <li> Извлекает имя команды из текста сообщения.</li>
     *             <li>Передает команду и обновление в {@link CommandHandler} для обработки.</li>
     *             <li>Отправляет ответ пользователю, используя метод {@link #execute(BotApiMethod)}.</li>
     *         </ol>
     *     </li>
     *     <li>Если сообщение содержит документ, выполняет следующие действия:
     *         <ol>
     *             <li> Извлекает подпись документа.</li>
     *             <li>Если подпись равна "/upload", передает команду в {@link CommandHandler} для обработки.</li>
     *             <li>Если подпись не равна "/upload", отправляет сообщение с просьбой использовать команду /upload в описании к файлу.</li>
     *         </ol>
     *     </li>
     * </ol>
     * Если возникает ошибка при отправке сообщения, она логируется.
     *
     * @param update входящее обновление от Telegram, содержащее информацию о сообщении,
     *               включая текст и документы
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();

            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                String[] commandAndParams = messageText.split(" ");
                String commandName = commandAndParams[0];

                BotApiMethod<?> response = commandHandler.handleCommand(commandName, update);

                try {
                    execute(response);
                } catch (TelegramApiException e) {
                    log.error("Ошибка отправки сообщений: " + e.getMessage());
                }
            } else if (update.getMessage().hasDocument()) {
                String caption = update.getMessage().getCaption();
                if (caption != null && caption.equals("/upload")) {
                    BotApiMethod<?> response = commandHandler.handleCommand("/upload", update);
                    try {
                        execute(response);
                    } catch (TelegramApiException e) {
                        log.error("Ошибка отправки сообщений: " + e.getMessage());
                    }
                } else {
                    try {
                        log.info("Пожалуйста, используйте команду /upload в описании к файлу для загрузки.");
                        execute(new SendMessage(chatId, "Пожалуйста, используйте команду /upload в описании к файлу для загрузки."));
                    } catch (TelegramApiException e) {
                        log.error("Ошибка отправки сообщений: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Отправляет текстовое сообщение в указанный чат.
     *
     * @param chatId идентификатор чата, в который будет отправлено сообщение
     * @param text текст сообщения, которое нужно отправить
     */
    @Override
    public void sendMessage(String chatId, String text) {
        try {
            SendMessage sendMessage = new SendMessage(chatId, text);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    /**
     * Отправляет документ в указанный чат.
     *
     * @param chatId идентификатор чата, в который будет отправлен документ
     * @param document объект {@link InputFile}, представляющий документ, который нужно отправить
     */
    @Override
    public void sendDocument(String chatId, InputFile document) {
        try {
            SendDocument sendDocument = new SendDocument(chatId, document);
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке документа: " + e.getMessage());
        }
    }
}