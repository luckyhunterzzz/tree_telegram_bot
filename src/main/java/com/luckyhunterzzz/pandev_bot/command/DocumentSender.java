package com.luckyhunterzzz.pandev_bot.command;

import org.telegram.telegrambots.meta.api.objects.InputFile;

/**
 * Интерфейс для отправки документов.
 *
 * Этот интерфейс определяет метод для отправки документов в чат.
 * Реализации этого интерфейса должны предоставлять конкретный способ отправки
 * документов через Telegram Bot API.
 */
public interface DocumentSender {
    /**
     * Отправляет документ в указанный чат.
     *
     * @param chatId идентификатор чата, в который будет отправлен документ
     * @param document объект {@link InputFile}, представляющий документ, который нужно отправить
     */
    void sendDocument(String chatId, InputFile document);
}