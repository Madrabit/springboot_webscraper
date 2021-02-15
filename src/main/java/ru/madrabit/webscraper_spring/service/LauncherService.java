package ru.madrabit.webscraper_spring.service;

import org.springframework.stereotype.Service;
import ru.madrabit.webscraper_spring.selenium.TargetSite;
import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscraper_spring.selenium.test24Ru.Test24Ru;
import ru.madrabit.webscraper_spring.selenium.test24Su.Test24Su;

import java.util.Arrays;

@Service
public class LauncherService {

    private final TargetSite test24Ru;
    private final TargetSite test24Su;

    public LauncherService() {
        this.test24Ru = new Test24Ru();
        this.test24Su = new Test24Su();
    }

    public String executeByLetter(String letter) {
        String upperLetter = letter.toUpperCase();
        boolean isExists = Arrays.stream(SiteLetters.values()).anyMatch((l) -> l.name().equals(upperLetter));
        if (isExists) {
            Thread scrapThread = new Thread(() -> {
                test24Ru.scrapeOneLetter(SiteLetters.valueOf(upperLetter));
            });
            Thread scrapThread24Su = new Thread(() -> {
                test24Su.scrapeOneLetter(SiteLetters.valueOf(upperLetter));
            });
            scrapThread24Su.start();
            scrapThread.start();
        }
        return "started";
    }

    public String getStatus() {
        return test24Ru.getStatus();
    }

    public String executeAll() {
        Thread scrapThread = new Thread(() -> {
            Arrays.stream(SiteLetters.values())
                    .forEach(letter -> test24Ru.scrapeOneLetter(letter));
        });
        scrapThread.start();
        return "started";
    }

    public void stop() {
        test24Ru.stop();
    }
}
