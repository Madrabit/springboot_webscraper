package ru.madrabit.webscraper.selenium.test24ru;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper.selenium.domen.Question;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomScraperTest24Test {

    private SeleniumHandler seleniumHandler;
    private CustomScraperTest24 customScraperTest24;

    @BeforeEach
    public void init() {
        seleniumHandler = new SeleniumHandler();
        seleniumHandler.start(true);
        customScraperTest24 = new CustomScraperTest24(seleniumHandler);

    }

    @Test
    void whenGetQuestionsNotEmptyText() {
        final Map<String, List<String>> ticketsMap = new HashMap<>();
        ticketsMap.put("A.1", Arrays.asList("https://tests24.ru/?iter=4&bil=1&test=726"));
        QuestionsParser questionsParser = new QuestionsParser(ticketsMap.get("A.1"), "A.1", seleniumHandler);
        seleniumHandler.openPage(customScraperTest24.getStartUrlA());
        questionsParser.autoPassing();
        List<Question> questionsList = customScraperTest24.getQuestions(questionsParser);
        Assert.assertTrue(!questionsList.get(0).getText().isEmpty());
    }

    @AfterEach
    public void tearDown() {
        seleniumHandler.stop();
    }
}
