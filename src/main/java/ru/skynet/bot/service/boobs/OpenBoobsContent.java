package ru.skynet.bot.service.boobs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class OpenBoobsContent {
    private int id;
    private String url;
    private int rank;
    private ContentType type;
}
