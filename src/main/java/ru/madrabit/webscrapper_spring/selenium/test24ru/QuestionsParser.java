package ru.madrabit.webscrapper_spring.selenium.test24ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscrapper_spring.selenium.config.SeleniumHandler;
import ru.madrabit.webscrapper_spring.selenium.domen.Answer;
import ru.madrabit.webscrapper_spring.selenium.domen.Question;

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
            WebElement questionsForm = seleniumHandler.getElement(".container > div > form");
            List<WebElement> questionsDiv = questionsForm.findElements(By.cssSelector(".card.flex-shrink-1.shadow"));
            for (WebElement question : questionsDiv) {
                question.findElement(By.cssSelector(".custom-control.custom-radio > label")).click();
            }
            seleniumHandler.getElement(".btn.btn-primary").click();
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

    private boolean checkRgba(String rgba) {
        String[] arr = rgba.substring(5).split(",");
        return Integer.parseInt(arr[0]) < 120 && Integer.parseInt(arr[1].trim()) > 120;
    }

    private Question parseQuestion(WebElement questionDiv, String id) {
        if (isStopped) {
            seleniumHandler.stop();
            return null;
        }
        Question question = new Question(
                id + "-" + ++questionSerial,
                questionDiv.findElement(By.cssSelector("i")).getText().substring(5).trim()
        );
        List<WebElement> answers = questionDiv.findElements(By.cssSelector(".f_sm"));
        int serial = 0;
        for (WebElement webElem : answers) {
            Answer answer = new Answer(++serial, webElem.getText(), question.getId());
            if (!webElem.getCssValue("color").isEmpty() && checkRgba(webElem.getCssValue("color"))) {
                answer.setRight(true);
                question.getAnswerNumber().add(serial);
                answer.setText(webElem.getText());
            } else {
                answer.setText(webElem.getText());
            }
            question.getAnswerSet().add(answer);
        }
        return question;
    }

    private List<Question> getAllQuestions(String id) {
        WebElement main = seleniumHandler.getElement("#item1 > div:nth-child(2) > div");
        List<Question> list = new ArrayList<>();
        for (WebElement questionDiv : main.findElements((By.cssSelector(".card .flex-shrink-1")))) {
            Question question = parseQuestion(questionDiv, id);
            list.add(question);
        }
        return list;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }
}
