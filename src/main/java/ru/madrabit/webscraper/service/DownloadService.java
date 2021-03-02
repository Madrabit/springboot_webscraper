package ru.madrabit.webscraper.service;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DownloadService {
    private final static String s = File.separator;
    private static final String BASE_DIR = System.getProperty("user.dir") + s + "tests" + s;


    public Optional<ByteArrayResource> getFile(String site, String test) {
        Optional<ByteArrayResource> resource = null;
        Path file = Paths.get(BASE_DIR + site + s,test +".xlsx");
        try (InputStream inputStream = Files.newInputStream(file)) {
            resource = Optional.of(new ByteArrayResource(ByteStreams.toByteArray(inputStream)));
        } catch (IOException e) {
            log.error("IO exception getting .xlsx {}", e.getMessage());
        }
        return resource;
    }

    public List<String> getFilesList() throws IOException {
        return Files.list(Paths.get(BASE_DIR))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .map(File::getName)
                .map(s -> s.replaceAll(".xlsx", ""))
                .collect(Collectors.toList());
    }
}
