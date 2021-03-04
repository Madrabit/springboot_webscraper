package ru.madrabit.webscraper.selenium.consts;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class SitesConst {

    private final Map<String, String> map;

    public SitesConst() {
        map = new HashMap<>();
        map.put("test24su", "test24su");
        map.put("test24ru", "test24ru");
    }
}
