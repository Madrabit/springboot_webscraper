package ru.madrabit.webscraper.selenium.test24ru;

import ru.madrabit.webscraper.selenium.SiteBase;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;

public class Test24Ru extends SiteBase {

    public Test24Ru() {
        super(new CustomScraperTest24(new SeleniumHandler()));
    }
}
