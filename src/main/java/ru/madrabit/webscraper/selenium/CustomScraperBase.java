package ru.madrabit.webscraper.selenium;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.domen.Question;
import ru.madrabit.webscraper.selenium.poi.CreateExcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class CustomScraperBase implements Scraper {
    public final String startUrl;
    protected Map<String, List<String>> tickets = new HashMap<>();
    public final SeleniumHandler seleniumHandler;
    public String status;
    public boolean isStopped;
    public QuestionsParserBase questionsParser;


    public CustomScraperBase(String startUrl, SeleniumHandler seleniumHandler) {
        this.startUrl = startUrl;
        this.seleniumHandler = seleniumHandler;

    }

    @Override
    public void work(SiteLetters letter) {
        status = "In process";
        if (seleniumHandler.start(false)) {
            isStopped = false;
            seleniumHandler.openPage(startUrl);
            log.info("Opened main page: {}", startUrl);
            if (letter.equals(SiteLetters.A_1)) {
                if (workA()) {
                    return;
                }
            } else {
                if (workLetters(letter)) {
                    return;
                }
            }
            seleniumHandler.stop();
        }
    }

    protected abstract boolean workLetters(SiteLetters letter);


    protected abstract boolean workA();



    public void saveToFile(List<Question> questionsList, boolean isEmpty, String siteName, String letter) {
        if (!isEmpty) {
            CreateExcel excelDemo = new CreateExcel(siteName, letter);
            excelDemo.createExcel(questionsList);
            status = "Finished";
        }
    }



    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Map<String, List<String>> getTickets() {
        return tickets;
    }

}
