package ru.madrabit.webscrapper_spring.selenium;

import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.test24ru.Test24Ru;
import ru.madrabit.webscrapper_spring.selenium.test24su.Test24Su;

public class Main {
    public static void main(String[] args) {
        TargetSite site = new Test24Ru();

        Thread scrapThread = new Thread(() -> {
            site.scrapeOneLetter(SiteLetters.B_1);
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
