package ru.skynet.bot.service.boobs;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class OpenContentParser {
    private static final long MILISECONDS_PER_MINUTE = 60000;

    @Autowired
    public OpenContentParser(@Qualifier("buttsContent")List<OpenBoobsContent> buttsContent,
                             @Qualifier("boobsContent")List<OpenBoobsContent> boobsContent) {
        this.boobsContent = boobsContent;
        this.buttsContent = buttsContent;
    }

    private List<OpenBoobsContent> buttsContent;
    private List<OpenBoobsContent> boobsContent;

    //TODO: вынести эти настройки в конфиг файл
    private static final String KEY_WORD = "iline";
    private static final int MAX_BOOBS_PAGE_NUMBER = 648;
    private static final int MAX_BUTTS_PAGE_NUMBER = 332;
    private static final int MIN_CONTENT_RANK = 40;
    private static final String BOOBS_URL_PART = "http://oboobs.ru/";
    private static final String BUTTS_URL_PART = "http://obutts.ru/";

    @Scheduled(fixedDelay = MILISECONDS_PER_MINUTE * 60 * 12, initialDelay = 0)
    public void doParseBoobs() {
        log.info("Boobs data fetching start");
        doParse(MAX_BOOBS_PAGE_NUMBER, boobsContent, BOOBS_URL_PART);
        log.info("Boobs data fetching end");
    }

    @Scheduled(fixedDelay = MILISECONDS_PER_MINUTE * 60 * 12, initialDelay = 0)
    public void doParseButts() {
        log.info("Butts data fetching start");
        doParse(MAX_BUTTS_PAGE_NUMBER, buttsContent, BUTTS_URL_PART);
        log.info("Butts data fetching end");
    }

    private void doParse(int pageMaxNumber, List<OpenBoobsContent> contents, String contentUrlPart) {
        for (int commonPageNum = 1; commonPageNum <= pageMaxNumber; commonPageNum++) {
            if (commonPageNum % 100 == 0) {
                log.info(commonPageNum + " pages processed");
            }
            Document commonContentPage = defineCommonContentPageList(defineCommonContentPageUrl(contentUrlPart, commonPageNum));
            for (Element element : getPageElementsByKey(commonContentPage)) {
                if (MIN_CONTENT_RANK <= getContentRank(element)) {
                    contents.add(OpenBoobsContent.builder()
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
