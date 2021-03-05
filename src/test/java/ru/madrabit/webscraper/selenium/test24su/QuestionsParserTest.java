package ru.madrabit.webscraper.selenium.test24su;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.domen.Question;
import ru.madrabit.webscraper.selenium.test24su.QuestionsParser;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsParserTest {

    private SeleniumHandler seleniumHandler;
    private QuestionsParser questionsParser;
    private String url = "https://tests24.su/a-1-bilet-1/";

    @BeforeEach
    void setUp() {
        seleniumHandler = new SeleniumHandler();
        questionsParser = new QuestionsParser(Collections.singletonList(url), "A.1", seleniumHandler);
        seleniumHandler.start(true);
    }

    @AfterEach
    void tearDown() {
        seleniumHandler.stop();
    }

    @Test
    void iterateTickets() {
        seleniumHandler.openPage(url);
        final List<Question> questions = questionsParser.iterateTickets();
        for (Question question : questions) {
            System.out.println(question.getText());
        }
        Assert.assertTrue(questions.size() > 0);
    }

    @Test
    void getAllQuestions() {
        questionsParser.moveToUrl(url);
        seleniumHandler.jumpToResult();
        final List<Question> allQuestions = questionsParser.getAllQuestions("A.1");
        allQuestions.stream().map(question -> question.getText()).forEach(System.out::println);
        Assert.assertTrue(allQuestions.size() > 0);
    }
}
