import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenContentParser {
    private String commonContentPageUrl;
    private Document commonContentPage;
    private List<OpenBoobsContent> boobsContent;
    private List<OpenBoobsContent> buttsContent;

    private static final String KEY_WORD = "iline";
    private static final int MAX_BOOBS_PAGE_NUMBER = 648;
    private static final int MAX_BUTTS_PAGE_NUMBER = 332;
    private static final int MIN_CONTENT_RANK = 40;

    private static final String BOOBS_URL_PART = "http://oboobs.ru/";
    private static final String BUTTS_URL_PART = "http://obutts.ru/";

    public List<OpenBoobsContent> getBoobsContent() {
        return boobsContent;
    }

    public List<OpenBoobsContent> getButtsContent() {
        return buttsContent;
    }

    public static void main(String[] args) {
        OpenContentParser parser = new OpenContentParser();
        parser.doParseButts();
        parser.doParseBoobs();
    }

    public void doParseBoobs() {
        boobsContent = new ArrayList<>();
        System.out.println("Boobs data fetching start");
        doParse(MAX_BOOBS_PAGE_NUMBER, boobsContent, BOOBS_URL_PART);
        System.out.println("Boobs data fetching end");
    }

    public void doParseButts() {
        buttsContent = new ArrayList<>();
        System.out.println("Butts data fetching start");
        doParse(MAX_BUTTS_PAGE_NUMBER, buttsContent, BUTTS_URL_PART);
        System.out.println("Butts data fetching end");
    }

    private void doParse(int pageMaxNumber, List<OpenBoobsContent> contents, String contentUrlPart) {
        for (int commonPageNum = 1; commonPageNum <= pageMaxNumber; commonPageNum++) {
            if (commonPageNum % 100 == 0) {
                System.out.println(commonPageNum + " pages processed");
            }
            defineCommonContentPageUrl(contentUrlPart, commonPageNum);
            defineCommonContentPageList();
            for (Element element : getPageElementsByKey(KEY_WORD)) {
                if (MIN_CONTENT_RANK <= getContentRank(element)) {
                    contents.add(OpenBoobsContent.builder()
                            .url(getConcretePageAddress(element))
                            .type(contentUrlPart.contains("boobs") ? ContentType.BOOBS : ContentType.BUTTS)
                            .rank(getContentRank(element))
                            .build());
                }
            }
        }
        contents.forEach(content -> System.out.println("content: " +
                content.getType() + ", " +
                content.getUrl() + ", " +
                content.getRank()));
    }

    private String getConcretePageAddress(Element element) {
        return element.getElementsByTag("a").get(2)
                .getElementsByAttribute("src")
                .attr("src");
    }

    private int getContentRank(Element element) {
        return Integer.valueOf(element.childNodes().get(1).childNode(3).childNode(0).toString().split("\n")[1]);
    }

    private void defineCommonContentPageUrl(String contentUrlPart, int pageNum) {
        commonContentPageUrl = contentUrlPart.concat(String.valueOf(pageNum)).concat("/e/");
    }

    private void defineCommonContentPageList() {
        try {
            commonContentPage = Jsoup.connect(commonContentPageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Elements getPageElementsByKey(String KEY_WORD) {
        return commonContentPage.getElementsByClass(KEY_WORD);
    }

}
