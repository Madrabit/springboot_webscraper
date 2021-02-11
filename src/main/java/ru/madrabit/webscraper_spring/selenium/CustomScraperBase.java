package ru.madrabit.webscraper_spring.selenium;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;
import ru.madrabit.webscraper_spring.selenium.poi.CreateExcel;
import ru.madrabit.webscraper_spring.selenium.test24Ru.QuestionsParserImpl;
import ru.madrabit.webscraper_spring.selenium.test24Su.UrlCrawlerImpl;

import java.util.List;

@Slf4j
public abstract class CustomScraperBase implements Scraper {
    public final String START_URL;

    public SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    public String status;
    public boolean isStopped;
    public QuestionsParserImpl questionsParserImpl;

    public CustomScraperBase(String START_URL) {
        this.START_URL = START_URL;
    }

    @Override
    public void work(SiteLetters letter) {
        status = "In process";
        if (seleniumHandler.start(true)) {
            isStopped = false;
            seleniumHandler.openPage(START_URL);
            log.info("Opened main page: {}", START_URL);
            if (letter.equals(SiteLetters.A_1)) {
                if (workA()) return;
            } else {
                if (workLetters(letter)) return;
            }
            seleniumHandler.stop();
        }
    }

    protected abstract boolean workLetters(SiteLetters letter);

    protected abstract boolean workA();


    public void saveToFile(List<Question> questionsList, boolean isEmpty, String letter) {
        if (!isEmpty) {
            CreateExcel excelDemo = new CreateExcel(letter);
            excelDemo.createExcel(questionsList);
            status = "Finished";
        }
    }

    public void stop() {
        questionsParserImpl.setStopped(true);
        this.isStopped = true;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
