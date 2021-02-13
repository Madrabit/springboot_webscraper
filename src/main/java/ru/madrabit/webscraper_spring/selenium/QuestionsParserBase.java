package ru.madrabit.webscraper_spring.selenium;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.List;

@Slf4j
public abstract class QuestionsParserBase {
    protected int questionSerial;
    public final SeleniumHandler seleniumHandler;
    protected final List<String> ticketsList;
    protected final String id;
    protected boolean isStopped;

    public QuestionsParserBase(List<String> ticketsList, String id, SeleniumHandler seleniumHandler) {
        this.seleniumHandler = seleniumHandler;
        this.ticketsList = ticketsList;
        this.id = id;
    }

    public void moveToUrl(String url) {
        try {
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }

    public abstract List<Question> iterateTickets();

    public void setQuestionSerial(int questionSerial) {
        this.questionSerial = questionSerial;
    }

    public abstract List<Question> getAllQuestions(String id);

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }
}
