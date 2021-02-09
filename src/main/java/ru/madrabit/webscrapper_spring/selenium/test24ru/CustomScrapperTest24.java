package ru.madrabit.webscrapper_spring.selenium.test24ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscrapper_spring.selenium.Scrapper;
import ru.madrabit.webscrapper_spring.selenium.UrlCrawler;
import ru.madrabit.webscrapper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscrapper_spring.selenium.test24ru.ElementsConst;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.domen.Question;
import ru.madrabit.webscrapper_spring.selenium.poi.CreateExcel;

import java.util.*;

@Slf4j
public class CustomScrapperTest24 implements Scrapper {

    public static final String START_URL = "https://tests24.ru/?iter=1&s_group=1";

    private SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private List<Question> questionsList = new LinkedList<>();
    private String status;
    private boolean isStopped;
    private QuestionsParser questionsParser;

    @Override
    public void work(SiteLetters letter) {

        status = "In process";
        if (seleniumHandler.start(true)) {
            isStopped = false;
            seleniumHandler.openPage(START_URL);
            log.info("Opened main page: {}", START_URL);

            UrlCrawler urlCrawler = new UrlCrawlerImpl(seleniumHandler);
            if (letter.equals(SiteLetters.A_1)) {
                seleniumHandler.openPage(ElementsConst.A_TICKETS);
                Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1();
                log.info("Tickets size: {}", tickets.size());
                if (isStopped) {
                    seleniumHandler.stop();
                    return;
                }
                seleniumHandler.openPage("https://tests24.ru/?iter=4&bil=1&test=726");
                WebElement questionsForm = seleniumHandler.getElement(".container > div > form");
                List<WebElement> questionsDiv = questionsForm.findElements(By.cssSelector(".card.flex-shrink-1.shadow"));
                for (WebElement question : questionsDiv) {
                    question.findElement(By.cssSelector(".custom-control.custom-radio > label")).click();
                }
                seleniumHandler.getElement(".btn.btn-primary").click();
                questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1");
                questionsList = questionsParser.iterateTickets();
                if (isStopped) {
                    seleniumHandler.stop();
                    return;
                }
                log.info("Questions in ticket: {}", questionsList.size());
                saveToFile(questionsList.isEmpty(), "A.1");
            } else {
                Map<Enum<SiteLetters>, String> letters = urlCrawler.scrapeLetters();
                seleniumHandler.openPage(letters.get(letter));

//                seleniumHandler.openPage("https://tests24.ru/?iter=2&group=11");

                Map<String, String> subTests = urlCrawler.scrapeSubTests();
                if (isStopped) {
                    seleniumHandler.stop();
                    return;
                }

                Map<String, List<String>> tickets = urlCrawler.getTicketsUrl(subTests);
                log.info("Tickets size: {}", tickets.size());
                for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
                    QuestionsParser questionsParser = new QuestionsParser(entry.getValue(), entry.getKey());
                    if (isStopped) {
                        seleniumHandler.stop();
                        return;
                    }
                    questionsList = questionsParser.iterateTickets();
                    log.info("Questions in ticket: {}", questionsList.size());
                    saveToFile(questionsList.isEmpty(), entry.getKey());
                }
            }

            seleniumHandler.stop();
        }


    }

    private boolean checkRgba(String rgba) {
        String[] arr = rgba.substring(5).split(",");
        return Integer.parseInt(arr[0]) < 120 && Integer.parseInt(arr[1].trim()) > 120;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public void setStatus(String status) {

    }

    private void saveToFile(boolean isEmpty, String letter) {
        if (!isEmpty) {
            CreateExcel excelDemo = new CreateExcel(letter);
            excelDemo.createExcel(questionsList);
            status = "Finished";
        }
    }
//
//    public void stop() {
//        questionsParser.setStopped(true);
//        this.isStopped = true;
//    }
//
//    @Override
//    public String getStatus() {
//        return status;
//    }
//
//    @Override
//    public void setStatus(String status) {
//        this.status = status;
//    }
}
