package ru.madrabit.webscraper.selenium.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

/**
 * Config for Chromediver.
 * Driver saved in resources folder.
 * IMPORTANT chromedriver.exe should match Selenium version driver.
 */
@Slf4j
@Component
public class ChromeConfig {
    public static final String CHROME_DRIVER_PATH = "chromedriver.exe";

    public static void setDriverPath() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        log.info("Path to chrome driver: {}", System.getProperty("webdriver.chrome.driver"));
    }

    static ChromeOptions getChromeOptions(boolean headlessMode) {
        ChromeOptions options = new ChromeOptions()
                .addArguments("--lang=ru")
                .addArguments("start-maximized");

        if (headlessMode) {
            options.addArguments("--headless");
        }
        return options;

    }
}
