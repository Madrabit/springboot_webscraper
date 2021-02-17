package ru.madrabit.webscraper.selenium;

import ru.madrabit.webscraper.selenium.consts.SiteLetters;

import java.util.List;
import java.util.Map;

public interface Scraper {
    void work(SiteLetters letter);

    String getStatus();

    void stop();

    void setStatus(String status);

    Map<String, List<String>> getTickets();

    int getPassedTickets();
}
