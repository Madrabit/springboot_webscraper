package ru.madrabit.webscraper_spring.selenium.test24Ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.CustomScraperBase;
import ru.madrabit.webscraper_spring.selenium.ScrapeTickets;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.consts.ElementsConstTest24Ru;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
public class CustomScraperTest24 extends CustomScraperBase {

    private final ru.madrabit.webscraper_spring.selenium.UrlCrawler urlCrawler;

    public CustomScraperTest24(SeleniumHandler seleniumHandler) {
        super("https://tests24.ru/?iter=1&s_group=1", seleniumHandler);
        this.urlCrawler = new UrlCrawler(seleniumHandler);
    }

    public boolean workA() {
        seleniumHandler.openPage(ElementsConstTest24Ru.A_TICKETS);

        ScrapeTickets scrapeTickets = () -> {
            String LINKS = ".col-6.col-sm-6.col-md-3.col-lg-2 a";
            String ROW_WITH_TICKETS = ".row.text-center";
            WebElement elements = null;
            List<String> tickets = new ArrayList<>();
            elements = seleniumHandler.getElement(ROW_WITH_TICKETS);
            if (elements != null) {
                tickets = elements.findElements(By.cssSelector(LINKS)).stream().map(e -> e.getAttribute("href")).collect(toList());
            }
            return tickets;
        };
        List<String> ticketsScraper = urlCrawler.scrapeTickets();
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1(ticketsScraper);

        log.info("Tickets size: {}", tickets.size());
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        seleniumHandler.openPage("https://tests24.ru/?iter=4&bil=1&test=726");
        QuestionsParser questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1", seleniumHandler);
        questionsParser.autoPassing();
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParser);
        saveToFile(questionsList, questionsList.isEmpty(), "A.1");
        return false;
    }

    public boolean workLetters(SiteLetters letter) {
        Map<Enum<SiteLetters>, String> letters = urlCrawler.scrapeLetters();
        seleniumHandler.openPage(letters.get(letter));
        Map<String, String> subTests = urlCrawler.scrapeSubTests();
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrl(subTests);
        log.info("Tickets size: {}", tickets.size());
        for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
            QuestionsParser questionsParser = new QuestionsParser(entry.getValue(), entry.getKey(), seleniumHandler);
            if (isStopped) {
                seleniumHandler.stop();
                return true;
            }
            List<Question> questionsList = getQuestions(questionsParser);
            saveToFile(questionsList, questionsList.isEmpty(), entry.getKey());
        }
        return false;
    }

    private List<Question> getQuestions(QuestionsParser questionsParser) {
        List<Question> questionsList = questionsParser.iterateTickets();
        log.info("Questions in ticket: {}", questionsList.size());
        return questionsList;
    }
}
