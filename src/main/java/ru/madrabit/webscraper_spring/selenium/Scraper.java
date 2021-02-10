package ru.madrabit.webscraper_spring.selenium;

import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;

public interface Scraper {
    void work(SiteLetters letter);

    String getStatus();

    void stop();

    void setStatus(String status);
}
