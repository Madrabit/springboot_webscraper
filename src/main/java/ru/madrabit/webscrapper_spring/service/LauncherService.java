package ru.madrabit.webscrapper_spring.service;

import org.springframework.stereotype.Service;
import ru.madrabit.webscrapper_spring.selenium.TargetSite;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.test24.Test24;

import java.util.Arrays;

@Service
public class LauncherService {

    private final TargetSite site;

    public LauncherService() {
        this.site = new Test24();
    }

    public String executeByLetter(String letter) {
        String upperLetter = letter.toUpperCase();
        boolean isExists = Arrays.stream(SiteLetters.values()).anyMatch((l) -> l.name().equals(upperLetter));
        if (isExists) {
            Thread scrapThread = new Thread(() -> {
                site.scrapeOneLetter(SiteLetters.valueOf(upperLetter));
            });
            scrapThread.start();
        }
        return "started";
    }

    public String getStatus() {
        return site.getStatus();
    }

    public String executeAll() {
        Thread scrapThread = new Thread(() -> {
            Arrays.stream(SiteLetters.values())
                    .forEach(letter -> site.scrapeOneLetter(letter));
        });
        scrapThread.start();
        return "started";
    }

    public void stop() {
        site.stop();
    }
}
