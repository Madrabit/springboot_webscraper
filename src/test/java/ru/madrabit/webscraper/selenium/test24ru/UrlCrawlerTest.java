package ru.madrabit.webscraper.selenium.test24ru;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.ElementsConstTest24Ru;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;

import java.util.*;

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
    public void whenScrapeTicketsThenSizeNotEmpty() {
        seleniumHandler.openPage(ElementsConstTest24Ru.A_TICKETS);
        final List<String> tickets = urlCrawler.scrapeTickets();
        Assert.assertEquals(true, tickets.size() > 0);
    }

    @Test
    public void whenGetTicketsUrlForA1() {
        String url = "https://tests24.ru/?iter=4&bil=1&test=726";
        final Map<String, List<String>> ticketsMap = urlCrawler.pushTicketsToA1(Arrays.asList(url));
        Assert.assertEquals(true, ticketsMap.containsKey("A.1"));
        Assert.assertEquals(url, ticketsMap.get("A.1").get(0));
    }

    @Test
    public void whenScrapeLettersThenSizeNotNull() {
        seleniumHandler.openPage(ElementsConstTest24Ru.START_URL);
        Map<Enum<SiteLetters>, String> letters = urlCrawler.scrapeLetters();
        for (Map.Entry<Enum<SiteLetters>, String> entry : letters.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        Assert.assertEquals(true, letters.size() > 0);
    }

    @Test
    public void whenScrapeSubTestB_1ThenSizeNotNull() {
        Map<Enum<SiteLetters>, String> letters = new HashMap<>();
        letters.put(SiteLetters.B_1, "https://tests24.ru/?iter=2&group=11");
        seleniumHandler.openPage(letters.get(SiteLetters.B_1));
        Map<String, String> subTests = urlCrawler.scrapeSubTests();
        for (Map.Entry<String, String> entry : subTests.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        Assert.assertEquals(true, subTests.size() > 0);
    }

    @AfterEach
    public void tearDown() {
        seleniumHandler.stop();
    }



}
