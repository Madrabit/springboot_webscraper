package ru.madrabit.webscrapper_spring.selenium;

import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.test24.Test24;

public class Main {
    public static void main(String[] args) {
        TargetSite test24su = new Test24();
        test24su.scrapeOneLetter(SiteLetters.valueOf(args[0]));
    }
}
