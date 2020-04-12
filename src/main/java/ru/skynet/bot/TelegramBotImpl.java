package ru.skynet.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.skynet.bot.core.AnswerType;
import ru.skynet.bot.core.AnswerWrapper;
import ru.skynet.bot.core.NoAnswerException;

import java.util.Properties;

@Slf4j
public class TelegramBotImpl extends TelegramLongPollingBot {
    private final IncomingMessageAnalyser analyser;
    private Properties settings;

    @Autowired
    public TelegramBotImpl(IncomingMessageAnalyser analyser,
                           Properties settings) {
        this.analyser = analyser;
        this.settings = settings;
    }

    @Override
    public String getBotUsername() {
        return settings.getProperty("botUsername");
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived(Update message) {
        try {
            AnswerWrapper answer = analyser.analyse(message);
            //FIXME: костыль
            if (answer.getType().equals(AnswerType.BOT_API_METHOD)) {
                execute((BotApiMethod) answer.getAnswer());
            } else {
                execute((SendPhoto) answer.getAnswer());
            }
        } catch (TelegramApiException | NoAnswerException e) {
            log.error("Message received error, ", e);
        }
    }

    @Override
    public String getBotToken() {
        return settings.getProperty("botToken");
    }
}
