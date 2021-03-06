package ru.madrabit.webscraper;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


public class UtilsTest {
    @Test
    public void checkRgba() {
        String inputValue = "rgba(110, 167, 62, 1)";
        String[] arr = inputValue.substring(5).split(",");
        boolean result = Integer.parseInt(arr[0]) < 120 && Integer.parseInt(arr[1].trim()) > 120;
        Assert.assertTrue(result);
    }

    @Test
    public void cutSubTestNameThenGetLetter() {
        String testName = "Б.1. 1 Эксплуатация объектов нефтехимии (ноябрь 2018)";
        String result = testName.substring(0, 7).replaceAll(" ", "");
        assertEquals("Б.1.1", result);
    }

    @Test
    public void cutTestNameThenGetLetter() {
        String testName = "Б17. Требования промышленной безопасности на объектах газораспределения и газопотребления";
        String letter = testName.substring(0, 1);
        String num = testName.substring(1, 3);
        String result = "";
        if ("А".equals(letter)) {
            result = "A." + num;
        } else if ("Б".equals(letter)) {
            result = "B." + num;
        }
        assertEquals("B.17", result);
    }


}
