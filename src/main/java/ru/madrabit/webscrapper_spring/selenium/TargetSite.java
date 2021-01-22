package ru.madrabit.webscrapper_spring.selenium;

import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;

public interface TargetSite {
    void scrapeOneLetter(SiteLetters letter);

    void scrapeAllLetters();

    String getStatus();
}
