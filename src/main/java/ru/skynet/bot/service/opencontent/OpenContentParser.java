package ru.skynet.bot.service.opencontent;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class OpenContentParser implements SchedulingConfigurer {
    public OpenContentParser(@Qualifier("buttsContent") List<OpenBoobsContent> buttsContent,
                             @Qualifier("boobsContent") List<OpenBoobsContent> boobsContent,
                             @Qualifier("settings") Properties settings) {
        this.boobsContent = boobsContent;
        this.buttsContent = buttsContent;
        this.settings = settings;
    }

    private List<OpenBoobsContent> buttsContent;
    private List<OpenBoobsContent> boobsContent;
    private Properties settings;

    private static final String KEY_WORD = "iline";
    private static final String BOOBS_URL_PART = "http://oboobs.ru/";
    private static final String BUTTS_URL_PART = "http://obutts.ru/";

    @Scheduled(fixedDelay = 1000000000000000000L)
    public void doParseBoobs() {
        if (!settings.isEmpty()) {
            log.info("Boobs data fetching start");
            doParse(Integer.parseInt(settings.getProperty("maxBoobsPageNumber")), boobsContent, BOOBS_URL_PART);
            log.info("Boobs data fetching end");
        }
    }

    @Scheduled(fixedDelay = 1000000000000000000L)
    public void doParseButts() {
        if (!settings.isEmpty()) {
            log.info("Butts data fetching start");
            doParse(Integer.parseInt(settings.getProperty("maxButtsPageNumber")), buttsContent, BUTTS_URL_PART);
            log.info("Butts data fetching end");
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> {
                    this.doParseBoobs();
                    this.doParseButts();
                },
                (triggerContext) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MINUTE, Integer.parseInt(settings.getProperty("openContentFetchingDelayInMinutes", "10")));
                    log.info("Next settings open content fetching will be at {}", cal.getTime());
                    return cal.getTime();
                });
    }

    private void doParse(int pageMaxNumber, List<OpenBoobsContent> contents, String contentUrlPart) {
        for (int commonPageNum = 1; commonPageNum <= pageMaxNumber; commonPageNum++) {
            if (commonPageNum % 100 == 0) {
                log.info("Content type: " + contents.get(0).getType() + ". " + commonPageNum + " pages processed");
            }
            Document commonContentPage = defineCommonContentPageList(defineCommonContentPageUrl(contentUrlPart, commonPageNum));
            for (Element element : getPageElementsByKey(commonContentPage)) {
                if (Integer.parseInt(settings.getProperty("minContentRank")) <= getContentRank(element)) {
                    contents.add(OpenBoobsContent.builder()
                                         .id(getContentId(element))
                                         .url(getConcretePageAddress(element))
                                         .type(contentUrlPart.contains("boobs") ? ContentType.BOOBS : ContentType.BUTTS)
                                         .rank(getContentRank(element))
                                         .build());
                }
            }
        }
        contents.forEach(content -> log.debug(content.toString()));
    }

    private String getConcretePageAddress(Element element) {
        return element.getElementsByTag("a").get(2)
                .getElementsByAttribute("src")
                .attr("src");
    }

    private int getContentRank(Element element) {
        return Integer.parseInt(element.childNodes().get(1).childNode(3)
                                        .childNode(0).toString().split("\n")[1]);
    }

    private int getContentId(Element element) {
        return Integer.parseInt(element.getElementsByTag("a").get(2)
                                        .getElementsByAttribute("href")
                                        .attr("href")
                                        .split("/")[2]);
    }

    private String defineCommonContentPageUrl(String contentUrlPart, int pageNum) {
        return contentUrlPart.concat(String.valueOf(pageNum)).concat("/e/");
    }

    private Document defineCommonContentPageList(String commonContentPageUrl) {
        try {
            return Jsoup.connect(commonContentPageUrl).get();
        } catch (IOException e) {
            log.error("Something wrong with data fetching, " + e);
            //TODO: придумать как тут получше обработать кейс
            return new Document("");
        }
    }

    private Elements getPageElementsByKey(Document commonContentPage) {
        return commonContentPage.getElementsByClass(KEY_WORD);
    }
}
