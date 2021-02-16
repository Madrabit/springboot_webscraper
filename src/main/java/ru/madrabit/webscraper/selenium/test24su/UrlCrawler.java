package ru.madrabit.webscraper.selenium.test24su;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper.selenium.UrlCrawlerBase;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.exceptions.NoSuchLetterException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
public class UrlCrawler extends UrlCrawlerBase {

    private static final String LINKS = "div p a ";
    private static final String ROW_CHILD_2 = ".panel-grid.panel-no-style:nth-child(2)";
    private static final String ROW_CHILD_3 = ".panel-grid.panel-no-style:nth-child(3)";

    public UrlCrawler(SeleniumHandler seleniumHandler) {
        super(seleniumHandler);
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
            if (href.contains("proverka")) {
                break; // exclude last test, which doesn't need
            }
            try {
                map.put(getTestNameFromTicketUrl(href), href);
            } catch (NoSuchLetterException nsl) {
                log.error(nsl.getMessage(), nsl);
            }
        }
        log.info("Second lvl Letters collected: {}", map.size());
        return map;
    }

    public List<String> scrapeTickets() {
        WebElement firstRow = seleniumHandler.getElement(ROW_CHILD_2 + " > div > " + LINKS);
        WebElement secondRow = seleniumHandler.getElement(ROW_CHILD_3 + " > div > " + LINKS);
        WebElement elements = null;
        List<String> tickets = new ArrayList<>();
        if (firstRow.getText().contains("БИЛЕТ")) {
            elements = seleniumHandler.getElement(ROW_CHILD_2);
        } else if (secondRow != null && secondRow.getText().contains("БИЛЕТ")) {
            elements = seleniumHandler.getElement(ROW_CHILD_3);
        } else {
            log.error("Can't find row with tickets");
        }
        if (elements != null) {
            tickets = elements.findElements(By.cssSelector(LINKS)).stream().map(e -> e.getAttribute("href")).collect(toList());
        }
        return tickets;
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
