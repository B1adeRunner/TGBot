package ru.skynet.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

@Component
public class TelegramBotImpl extends TelegramLongPollingBot {
    @Autowired
    public TelegramBotImpl(@Qualifier("botCommands") List botCommands,
                           @Qualifier("botPhrases") Map botPhrases,
                           IncomingMessageAnalyser analyser) {
        this.botCommands = botCommands;
        this.botPhrases = botPhrases;
        this.analyser = analyser;
    }

    private List botCommands;
    private Map botPhrases;
    private IncomingMessageAnalyser analyser;

    @Override
    public String getBotUsername() {
        return "HooliCorporationBot";
        //"AgentSmitBot";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdateReceived(Update message) {
        if (botCommands.size() != 0 && botPhrases.size() != 0) {
            analyser.analyse(message);
        }
    }

    @Override
    public String getBotToken() {
        return "465572557:AAFidxREQLbsPQk2Ic9xhcPniV4xT6N-sug";
        // token AgentSmitBot
        // "416583838:AAHzstBVDp5dUndmvqesXY0cbvPSY3Gu3R4";
    }
}
