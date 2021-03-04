package ru.madrabit.webscraper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.madrabit.webscraper.exception.InvalidInputException;
import ru.madrabit.webscraper.selenium.consts.SiteLetters;
import ru.madrabit.webscraper.selenium.consts.SitesConst;
import ru.madrabit.webscraper.service.LauncherService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/launcher")
@Api(description = "REST API description")
public class LauncherController {
    private final LauncherService service;
    private final Map<String, SiteLetters> letters;
    private final SitesConst sites;

    public LauncherController(LauncherService service, SitesConst sites) {
        this.service = service;
        letters = collectLetters();
        this.sites = sites;
    }

    @ApiOperation(value = "Launch scraper with Parameter")
    @GetMapping("/{site}/{letter}")
    public ResponseEntity<String> launchByLetter(
            @ApiParam(name = "site",
                    required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @PathVariable String site,
            @ApiParam(name = "letter", required = true, example = "A_1",
                    allowableValues = "A_1, B_1, B_2")
            @PathVariable String letter) throws InvalidInputException {
        if (!letters.containsKey(letter)) {
            throw new InvalidInputException("Wrong parameter: letter.");
        }
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        return ResponseEntity.ok(service.executeByLetter(site, letter));
    }

    @ApiOperation(value = "Launch to get All tests")
    @GetMapping("/{site}/all")
    public ResponseEntity<String> launchAll(
            @ApiParam(name = "site",
                    required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @PathVariable String site
    ) throws InvalidInputException {
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        return ResponseEntity.ok(service.executeAll(site));
    }

    @ApiOperation(value = "Get scrapper status")
    @GetMapping("/{site}/status")
    public ResponseEntity<String> getStatus(
            @ApiParam(name = "site",
                    required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @PathVariable String site) throws InvalidInputException {
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        return ResponseEntity.ok(service.getStatus(site));
    }

    @ApiOperation(value = "Get Progress in percents")
    @GetMapping("/{site}/percent")
    public ResponseEntity<Integer> getPassedTicketsPercent(
            @ApiParam(name = "site",
                    required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @PathVariable String site) throws InvalidInputException {
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        return ResponseEntity.ok(service.getPassedTicketsPercent(site));
    }

    @ApiOperation(value = "Stop parser. Switch it off.")
    @GetMapping("/{site}/stop")
    public ResponseEntity<String> stopParser(
            @ApiParam(name = "site",
                    required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @PathVariable String site) throws InvalidInputException {
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        service.stop(site);
        return ResponseEntity.ok(service.getStatus(site));
    }

    private Map<String, SiteLetters> collectLetters() {
        Map<String, SiteLetters> map = new HashMap<>();
        for (SiteLetters letter : SiteLetters.values()) {
            map.put(letter.name(), letter);
        }
        return map;
    }
}
