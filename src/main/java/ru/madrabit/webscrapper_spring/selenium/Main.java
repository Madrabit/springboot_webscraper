package ru.madrabit.webscrapper_spring.selenium;

import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.test24.Test24;

public class Main {
    public static void main(String[] args) {
        TargetSite test24su = new Test24();

        Thread scrapThread = new Thread(() -> {
            test24su.scrapeOneLetter(SiteLetters.A_1);
        });

        Thread statusThread = new Thread(() -> {
            try {
                while (true) {
                    System.out.println(test24su.getStatus());
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        scrapThread.start();
        statusThread.start();
    }
}
