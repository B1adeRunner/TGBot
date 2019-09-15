package ru.skynet.bot.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Вспомогательная сущность для ответа пользователю
 */
@Getter
@Setter
@AllArgsConstructor
public class AnswerWrapper {
    private AnswerType type;
    private Object answer;
}
