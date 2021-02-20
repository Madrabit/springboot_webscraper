package ru.madrabit.webscraper.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DownloadService {

    private static final String BASE_DIR = System.getProperty("user.dir") + "/tests/";


    public List<String> getFilesList() throws IOException {
        return Files.list(Paths.get(BASE_DIR))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .map(File::getName)
                .map(s -> s.replaceAll(".xlsx", ""))
                .collect(Collectors.toList());
    }
}
