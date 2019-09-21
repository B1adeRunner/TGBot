package ru.skynet.bot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skynet.bot.core.AnswerType;
import ru.skynet.bot.core.AnswerWrapper;
import ru.skynet.bot.core.PrimitiveContent;
import ru.skynet.bot.service.boobs.ContentType;
import ru.skynet.bot.service.boobs.OpenBoobsContent;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import static ru.skynet.bot.core.PrimitiveContent.boobsPrimitiveContent;
import static ru.skynet.bot.core.PrimitiveContent.buttsPrimitiveContent;

@Component
public class BotCommandsProcessor {
    private volatile List allCommands;
    private volatile List privateUserCommands;
    private volatile Map botPhrases;
    private volatile List<OpenBoobsContent> boobsContent;
    private volatile List<OpenBoobsContent> buttsContent;
    private volatile List<String> permissions;
    private volatile Properties settings;
    private ArrayBlockingQueue<String> openBoobsQueue;
    private ArrayBlockingQueue<String> openButtsQueue;
    private ArrayBlockingQueue<String> boobsQueue;
    private ArrayBlockingQueue<String> buttsQueue;

    public BotCommandsProcessor(@Qualifier("boobsContent") List<OpenBoobsContent> boobsContent,
                                @Qualifier("buttsContent") List<OpenBoobsContent> buttsContent,
                                @Qualifier("allCommands") List allCommands,
                                @Qualifier("privateUserCommands") List privateUserCommands,
                                @Qualifier("botPhrases") Map botPhrases,
                                @Qualifier("permissions") List<String> permissions,
                                @Qualifier("settings") Properties settings) {
        this.boobsContent = boobsContent;
        this.buttsContent = buttsContent;
        this.allCommands = allCommands;
        this.privateUserCommands = privateUserCommands;
        this.botPhrases = botPhrases;
        this.permissions = permissions;
        this.settings = settings;
        int bufferCapacity = Integer.parseInt(settings.getProperty("bufferCapacity"));
        openBoobsQueue = new ArrayBlockingQueue<>(bufferCapacity);
        openButtsQueue = new ArrayBlockingQueue<>(bufferCapacity);
        boobsQueue = new ArrayBlockingQueue<>(bufferCapacity);
        buttsQueue = new ArrayBlockingQueue<>(bufferCapacity);
    }

    public AnswerWrapper commandProcess(Update message) {
        boolean isVipChat = message.getMessage().getChat() != null &&
                message.getMessage().getChat().getTitle() != null &&
                permissions.contains(message.getMessage().getChat().getTitle());
        String receivedMessageText = message.getMessage().getText() != null ? message.getMessage().getText() : "";
        if (allCommands.contains(receivedMessageText)) {
            if (isVipChat && !boobsContent.isEmpty() && !buttsContent.isEmpty()) {
                return new AnswerWrapper(AnswerType.PARTIAL_BOT_API_METHOD,
                                         sendOpenBoobsContent(message.getMessage()));
            } else {
                return new AnswerWrapper(AnswerType.BOT_API_METHOD,
                                         sendPrimitiveContent(message.getMessage()));
            }
        }
        return new AnswerWrapper(AnswerType.BOT_API_METHOD,
                                 sendMessage(message.getMessage(), "i dont know this command", false));
    }

    private ContentType definePrimitiveContent(Message msg) {
        return msg.getText().toUpperCase().contains(ContentType.BOOBS.name()) ? ContentType.BOOBS : ContentType.BUTTS;
    }

    private OpenBoobsContent fetchContent(ContentType contentType) {
        if (contentType.equals(ContentType.BOOBS)) {
            return defineContent(boobsContent, openBoobsQueue);
        } else {
            return defineContent(buttsContent, openButtsQueue);
        }
    }

    private PrimitiveContent fetchPrimitiveContent(ContentType contentType) {
        String val;
        if (contentType.equals(ContentType.BOOBS)) {
            val = definePrimitiveContent(boobsPrimitiveContent, boobsQueue);
        } else {
            val = definePrimitiveContent(buttsPrimitiveContent, buttsQueue);
        }
        return new PrimitiveContent(val.split(" - ")[1], val.split(" - ")[0], val);
    }

    private <T> T getRandomValue(List<T> content, int maxvalueNumber) {
        return content.get((int) (Math.random() * maxvalueNumber));
    }

    private String definePrimitiveContent(List<String> content, ArrayBlockingQueue<String> queue) {
        String val;
        do {
            val = getRandomValue(content, content.size() - 1);
        } while (queue.contains(val));

        if (!queue.offer(val)) {
            queue.poll();
            queue.offer(val);
        }
        return val;
    }

    private OpenBoobsContent defineContent(List<OpenBoobsContent> content, ArrayBlockingQueue<String> queue) {
        OpenBoobsContent val;
        do {
            val = getRandomValue(content, content.size() - 1);
        } while (queue.contains(val.getUrl()));

        if (!queue.offer(val.getUrl())) {
            queue.poll();
            queue.offer(val.getUrl());
        }
        return val;
    }

    private PartialBotApiMethod sendOpenBoobsContent(Message msg) {
        ContentType contentType = definePrimitiveContent(msg);
        OpenBoobsContent content = fetchContent(contentType);
        SendPhoto photo = new SendPhoto();
        photo.setChatId(msg.getChatId());
        photo.setPhoto(content.getUrl());
        photo.setCaption("rank: " + content.getRank());
        return photo;
    }

    private PartialBotApiMethod sendPrimitiveContent(Message msg) {
        ContentType contentType = definePrimitiveContent(msg);
        PrimitiveContent content = fetchPrimitiveContent(contentType);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(content.getFullContentMessage());
        return sendMessage;
    }

    private PartialBotApiMethod sendMessage(Message message, String text, boolean isNeedToAnswerUserPrivately) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }

}
