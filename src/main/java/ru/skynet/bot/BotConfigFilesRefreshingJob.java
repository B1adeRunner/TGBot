package ru.skynet.bot;

import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotConfigFilesRefreshingJob {
    public BotConfigFilesRefreshingJob(@Qualifier("botCommands") List botCommands,
                                       @Qualifier("allCommands") List allCommands,
                                       @Qualifier("privateUserCommands") List privateUserCommands,
                                       @Qualifier("botPhrases") Map botPhrases) {
        this.botCommands = botCommands;
        this.allCommands = allCommands;
        this.privateUserCommands = privateUserCommands;
        this.botPhrases = botPhrases;
    }

    private List botCommands;
    private List allCommands;
    private List privateUserCommands;
    private Map botPhrases;

    @Scheduled(fixedDelay = 100000)
    private void updateBotCommands() {
        try {
            //TODO: вынести путь файлов в jvm переменные
            botCommands = Yaml.loadType(new File("src/resources/botCommands.yml"), ArrayList.class);
            botPhrases = Yaml.loadType(new File("src/resources/botPhrases.yml"), HashMap.class);
            allCommands = (List) (((Map) (botCommands.get(0))).get("allCommands"));
            privateUserCommands = (List) (((Map) (botCommands.get(0))).get("privateUserCommands"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
