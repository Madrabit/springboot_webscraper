package ru.madrabit.webscraper_spring.selenium.test24Su;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscraper_spring.selenium.domen.Answer;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class QuestionsParser {
    private int questionSerial;
    private final SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private final List<String> ticketsList;
    private final String id;
    private boolean isStopped;

    public QuestionsParser(List<String> ticketsList, String id) {
        this.ticketsList = ticketsList;
        this.id = id;
    }

    private void moveToUrl(String url) {
        try {
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }

    public List<Question> iterateTickets() {
        List<Question> questionList = new LinkedList<>();
        this.setQuestionSerial(0);
        for (String ticket : ticketsList) {
            if (isStopped) {
                seleniumHandler.stop();
                return null;
            }
            moveToUrl(ticket);
            if (seleniumHandler.getElement(".entry-content")
                    .getText().contains("Этот тест в настоящее время неактивен.")) {
                return new ArrayList<>();
            }
            seleniumHandler.jumpToResult();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            questionList.addAll(getAllQuestions(id));
        }
        return questionList;
    }

    public void setQuestionSerial(int questionSerial) {
        this.questionSerial = questionSerial;
    }

    private Question parseQuestion(WebElement questionDiv, String id) {
        if (isStopped) {
            seleniumHandler.stop();
            return null;
        }
        Question question = new Question(
                id + "-" + ++questionSerial,
                questionDiv.findElement(By.cssSelector(".show-question-content")).getText()
        );
        List<WebElement> answers = questionDiv.findElements(By.cssSelector(".show-question-choices > ul > li"));
        int serial = 0;
        for (WebElement webElem : answers) {
            Answer answer = new Answer(++serial, webElem.getText(), question.getId());
            if (webElem.getAttribute("class").contains("correct-answer")) {
                answer.setRight(true);
                question.getAnswerNumber().add(serial);
                answer.setText(webElem.findElement(By.tagName("span")).getText());
            } else {
                answer.setText(webElem.getText());
            }
            question.getAnswerSet().add(answer);
        }
        return question;
    }

    private List<Question> getAllQuestions(String id) {
        WebElement main = seleniumHandler.getElement(".entry-content");
        return main.findElements((By.cssSelector(".watupro-choices-columns")))
                .stream().map(questionDiv -> parseQuestion(questionDiv, id)).collect(toList());
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }
}
