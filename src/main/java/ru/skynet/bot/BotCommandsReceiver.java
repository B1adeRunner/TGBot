package ru.skynet.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO: разбить файл на несколько, согласно слоеной архитектуре
public class BotCommandsReceiver {
    private final Optional<TelegramBotImpl> telegramBot;

    @Autowired
    public BotCommandsReceiver(TelegramBotImpl telegramBot) {
        this.telegramBot = telegramBot;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botAPI = new TelegramBotsApi();
        try {
            //возможно нужно подставлять с апраметрами
            botAPI.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
