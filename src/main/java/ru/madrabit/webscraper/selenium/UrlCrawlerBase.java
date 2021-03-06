package ru.madrabit.webscraper.selenium;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;

import java.util.*;

@Slf4j
public abstract class UrlCrawlerBase implements UrlCrawler {

    protected final SeleniumHandler seleniumHandler;

    public UrlCrawlerBase(SeleniumHandler seleniumHandler) {
        this.seleniumHandler = seleniumHandler;
    }

    @Override
    public abstract Map<Enum<SiteLetters>, String> scrapeLetters();


    @Override
    public abstract Map<String, String> scrapeSubTests();

    @Override
    public Map<String, List<String>> getTicketsUrl(Map<String, String> subTests) {
        Map<String, List<String>> tickets = new HashMap<>();
        for (Map.Entry<String, String> entry : subTests.entrySet()) {
            moveToUrl(entry.getValue());
            List<String> ticketsList = scrapeTickets();
            tickets.put(entry.getKey(), ticketsList);
        }
        log.info("Tickets collected: {}", tickets.size());
        return tickets;
    }

    @Override
    public Map<String, List<String>> pushTicketsToA1(List<String> ticketsList) {
        Map<String, List<String>> tickets = new HashMap<>();
//        List<String> ticketsList = scrapeTickets();
        tickets.put("A.1", ticketsList);
        log.info("Tickets A.1 collected and size is: {}", ticketsList.size());
        return tickets;
    }

    public abstract List<String> scrapeTickets();

    public void moveToUrl(String url) {
        try {
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }

}
