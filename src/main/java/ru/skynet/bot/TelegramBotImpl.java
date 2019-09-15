package ru.skynet.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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

@Slf4j
@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot {
    private final IncomingMessageAnalyser analyser;

    @Override
    public String getBotUsername() {
        return "AgentSmitBot";
        // HooliCorporationBot    AgentSmitBot
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived(Update message) {
        try {
            AnswerWrapper answer = analyser.analyse(message);
            //FIXME: костыль
            if(answer.getType().equals(AnswerType.BOT_API_METHOD)) {
                execute((BotApiMethod)answer.getAnswer());
            } else {
                execute((SendPhoto) answer.getAnswer());
            }
        } catch (TelegramApiException | NoAnswerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "465572557:AAFidxREQLbsPQk2Ic9xhcPniV4xT6N-sug";
        //hooliBot 465572557:AAFidxREQLbsPQk2Ic9xhcPniV4xT6N-sug
        // agentSmithBot 416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4
    }
}
