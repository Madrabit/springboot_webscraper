package ru.madrabit.webscrapper_spring.selenium.test24su;

import ru.madrabit.webscrapper_spring.selenium.Scrapper;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;

import java.util.Arrays;

public class AllScrapper  {
    Scrapper scrapper = new CustomScrapperTest24();

    public void scrapeAll() {
        Arrays.stream(SiteLetters.values())
                .forEach(letter -> scrapper.work(letter));
    }

}
