package ru.madrabit.webscraper_spring.selenium.test24Su;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper_spring.selenium.CustomScraperBase;
import ru.madrabit.webscraper_spring.selenium.UrlCrawler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.List;
import java.util.Map;

@Slf4j
public class CustomScraperTest24 extends CustomScraperBase {

    private final UrlCrawler urlCrawler;

    public CustomScraperTest24() {
        super("https://tests24.su/test-24/promyshlennaya-bezopasnost/");
        this.urlCrawler = new UrlCrawlerImpl(seleniumHandler);
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

    public boolean workA() {
        seleniumHandler.openPage(ElementsConst.A_TICKETS);
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1(scrapeTickets);
        log.info("Tickets size: {}", tickets.size());
        QuestionsParserImpl questionsParserImpl = new QuestionsParserImpl(tickets.get("A.1"), "A.1");
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParserImpl);
        saveToFile(questionsList, questionsList.isEmpty(), "A.1");
        return false;
    }

    private List<Question> getQuestions(QuestionsParserImpl questionsParserImpl) {
        List<Question> questionsList = questionsParserImpl.iterateTickets();
        log.info("Questions in ticket: {}", questionsList.size());
        return questionsList;
    }


}
