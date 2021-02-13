package ru.madrabit.webscraper_spring.selenium.test24Su;

import ru.madrabit.webscraper_spring.selenium.SiteBase;
import ru.madrabit.webscraper_spring.selenium.config.SeleniumHandler;

public class Test24Su extends SiteBase {
    public Test24Su() {
       super(new CustomScraperTest24(new SeleniumHandler()));
    }
}
