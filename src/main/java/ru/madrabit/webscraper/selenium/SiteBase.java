package ru.madrabit.webscraper.selenium;

import lombok.Getter;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public abstract class SiteBase implements TargetSite {
    private String status;
    Map<String, List<String>> tickets;
    private int passedTickets;

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

    public Map<String, List<String>> getTickets() {
        return scraperForOne.getTickets();
    }

    public int getPassedTickets() {
        return scraperForOne.getPassedTickets();
    }
}
