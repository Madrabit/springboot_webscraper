package ru.madrabit.webscraper.selenium.domen;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@EqualsAndHashCode
@Slf4j
@ToString
public class Answer {
    private int id;
    private String text;
    private String questionId;
    private boolean right;

    public Answer(int id, String text, String questionId) {
        this.id = id;
        this.text = text;
        this.questionId = questionId;
    }
}
