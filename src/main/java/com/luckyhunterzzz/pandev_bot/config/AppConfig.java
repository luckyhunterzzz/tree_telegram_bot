package com.luckyhunterzzz.pandev_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Конфигурационный класс для настройки ботов Telegram.
 *
 * Этот класс содержит методы для создания и настройки бинов, необходимых для работы
 * с Telegram Bot API. Он предоставляет экземпляр {@link TelegramBotsApi}, который
 * используется для регистрации ботов.
 */
@Configuration
public class AppConfig {

    /**
     * Создает и возвращает экземпляр {@link TelegramBotsApi}.
     *
     * @return экземпляр {@link TelegramBotsApi} для работы с Telegram Bot API
     * @throws TelegramApiException если возникает ошибка при создании экземпляра
     */
    @Bean
    TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }
}