package ru.madrabit.webscrapper_spring.selenium.test24;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscrapper_spring.selenium.Scrapper;
import ru.madrabit.webscrapper_spring.selenium.UrlCrawler;
import ru.madrabit.webscrapper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscrapper_spring.selenium.consts.ElementsConst;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.domen.Question;
import ru.madrabit.webscrapper_spring.selenium.poi.CreateExcel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomScrapperTest24 implements Scrapper {

    public static final String START_URL = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";

    private SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    List<Question> questionList = new LinkedList<>();
    Map<Enum<SiteLetters>, String> letters = new HashMap<>();
    private String status;

    @Override
    public void work(SiteLetters letter) {
        status = "In process";
        if (seleniumHandler.start(true)) {
            seleniumHandler.openPage(START_URL);
            log.info("Opened main page: {}", START_URL);

            UrlCrawler urlCrawler = new UrlCrawlerImpl(seleniumHandler);
            if (letter.equals(SiteLetters.A_1)) {
                seleniumHandler.openPage(ElementsConst.A_TICKETS);
                Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1();
                log.info("Tickets size: {}", tickets.size());
                QuestionsParser questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1");
                questionList = questionsParser.iterateTickets();
                log.info("Questions in ticket: {}", questionList.size());
                saveToFile(questionList.isEmpty(), "A.1");
            } else {
                letters = urlCrawler.scrapeLetters();
                seleniumHandler.openPage(letters.get(letter));
                Map<String, String> subTests = urlCrawler.scrapeSubTests();
                Map<String, List<String>> tickets = urlCrawler.getTicketsUrl(subTests);
                log.info("Tickets size: {}", tickets.size());
                for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
                    QuestionsParser questionsParser = new QuestionsParser(entry.getValue(), entry.getKey());
                    questionList = questionsParser.iterateTickets();
                    log.info("Questions in ticket: {}", questionList.size());
                    saveToFile(questionList.isEmpty(), entry.getKey());
                }
            }
            seleniumHandler.stop();
        }
    }

    private void saveToFile(boolean isEmpty, String letter) {
        if (!isEmpty) {
            CreateExcel excelDemo = new CreateExcel(letter);
            excelDemo.createExcel(questionList);
            status = "Finished";
        }
    }

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
