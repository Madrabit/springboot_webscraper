package ru.madrabit.webscraper.service;

import org.springframework.stereotype.Service;
import ru.madrabit.webscraper.selenium.TargetSite;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.test24ru.Test24Ru;
import ru.madrabit.webscraper.selenium.test24su.Test24Su;

import java.util.Arrays;

@Service
public class LauncherService {

    private final TargetSite test24ru;
    private final TargetSite test24su;
    private static final String TEST_24_SU = "test24su";
    private static final String TEST_24_RU = "test24ru";

    public LauncherService() {
        this.test24ru = new Test24Ru();
        this.test24su = new Test24Su();
    }

    public String executeByLetter(String site, String letter) {
        String upperLetter = letter.toUpperCase();
        String siteLower = site.toLowerCase();
        boolean isExists = Arrays.stream(SiteLetters.values()).anyMatch((l) -> l.name().equals(upperLetter));
        if (isExists) {
            Thread scrapThread24ru = new Thread(() ->
                test24ru.scrapeOneLetter(SiteLetters.valueOf(upperLetter)));
            Thread scrapThread24su = new Thread(() ->
                    test24su.scrapeOneLetter(SiteLetters.valueOf(upperLetter)));
            if (TEST_24_SU.equals(siteLower)) {
                scrapThread24su.start();
            } else if (TEST_24_RU.equals(siteLower)) {
                scrapThread24ru.start();
            } else {
                return "No such site";
            }

        }
        return "started";
    }

    public String getStatus(String site) {
        return test24ru.getStatus();
    }

    public int getPassedTicketsPercent(String site) {
        return test24ru.getPassedTickets();
    }

    public String executeAll(String site) {
        String siteLower = site.toLowerCase();
        Thread scrapThread24ru = new Thread(() -> Arrays.stream(SiteLetters.values())
                    .forEach(test24ru::scrapeOneLetter));
        Thread scrapThread24su = new Thread(() -> Arrays.stream(SiteLetters.values())
                .forEach(test24su::scrapeOneLetter));
        if (TEST_24_SU.equals(siteLower)) {
            scrapThread24su.start();
        } else if (TEST_24_RU.equals(siteLower)) {
            scrapThread24ru.start();
        } else {
            return "No such site";
        }
        return "started";
    }

    public void stop(String site) {
        String siteLower = site.toLowerCase();
        if (TEST_24_SU.equals(siteLower)) {
            test24su.stop();
        } else if (TEST_24_RU.equals(siteLower)) {
            test24ru.stop();
        }
    }
}
