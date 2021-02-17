package ru.madrabit.webscraper.selenium;

import ru.madrabit.webscraper.selenium.consts.SiteLetters;

public interface TargetSite {
    void scrapeOneLetter(SiteLetters letter);

    void scrapeAllLetters();

    String getStatus();

    void stop();

    int getPassedTickets();
}
