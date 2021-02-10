package ru.madrabit.webscraper_spring.selenium.test24Ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.CustomScraperBase;
import ru.madrabit.webscraper_spring.selenium.UrlCrawler;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.List;
import java.util.Map;

@Slf4j
public class CustomScraperTest24 extends CustomScraperBase {

    public CustomScraperTest24() {
        super("https://tests24.ru/?iter=1&s_group=1");
    }

    @Override
    public void work(SiteLetters letter) {
        status = "In process";
        if (seleniumHandler.start(true)) {
            isStopped = false;
            seleniumHandler.openPage(START_URL);
            log.info("Opened main page: {}", START_URL);
            UrlCrawler urlCrawler = new UrlCrawlerImpl(seleniumHandler);
            if (letter.equals(SiteLetters.A_1)) {
                if (workA(urlCrawler)) return;
            } else {
                if (workLetter(letter, urlCrawler)) return;
            }
            seleniumHandler.stop();
        }
    }

    private boolean workA(UrlCrawler urlCrawler) {
        seleniumHandler.openPage(ElementsConst.A_TICKETS);
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1();
        log.info("Tickets size: {}", tickets.size());
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        seleniumHandler.openPage("https://tests24.ru/?iter=4&bil=1&test=726");
        WebElement questionsForm = seleniumHandler.getElement(".container > div > form");
        List<WebElement> questionsDiv = questionsForm.findElements(By.cssSelector(".card.flex-shrink-1.shadow"));
        for (WebElement question : questionsDiv) {
            question.findElement(By.cssSelector(".custom-control.custom-radio > label")).click();
        }
        seleniumHandler.getElement(".btn.btn-primary").click();
        QuestionsParser questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1");
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        List<Question> questionsList = getQuestions(questionsParser);
        saveToFile(questionsList, questionsList.isEmpty(), "A.1");
        return false;
    }

    private boolean workLetter(SiteLetters letter, UrlCrawler urlCrawler) {
        Map<Enum<SiteLetters>, String> letters = urlCrawler.scrapeLetters();
        seleniumHandler.openPage(letters.get(letter));
        Map<String, String> subTests = urlCrawler.scrapeSubTests();
        if (isStopped) {
            seleniumHandler.stop();
            return true;
        }
        Map<String, List<String>> tickets = urlCrawler.getTicketsUrl(subTests);
        log.info("Tickets size: {}", tickets.size());
        for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
            QuestionsParser questionsParser = new QuestionsParser(entry.getValue(), entry.getKey());
            if (isStopped) {
                seleniumHandler.stop();
                return true;
            }
            List<Question> questionsList = getQuestions(questionsParser);
            saveToFile(questionsList, questionsList.isEmpty(), entry.getKey());
        }
        return false;
    }

    private List<Question> getQuestions(QuestionsParser questionsParser) {
        List<Question> questionsList = questionsParser.iterateTickets();
        log.info("Questions in ticket: {}", questionsList.size());
        return questionsList;
    }
}
