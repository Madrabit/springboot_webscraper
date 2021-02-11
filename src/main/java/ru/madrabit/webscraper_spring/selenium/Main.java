package ru.madrabit.webscraper_spring.selenium;

import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.test24Ru.Test24Ru;
import ru.madrabit.webscraper_spring.selenium.test24Su.Test24Su;

public class Main {
    public static void main(String[] args) {
        SiteBase site = new Test24Su();

        Thread scrapThread = new Thread(() -> {
            site.scrapeOneLetter(SiteLetters.A_1);
        });

//        Thread statusThread = new Thread(() -> {
//            try {
//                while (true) {
//                    System.out.println(site.getStatus());
//                    Thread.sleep(5000);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        scrapThread.start();
//        statusThread.start();
    }
}
