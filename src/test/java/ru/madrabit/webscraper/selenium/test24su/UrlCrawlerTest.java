package ru.madrabit.webscraper.selenium.test24su;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.ElementsConstTest24Ru;
import ru.madrabit.webscraper.selenium.consts.ElementsConstTest24Su;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.test24su.UrlCrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UrlCrawlerTest {

    private SeleniumHandler seleniumHandler;
    private UrlCrawler urlCrawler;

    @BeforeEach
    public void init() {
        seleniumHandler = new SeleniumHandler();
        seleniumHandler.start(true);
        urlCrawler = new UrlCrawler(seleniumHandler);
    }

    @Test
    void scrapeLetters() {
        seleniumHandler.openPage(ElementsConstTest24Su.START_URL);
        Map<Enum<SiteLetters>, String> letters = urlCrawler.scrapeLetters();
        for (Map.Entry<Enum<SiteLetters>, String> entry : letters.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        Assert.assertEquals(true, letters.size() > 0);
    }

    @Test
    void scrapeSubTests() {
        Map<Enum<SiteLetters>, String> letters = new HashMap<>();
        letters.put(SiteLetters.B_1, "https://tests24.su/b1-trebovaniya-promyshlennoj-bezopasnosti-v-himicheskoj-neftehimicheskoj-i-neftepererabatyvayushhej-promyshlennosti/");
        seleniumHandler.openPage(letters.get(SiteLetters.B_1));
        Map<String, String> subTests = urlCrawler.scrapeSubTests();
        for (Map.Entry<String, String> entry : subTests.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        Assert.assertEquals(true, subTests.size() > 0);
    }

    @Test
    void scrapeTickets() {
        seleniumHandler.openPage(ElementsConstTest24Su.A_TICKETS);
        final List<String> tickets = urlCrawler.scrapeTickets();
        for (String ticket : tickets) {
            System.out.println(ticket);
        }
        Assert.assertEquals(true, tickets.size() > 0);
    }

    @AfterEach
    public void tearDown() {
        seleniumHandler.stop();
    }
}
