package ru.skynet.bot;

import org.telegram.telegrambots.meta.api.objects.Message;

public class BotCommandsParser {

    private void parsingMessage(Message message) {
        if (message.hasDocument()) {
            String sql = String.join("", "");
        } else if (message.hasEntities()) {
        } else if (message.hasInvoice()) {
        } else if (message.hasLocation()) {
        } else if (message.hasPhoto()) {
        } else if (message.hasSuccessfulPayment()) {
        } else if (message.hasText()) {
        }
    }
}
