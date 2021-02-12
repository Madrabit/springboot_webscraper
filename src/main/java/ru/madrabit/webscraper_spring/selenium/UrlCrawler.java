package ru.madrabit.webscraper_spring.selenium;

import ru.madrabit.webscraper_spring.selenium.consts.SiteLetters;

import java.util.List;
import java.util.Map;

public interface UrlCrawler {
    Map<String, String> scrapeSubTests();

    Map<Enum<SiteLetters>, String> scrapeLetters();

    Map<String, List<String>> getTicketsUrl(Map<String, String> subTests);

    Map<String, List<String>> getTicketsUrlForA1(List<String> ticketsScraper);

    List<String> scrapeTickets();
}
