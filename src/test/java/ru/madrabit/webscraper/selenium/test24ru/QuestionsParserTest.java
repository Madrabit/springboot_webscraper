package ru.madrabit.webscraper.selenium.test24ru;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.util.Collections;
import java.util.List;

class QuestionsParserTest {

    private SeleniumHandler seleniumHandler;
    private QuestionsParser questionsParser;
    private String url = "https://tests24.ru/?iter=4&bil=1&test=726";
    @BeforeEach
    public void init() {
        seleniumHandler = new SeleniumHandler();
        questionsParser = new QuestionsParser(Collections.singletonList(url), "A.1", seleniumHandler);
        seleniumHandler.start(true);
    }


    @Test
    void whenIterateTicketsSizeNotEmpty() {
        seleniumHandler.openPage(url);
        final List<Question> questions = questionsParser.iterateTickets();
        for (Question question : questions) {
            System.out.println(question.getText());
        }
        Assert.assertTrue(questions.size() > 0);
    }

    @Test
    void whenGetAllQuestionsSizeNotEmpty() {
        questionsParser.moveToUrl(url);
        questionsParser.autoPassing();
        final List<Question> allQuestions = questionsParser.getAllQuestions("A.1");
        allQuestions.stream().map(question -> question.getText()).forEach(System.out::println);
        Assert.assertTrue(allQuestions.size() > 0);
    }

    @AfterEach
    public void tearDown() {
        seleniumHandler.stop();
    }
}
