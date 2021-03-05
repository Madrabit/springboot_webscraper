package ru.madrabit.webscraper.selenium.test24ru;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper.selenium.CustomScraperBase;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.ElementsConstTest24Ru;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
public class CustomScraperTest24 extends CustomScraperBase {

    private int passedTickets;
    private final UrlCrawler urlCrawler;
    protected QuestionsParser questionsParser;
    private final String startUrlA = "https://tests24.ru/?iter=4&bil=1&test=726";

    public CustomScraperTest24(SeleniumHandler seleniumHandler) {
        super(ElementsConstTest24Ru.START_URL, seleniumHandler);
        this.urlCrawler = new UrlCrawler(seleniumHandler);
    }

    public boolean workA() {
        seleniumHandler.openPage(ElementsConstTest24Ru.A_TICKETS);
        List<String> ticketsScraper = urlCrawler.scrapeTickets();
        tickets = urlCrawler.pushTicketsToA1(ticketsScraper);
        log.info("Tickets size: {}", tickets.size());
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        seleniumHandler.openPage(startUrlA);
        questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1", seleniumHandler);
        questionsParser.autoPassing();
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParser);
        saveToFile(questionsList, questionsList.isEmpty(), "test24ru", "A.1");
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
        tickets = urlCrawler.getTicketsUrl(subTests);
        log.info("Tickets size: {}", tickets.size());
        for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
            questionsParser = new QuestionsParser(entry.getValue(), entry.getKey(), seleniumHandler);
            if (isStopped) {
                seleniumHandler.stop();
                return true;
            }
            List<Question> questionsList = getQuestions(questionsParser);
            saveToFile(questionsList, questionsList.isEmpty(), "test24ru", entry.getKey());
        }
        return false;
    }


    public List<Question> getQuestions(QuestionsParser questionsParser) {
        Optional<List<Question>> questionsList = Optional.ofNullable(questionsParser.iterateTickets());
        log.info("Questions in ticket: {}", questionsList.isPresent() ? questionsList.get().size() : new ArrayList<>());
        return questionsList.orElse(new ArrayList<>());
    }

    public void stop() {
        questionsParser.setStopped(true);
        this.isStopped = true;
    }

    @Override
    public int getPassedTickets() {
        return questionsParser.getPassedTickets();
    }
}
