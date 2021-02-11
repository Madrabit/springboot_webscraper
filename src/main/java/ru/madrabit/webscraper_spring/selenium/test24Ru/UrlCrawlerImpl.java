package ru.madrabit.webscraper_spring.selenium.test24Ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.ScrapeTickets;
import ru.madrabit.webscraper_spring.selenium.UrlCrawlerBase;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.exceptions.NoSuchLetterException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
public class UrlCrawlerImpl extends UrlCrawlerBase {

    public UrlCrawlerImpl(SeleniumHandler seleniumHandler) {
        super(seleniumHandler);
    }

    @Override
    public Map<Enum<SiteLetters>, String> scrapeLetters() {
        Map<Enum<SiteLetters>, String> map = new LinkedHashMap<>();
        WebElement elements = seleniumHandler.getElement("#item1 > div > div");
        List<WebElement> links = elements.findElements(By.cssSelector(" a"));
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            String h4Text = link.getText();
            map.put(h4Cutter(h4Text), href);
        }
        log.info("Letters collected: {}", map.size());
        return map;
    }


    @Override
    public Map<String, String> scrapeSubTests() {
        Map<String, String> map = new LinkedHashMap<>();

        WebElement elements = seleniumHandler.getElement("#item1 > div > div");
        List<WebElement> links = elements.findElements(By.cssSelector(" a"));
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            String h4Text = link.getText();
            map.put(h4Text.substring(0, 7).replaceAll(" ", ""), href);
        }
        log.info("Second lvl Letters collected: {}", map.size());
        return map;
    }

    @Override
    public Map<String, List<String>> getTicketsUrlForA1(ScrapeTickets scrapeTickets) {
        Map<String, List<String>> tickets = new HashMap<>();
        List<String> ticketsList = scrapeTickets();
        tickets.put("A.1", ticketsList);
        log.info("Tickets A.1 collected and size is: {}", ticketsList.size());
        return tickets;
    }

    private List<String> scrapeTickets() {
        String LINKS = ".col-6.col-sm-6.col-md-3.col-lg-2 a";
        String ROW_WITH_TICKETS = ".row.text-center";
        WebElement elements = null;
        List<String> tickets = new ArrayList<>();
        elements = seleniumHandler.getElement(ROW_WITH_TICKETS);
        if (elements != null) {
            tickets = elements.findElements(By.cssSelector(LINKS)).stream().map(e -> e.getAttribute("href")).collect(toList());
        }
        return tickets;
    }

    private SiteLetters h4Cutter(String h4) {
        String letter = h4.substring(0, 1);
        String num = h4.substring(1, 3).trim();
        String result = "";
        if ("А".equals(letter)) {
            result = "A_1";
        } else if ("Б".equals(letter)) {
            result = "B_" + num;
        } else {
            try {
                throw new NoSuchLetterException("Wrong Letter selected");
            } catch (NoSuchLetterException e) {
                e.printStackTrace();
            }
        }
        return SiteLetters.valueOf(result);
    }



}
