package ru.madrabit.webscrapper_spring;

import org.junit.Assert;
import org.junit.Test;


public class UtilsTest {
    @Test
    public void checkRgba() {
        String inputValue = "rgba(110, 167, 62, 1)";
        String [] arr = inputValue.substring(5).split(",");
        boolean result = Integer.parseInt(arr[0]) < 120 && Integer.parseInt(arr[1].trim()) > 120;
        Assert.assertTrue(result);
    }
}
