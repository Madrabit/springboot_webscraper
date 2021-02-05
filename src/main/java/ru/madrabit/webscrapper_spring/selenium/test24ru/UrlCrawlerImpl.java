package ru.madrabit.webscrapper_spring.selenium.test24ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscrapper_spring.selenium.UrlCrawler;
import ru.madrabit.webscrapper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.exceptions.NoSuchLetterException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
public class UrlCrawlerImpl implements UrlCrawler {

    private final SeleniumHandler seleniumHandler;

    public UrlCrawlerImpl(SeleniumHandler seleniumHandler) {
        this.seleniumHandler = seleniumHandler;
    }

    @Override
    public Map<Enum<SiteLetters>, String> scrapeLetters() {
        Map<Enum<SiteLetters>, String> map = new HashMap<>();
        List<WebElement> elements = seleniumHandler.getElement("#panel-1201-0-0-1 ").findElements(By.cssSelector(" a"));
        elements.remove(0); // First one is uniq, should not parse in cycle.
        elements.remove(elements.size() - 1); //remove excess last one test.
        for (WebElement e : elements) {
            String href = e.getAttribute("href");
            try {
                map.put(getTestNameFromUrl(href), href);
            } catch (NoSuchLetterException nsl) {
                log.error(nsl.getMessage(), nsl);
            }
        }
        log.info("Letters collected: {}", map.size());
        return map;
    }

    @Override
    public Map<String, String> scrapeSubTests() {
        Map<String, String> map = new LinkedHashMap<>();
        for (WebElement e : seleniumHandler.getElement(".siteorigin-widget-tinymce.textwidget:nth-child(2)")
                .findElements(By.cssSelector("div > blockquote > p > strong > a"))) {
            String href = e.getAttribute("href");
            if (href.contains("proverka")) break; // exclude last test, which doesn't need
            try {
                map.put(getTestNameFromTicketUrl(href), href);
            } catch (NoSuchLetterException nsl) {
                log.error(nsl.getMessage(), nsl);
            }
        }
        log.info("Second lvl Letters collected: {}", map.size());
        return map;
    }

    @Override
    public Map<String, List<String>> getTicketsUrl(Map<String, String> subTests) {
        Map<String, List<String>> tickets = new HashMap<>();
        for (Map.Entry<String, String> entry : subTests.entrySet()) {


            moveToUrl(entry.getValue());
//            String id = getTestNameFromUrl(subTests.get(j));
            List<String> ticketsList = scrapeTickets();

//            log.info("Tickets collected: {}", ticketsList.size());
            tickets.put(entry.getKey(), ticketsList);
        }
        log.info("Tickets collected: {}", tickets.size());
        return tickets;
    }

    @Override
    public Map<String, List<String>> getTicketsUrlForA1() {
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

    public void moveToUrl(String url) {
        try {
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }

    private static Enum<SiteLetters> getTestNameFromUrl(String url) throws NoSuchLetterException {
        String s = url.substring(19);
        String[] splitted = s.split("-");
        String result = "";
        // if letter and number glued like "b1"
        if (splitted[0].length() > 1) {
            return SiteLetters.valueOf(splitted[0].substring(0, 1).toUpperCase()
                    + "_"
                    + splitted[0].substring(1, 2));
        }
        switch (splitted[0]) {
            case "b":
                result = "B_" + splitted[1];
                break;
            case "d":
                result = "D";
                break;
            default:
                throw new NoSuchLetterException("Wrong Letter selected");
        }
        return SiteLetters.valueOf(result);
    }

    private String getTestNameFromTicketUrl(String url) throws NoSuchLetterException {
        String s = url.substring(19);
        String[] splitted = s.split("-");
        String result = "";
        switch (splitted[0]) {
            case "b":
                result = "Б." + splitted[1] + "." + splitted[2];
                break;
            case "d":
                result = "Д." + splitted[1];
                break;
            default:
                throw new NoSuchLetterException("Wrong Letter selected");
        }
        return result;
    }
}
