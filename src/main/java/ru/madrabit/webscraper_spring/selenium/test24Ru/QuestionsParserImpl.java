package ru.madrabit.webscraper_spring.selenium.test24Ru;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.webscraper_spring.selenium.QuestionsParserBase;
import ru.madrabit.webscraper_spring.selenium.domen.Answer;
import ru.madrabit.webscraper_spring.selenium.domen.Question;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class QuestionsParserImpl extends QuestionsParserBase {


    public QuestionsParserImpl(List<String> ticketsList, String id) {
        super(ticketsList, id);
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
            autoPassing();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            questionList.addAll(getAllQuestions(id));
        }
        return questionList;
    }

    public void autoPassing() {
        WebElement questionsForm = seleniumHandler.getElement(".container > div > form");
        List<WebElement> questionsDiv = questionsForm.findElements(By.cssSelector(".card.flex-shrink-1.shadow"));
        for (WebElement question : questionsDiv) {
            question.findElement(By.cssSelector(".custom-control.custom-radio > label")).click();
        }
        seleniumHandler.getElement(".btn.btn-primary").click();
    }

    public List<Question> getAllQuestions(String id) {
        WebElement main = seleniumHandler.getElement("#item1 > div:nth-child(2) > div");
        List<Question> list = new ArrayList<>();
        for (WebElement questionDiv : main.findElements((By.cssSelector(".card .flex-shrink-1")))) {
            Question question = parseQuestion(questionDiv, id);
            list.add(question);
        }
        return list;
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

}
