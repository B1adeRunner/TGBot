package ru.skynet.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skynet.bot.core.AnswerWrapper;
import ru.skynet.bot.core.NoAnswerException;

@Component
@Slf4j
public class IncomingMessageAnalyser {
    @Autowired
    public IncomingMessageAnalyser(BotCommandsProcessor processor) {
        this.processor = processor;
    }

    private BotCommandsProcessor processor;

    public AnswerWrapper analyse(Update message) throws NoAnswerException {
        if (message != null) {
            if(message.hasMessage()) {
                Message msg = message.getMessage();
                if (msg.isCommand()) {
                    return processor.commandProcess(message);
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
                }
            }
            if(message.hasCallbackQuery()) {
                return processor.commandProcess(message);
            }
        }
        throw new NoAnswerException();
    }
}
