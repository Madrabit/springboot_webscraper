package ru.madrabit.webscraper;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadFilesTest {

    public List<String> listFilesUsingFilesList(String dir) throws IOException {
        return Files.list(Paths.get(dir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .map(File::getName)
                .map(s -> s.replaceAll(".xlsx", ""))
                .collect(Collectors.toList());
    }

    @Test
    public void filesNameReturn() throws IOException {
        String filePath = System.getProperty("user.dir") + "/tests/";
        listFilesUsingFilesList(filePath);
    }
}
