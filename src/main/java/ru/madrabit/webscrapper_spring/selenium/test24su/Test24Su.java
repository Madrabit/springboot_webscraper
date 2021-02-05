package ru.madrabit.webscrapper_spring.selenium.test24su;

import ru.madrabit.webscrapper_spring.selenium.Scrapper;
import ru.madrabit.webscrapper_spring.selenium.TargetSite;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;

public class Test24Su implements TargetSite {
    private String status;

    private final Scrapper scraperForOne;
    private final AllScrapper scrapperForAll;

    public Test24Su() {
        this.scraperForOne = new CustomScrapperTest24();
        this.scrapperForAll = new AllScrapper();
    }

    @Override
    public void scrapeOneLetter(SiteLetters letter) {
        scraperForOne.work(letter);
    }

    @Override
    public void scrapeAllLetters() {
        scrapperForAll.scrapeAll();
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