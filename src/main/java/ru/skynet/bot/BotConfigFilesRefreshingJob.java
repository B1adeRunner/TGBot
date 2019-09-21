package ru.skynet.bot;

import lombok.extern.slf4j.Slf4j;
import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class BotConfigFilesRefreshingJob implements SchedulingConfigurer {
    public BotConfigFilesRefreshingJob(@Qualifier("permissions") List<String> permissions,
                                       @Qualifier("allCommands") List<String> allCommands,
                                       @Qualifier("privateUserCommands") List<String> privateUserCommands,
                                       @Qualifier("botPhrases") Map<String, String> botPhrases,
                                       @Qualifier("settings") Properties settings) {
        this.allCommands = allCommands;
        this.permissions = permissions;
        this.privateUserCommands = privateUserCommands;
        this.botPhrases = botPhrases;
        this.settings = settings;
    }

    private volatile List<Map<String, List<String>>> botCommands;
    private volatile List<String> permissions;
    private volatile List<String> allCommands;
    private volatile List<String> privateUserCommands;
    private volatile Map<String, String> botPhrases;
    private volatile Properties settings;

    @Scheduled(fixedDelay = 1000000000000000000L)
    public void updateConfigurations() {
        try (FileInputStream fileInputStream = new FileInputStream("resources/settings.properties")) {
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
            settings.clear();
            settings.load(fileInputStream);
            log.info("End of bot configuration parsing");
        } catch (IOException e) {
            log.error("Error while configuration parsing, " + e);
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                this::updateConfigurations,
                (triggerContext) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, Integer.parseInt(settings.getProperty("settingsRefreshingDelayInMinutes", "10")));
                    log.info("Next settings refresh will be at {}", cal.getTime());
                    return cal.getTime();
                });
    }
}
