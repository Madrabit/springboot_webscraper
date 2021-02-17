package ru.madrabit.webscraper.selenium;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.util.List;

@Slf4j
public abstract class QuestionsParserBase {
    protected int questionSerial;
    public final SeleniumHandler seleniumHandler;
    protected final List<String> ticketsList;
    protected final String id;
    protected boolean isStopped;
    protected int passedTickets;

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

    public int getPassedTickets() {
        return passedTickets;
    }
}
