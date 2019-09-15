package ru.skynet.bot;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class BotConfigFilesRefreshingJob {
    private static final long MILISECONDS_PER_MINUTE = 60000;

    public BotConfigFilesRefreshingJob(@Qualifier("permissions") List<String> permissions,
                                       @Qualifier("allCommands") List<String> allCommands,
                                       @Qualifier("privateUserCommands") List<String> privateUserCommands,
                                       @Qualifier("botPhrases") Map<String, String> botPhrases) {
        this.allCommands = allCommands;
        this.permissions = permissions;
        this.privateUserCommands = privateUserCommands;
        this.botPhrases = botPhrases;
    }

    private volatile List<Map<String, List<String>>> botCommands;
    private volatile List<String> permissions;
    private volatile List<String> allCommands;
    private volatile List<String> privateUserCommands;
    private volatile Map<String, String> botPhrases;

    @Scheduled(fixedDelay = MILISECONDS_PER_MINUTE, initialDelay = 0)
    public void updateBotCommands() {
        try {
            log.info("Start of bot configuration parsing");
            //TODO: вынести путь файлов в jvm переменные
            botCommands = Yaml.loadType(new File("resources/botCommands.yml"), ArrayList.class);
            botPhrases.putAll(Yaml.loadType(new File("resources/botPhrases.yml"), HashMap.class));
            permissions.clear();
            permissions.addAll((List<String>) Yaml.loadType(new File("resources/permissions.yml"), ArrayList.class));
            permissions.forEach(x -> log.debug(String.valueOf(x)));
            List<String> tmpAllCmd = botCommands.get(0).get("allCommands");
            allCommands.clear();
            allCommands.addAll(tmpAllCmd);
            privateUserCommands.addAll((List<String>) (((Map) (botCommands.get(0))).get("privateUserCommands")));
            log.info("End of bot configuration parsing");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
