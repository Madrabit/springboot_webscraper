package ru.madrabit.webscraper_spring.selenium;

import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;
import ru.madrabit.webscraper_spring.selenium.poi.CreateExcel;
import ru.madrabit.webscraper_spring.selenium.test24Ru.QuestionsParser;

import java.util.List;

public abstract class CustomScraperBase implements Scraper {
    public final String START_URL;

    public SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    public String status;
    public boolean isStopped;
    public QuestionsParser questionsParser;

    public CustomScraperBase(String START_URL) {
        this.START_URL = START_URL;
    }

    @Override
    public abstract void work(SiteLetters letter);

    public void saveToFile(List<Question> questionsList, boolean isEmpty, String letter) {
        if (!isEmpty) {
            CreateExcel excelDemo = new CreateExcel(letter);
            excelDemo.createExcel(questionsList);
            status = "Finished";
        }
    }

    public void stop() {
        questionsParser.setStopped(true);
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
