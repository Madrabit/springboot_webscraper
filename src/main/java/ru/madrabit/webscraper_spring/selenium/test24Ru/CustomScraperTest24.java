package ru.madrabit.webscraper_spring.selenium.test24Ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.CustomScraperBase;
import ru.madrabit.webscraper_spring.selenium.ScrapeTickets;
import ru.madrabit.webscraper_spring.selenium.UrlCrawler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
public class CustomScraperTest24 extends CustomScraperBase {

    private final UrlCrawler urlCrawler;

    public CustomScraperTest24() {
        super("https://tests24.ru/?iter=1&s_group=1");
        this.urlCrawler = new UrlCrawlerImpl(seleniumHandler);
    }

    public boolean workA() {
        seleniumHandler.openPage(ElementsConst.A_TICKETS);
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
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1(scrapeTickets);
        log.info("Tickets size: {}", tickets.size());
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        seleniumHandler.openPage("https://tests24.ru/?iter=4&bil=1&test=726");
        QuestionsParserImpl questionsParserImpl = new QuestionsParserImpl(tickets.get("A.1"), "A.1");
        questionsParserImpl.autoPassing();
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParserImpl);
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
            QuestionsParserImpl questionsParserImpl = new QuestionsParserImpl(entry.getValue(), entry.getKey());
            if (isStopped) {
                seleniumHandler.stop();
                return true;
            }
            List<Question> questionsList = getQuestions(questionsParserImpl);
            saveToFile(questionsList, questionsList.isEmpty(), entry.getKey());
        }
        return false;
    }

    private List<Question> getQuestions(QuestionsParserImpl questionsParserImpl) {
        List<Question> questionsList = questionsParserImpl.iterateTickets();
        log.info("Questions in ticket: {}", questionsList.size());
        return questionsList;
    }
}
