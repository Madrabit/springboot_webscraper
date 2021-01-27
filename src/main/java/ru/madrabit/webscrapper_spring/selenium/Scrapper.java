package ru.madrabit.webscrapper_spring.selenium;

import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;

public interface Scrapper {
    void work(SiteLetters letter);

    String getStatus();

    void stop();

    void setStatus(String status);
}
