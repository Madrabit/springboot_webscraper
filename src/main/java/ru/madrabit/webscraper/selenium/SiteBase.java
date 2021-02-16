package ru.madrabit.webscraper.selenium;

import ru.madrabit.webscraper.selenium.consts.SiteLetters;

import java.util.Arrays;

public abstract class SiteBase implements TargetSite {
    private String status;

    private final Scraper scraperForOne;

    public SiteBase(Scraper scrapper) {
        this.scraperForOne = scrapper;
    }

    @Override
    public void scrapeOneLetter(SiteLetters letter) {
        scraperForOne.work(letter);
    }



    public void scrapeAllLetters() {
        Arrays.stream(SiteLetters.values())
                .forEach(letter -> scraperForOne.work(letter));
    }

    @Override
    public String getStatus() {
        return scraperForOne.getStatus();
    }

    public void stop() {
        scraperForOne.stop();
        scraperForOne.setStatus("stopped");
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
