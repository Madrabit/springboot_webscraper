package ru.madrabit.webscrapper_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.madrabit.webscrapper_spring.selenium.TargetSite;
import ru.madrabit.webscrapper_spring.selenium.consts.SiteLetters;
import ru.madrabit.webscrapper_spring.selenium.test24.Test24;

@SpringBootApplication
public class WebscrapperSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebscrapperSpringApplication.class, args);
        TargetSite test24su = new Test24();
        test24su.scrapeOneLetter(SiteLetters.A_1);
    }

}
