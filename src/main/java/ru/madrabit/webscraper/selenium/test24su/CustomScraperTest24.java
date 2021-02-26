package ru.madrabit.webscraper.selenium.test24su;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper.selenium.CustomScraperBase;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.ElementsConstTest24Su;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class CustomScraperTest24 extends CustomScraperBase {

    private final UrlCrawler urlCrawler;
    protected QuestionsParser questionsParser;

    public CustomScraperTest24(SeleniumHandler seleniumHandler) {
        super(ElementsConstTest24Su.START_URL, seleniumHandler);
        this.urlCrawler = new UrlCrawler(seleniumHandler);
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
            questionsParser = new QuestionsParser(entry.getValue(), entry.getKey(), seleniumHandler);
            if (isStopped) {
                seleniumHandler.stop();
                return true;
            }
            List<Question> questionsList = getQuestions(questionsParser);
            saveToFile(questionsList, questionsList.isEmpty(), "test24su", entry.getKey());
        }
        return false;
    }

    public boolean workA() {
        seleniumHandler.openPage(ElementsConstTest24Su.A_TICKETS);
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<String> scrapeTickets = urlCrawler.scrapeTickets();
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1(scrapeTickets);
        log.info("Tickets size: {}", tickets.size());
        questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1", seleniumHandler);
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParser);
        saveToFile(questionsList, questionsList.isEmpty(), "test24su", "A.1");
        return false;
    }

    private List<Question> getQuestions(QuestionsParser questionsParser) {
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
