package com.luckyhunterzzz.pandev_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения PandevBot.
 *
 * Этот класс является точкой входа в приложение и запускает его с помощью Spring Boot.
 * Аннотация {@link SpringBootApplication} включает в себя конфигурацию Spring,
 * автоматическую настройку и сканирование компонентов.
 */
@SpringBootApplication
public class PandevBotApplication {

    /**
     * Точка входа в приложение..
     *
     * @param args аргументы командной строки, переданные при запуске приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(PandevBotApplication.class, args);
    }

}
