package ru.skynet.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import ru.skynet.bot.service.boobs.OpenBoobsContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@ComponentScan
@EnableScheduling
@Configuration
public class ApplicationConfig {
    private BotSession botSession;

    @Bean
    public List<OpenBoobsContent> boobsContent() {
        return new ArrayList<>();
    }

    @Bean
    public List<OpenBoobsContent> buttsContent() {
        return new ArrayList<>();
    }

    @Bean
    public List<String> allCommands() {
        return new ArrayList<>();
    }

    @Bean
    public List<String> privateUserCommands() {
        return new ArrayList<>();
    }

    @Bean
    public Map<String, String> botPhrases() {
        return new HashMap<>();
    }

    @Bean
    public List<Map<String, List<String>>> botCommands() {
        return new ArrayList<>();
    }

    @Bean
    public List<String> permissions() {
        return new ArrayList<>();
    }

    @Bean
    public Properties settings() {
        return new Properties();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public void initAll() {
        BotCommandsProcessor processor = new BotCommandsProcessor(boobsContent(), buttsContent(),
                allCommands(), privateUserCommands(), botPhrases(), permissions());
        IncomingMessageAnalyser analyser = new IncomingMessageAnalyser(processor);
        TelegramBotsApi botAPI = new TelegramBotsApi();
        try {
            if (botSession == null) {
                botSession = botAPI.registerBot(new TelegramBotImpl(analyser));
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
