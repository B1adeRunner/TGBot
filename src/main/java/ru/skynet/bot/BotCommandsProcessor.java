package ru.skynet.bot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.skynet.bot.service.boobs.OpenBoobsContent;
import ru.skynet.bot.service.boobs.OpenBoobsContentType;

import java.util.List;
import java.util.Map;

@Component
public class BotCommandsProcessor {
    private List botCommands;
    private List allCommands;
    private List privateUserCommands;
    private Map botPhrases;
    private List<OpenBoobsContent> boobsContent;
    private List<OpenBoobsContent> buttsContent;

    //TODO: вынести в конфиг
    private static final int MAX_BOOBS_PAGE_NUMBER = 648;
    private static final int MAX_BUTTS_PAGE_NUMBER = 332;

    public BotCommandsProcessor(@Qualifier("boobsContent")List<OpenBoobsContent> boobsContent,
                                @Qualifier("buttsContent")List<OpenBoobsContent> buttsContent,
                                @Qualifier("botCommands")List botCommands,
                                @Qualifier("allCommands")List allCommands,
                                @Qualifier("privateUserCommands")List privateUserCommands,
                                @Qualifier("botPhrases")Map botPhrases) {
        this.boobsContent = boobsContent;
        this.buttsContent = buttsContent;
        this.botCommands = botCommands;
        this.allCommands = allCommands;
        this.privateUserCommands = privateUserCommands;
        this.botPhrases = botPhrases;
    }

    public void commandProcess(Message message) {
        String receivedMessageText = message.getText() != null ? message.getText() : "";
        if (allCommands.contains(receivedMessageText)) {
            Boolean isNeedToAnswerUserPrivately = privateUserCommands.contains(receivedMessageText);
            createOpenBoobsContent(message);
            //String botAnswer = botPhrases.get(receivedMessageText) != null ? (String) botPhrases.get(receivedMessageText) : "¯|_(ツ)_/¯";
            //createAnswerMessage(message, botAnswer, isNeedToAnswerUserPrivately);
        } else if (receivedMessageText.startsWith("/")) {
            createAnswerMessage(message, "я не знаю этой команды ⊙︿⊙ ", false);
            //TODO: прикруть логер сюда и выводить то, что пришло в лог
        }
    }

    private OpenBoobsContentType defineContent(Message msg) {
        return msg.getText().toUpperCase().contains(OpenBoobsContentType.BOOBS.name()) ? OpenBoobsContentType.BOOBS : OpenBoobsContentType.BUTTS;
    }

    private OpenBoobsContent fetchContent(OpenBoobsContentType contentType) {
        return contentType.equals(OpenBoobsContentType.BOOBS) ? getRandomBoobs() : getRandomButts();
    }

    private OpenBoobsContent getRandomBoobs() {
        return boobsContent.get((int) (Math.random() * MAX_BOOBS_PAGE_NUMBER));
    }

    private OpenBoobsContent getRandomButts() {
        return buttsContent.get((int) (Math.random() * MAX_BUTTS_PAGE_NUMBER));
    }

    private SendPhoto createOpenBoobsContent(Message msg) {
        OpenBoobsContentType contentType = defineContent(msg);
        OpenBoobsContent content = fetchContent(contentType);
        SendPhoto photo = new SendPhoto();
        photo.setChatId(msg.getChatId());
        photo.setPhoto(content.getUrl());
        photo.setCaption("rank: " + content.getRank());
        return photo;
    }

    private SendMessage createAnswerMessage(Message message, String text, Boolean isNeedToAnswerUserPrivately) {
        //String forWhomID = isNeedToAnswerUserPrivately ? message.getFrom().getId().toString() : message.getChatId().toString();
        //System.out.println( "forWhomID = " + forWhomID );
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        //createAnswerMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText(text);
        return sendMessage;
    }

}
