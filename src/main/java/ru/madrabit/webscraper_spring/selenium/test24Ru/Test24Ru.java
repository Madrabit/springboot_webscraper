package ru.madrabit.webscraper_spring.selenium.test24Ru;

import ru.madrabit.webscraper_spring.selenium.SiteBase;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;

public class Test24Ru extends SiteBase {

    public Test24Ru() {
        super(new CustomScraperTest24(new SeleniumHandler()));
    }
}
