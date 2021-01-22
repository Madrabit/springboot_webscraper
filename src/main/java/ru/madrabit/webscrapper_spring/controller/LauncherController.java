package ru.madrabit.webscrapper_spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.madrabit.webscrapper_spring.service.LauncherService;

@RestController
@RequestMapping("/api")
public class LauncherController {
    LauncherService service;

    public LauncherController(LauncherService service) {
        this.service = service;
    }

    @GetMapping("/launcher/{letter}")
    public ResponseEntity launchByLetter(@PathVariable String letter) {
        return ResponseEntity.ok(service.executeByLetter(letter));
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(service.getStatus());
    }
}
