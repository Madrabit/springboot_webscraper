package ru.madrabit.webscraper.selenium;

import ru.madrabit.webscraper.selenium.consts.SiteLetters;

public interface Scraper {
    void work(SiteLetters letter);

    String getStatus();

    void stop();

    void setStatus(String status);
}
