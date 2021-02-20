package ru.madrabit.webscraper.selenium;

import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.test24ru.Test24Ru;
import ru.madrabit.webscraper.selenium.test24su.Test24Su;

public class Main {
    public static void main(String[] args) {
        SiteBase test24Su = new Test24Su();
        SiteBase test24Ru = new Test24Ru();

        Thread test24SuThread = new Thread(() -> test24Su.scrapeOneLetter(SiteLetters.A_1));

        Thread test24RuThread = new Thread(() -> test24Ru.scrapeOneLetter(SiteLetters.A_1));

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
//        test24SuThread.start();
        test24RuThread.start();
    }
}
