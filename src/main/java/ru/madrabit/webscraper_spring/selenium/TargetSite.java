package ru.madrabit.webscraper_spring.selenium;

import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;

public interface TargetSite {
    void scrapeOneLetter(SiteLetters letter);

    void scrapeAllLetters();

    String getStatus();

    void stop();
}
