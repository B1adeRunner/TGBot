package ru.skynet.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class IncomingMessageAnalyser {
    @Autowired
    public IncomingMessageAnalyser(BotCommandsProcessor processor) {
        this.processor = processor;
    }

    private BotCommandsProcessor processor;

    public void analyse(Update message) {
        if (message != null && message.hasMessage()) {
            Message msg = message.getMessage();
            if (msg.isCommand()) {
                processor.commandProcess(msg);
            }

            if (msg.isChannelMessage()) {
            }

            if (msg.isGroupMessage()) {
            }

            if (msg.isReply()) {
            }

            if (msg.isSuperGroupMessage()) {
            }

            if (msg.isUserMessage()) {
                System.out.println("userMessage");
            }
        }
    }
}
