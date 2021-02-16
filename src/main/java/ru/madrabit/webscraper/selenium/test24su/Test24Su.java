package ru.madrabit.webscraper.selenium.test24su;

import ru.madrabit.webscraper.selenium.SiteBase;
import ru.madrabit.webscraper.selenium.config.SeleniumHandler;

public class Test24Su extends SiteBase {
    public Test24Su() {
       super(new CustomScraperTest24(new SeleniumHandler()));
    }
}
