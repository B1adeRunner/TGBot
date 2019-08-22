package ru.skynet.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.skynet.bot.service.boobs.OpenBoobsContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan
@EnableScheduling
@Configuration
public class ApplicationConfig {
    @Bean
    public List<OpenBoobsContent> boobsContent() {
        return new ArrayList<>();
    }

    @Bean
    public List<OpenBoobsContent> buttsContent() {
        return new ArrayList<>();
    }

    @Bean
    public List allCommands() {
        return new ArrayList();
    }

    @Bean
    public List privateUserCommands() {
        return new ArrayList();
    }

    @Bean
    public Map botPhrases() {
        return new HashMap();
    }

    @Bean
    public List botCommands() {
        return new ArrayList();
    }
}
