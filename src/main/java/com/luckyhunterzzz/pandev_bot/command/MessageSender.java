package com.luckyhunterzzz.pandev_bot.command;

/**
 * Интерфейс для отправки текстовых сообщений.
 *
 * Этот интерфейс определяет метод для отправки текстовых сообщений в чат.
 * Реализации этого интерфейса должны предоставлять конкретный способ отправки
 * сообщений через Telegram Bot API.
 */
public interface MessageSender {
    /**
     * Отправляет текстовое сообщение в указанный чат.
     *
     * @param chatId идентификатор чата, в который будет отправлено сообщение
     * @param text   текст сообщения, которое нужно отправить
     */
    void sendMessage(String chatId, String text);
}