package ru.skynet.bot.core;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BotCommand {
    private String name;
    private boolean isPrivate;
}
