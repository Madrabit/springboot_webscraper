package ru.madrabit.webscrapper_spring.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.madrabit.webscrapper_spring.service.LauncherService;

@RestController
@RequestMapping("/api")
@Api(value = "hello", description = "REST API description")
public class LauncherController {
    LauncherService service;

    public LauncherController(LauncherService service) {
        this.service = service;
    }

    @ApiOperation(value = "Launch scraper with Parameter")
    @GetMapping("/launcher/{letter}")
    public ResponseEntity launchByLetter(@PathVariable String letter) {
        return ResponseEntity.ok(service.executeByLetter(letter));
    }

    @ApiOperation(value = "Launch to get All tests")
    @GetMapping("/launcher/all")
    public ResponseEntity launchAll() {
        return ResponseEntity.ok(service.executeAll());
    }

    @ApiOperation(value = "Get scrapper status")
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(service.getStatus());
    }

    @ApiOperation(value = "Stop parser. Switch it off.")
    @GetMapping("/stop")
    public ResponseEntity<String> stopParser() {
        service.stop();
        return ResponseEntity.ok(service.getStatus());
    }
}
