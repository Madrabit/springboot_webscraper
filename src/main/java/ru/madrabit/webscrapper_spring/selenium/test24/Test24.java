package ru.madrabit.webscrapper_spring.selenium.test24;

import ru.madrabit.webscrapper_spring.selenium.TargetSite;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;

public class Test24 implements TargetSite {

    @Override
    public void scrapeOneLetter(SiteLetters letter) {
            new CustomScrapperTest24().work(letter);
    }

    @Override
    public void scrapeAllLetters() {
        new AllScrapper().scrapeAll();
    }
}
