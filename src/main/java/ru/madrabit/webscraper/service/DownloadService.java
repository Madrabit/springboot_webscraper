package ru.madrabit.webscraper.service;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.madrabit.webscraper.selenium.consts.SitesConst;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class DownloadService {
    private final static String s = File.separator;
    private static final String BASE_DIR = System.getProperty("user.dir") + s + "tests" + s;


    public Optional<ByteArrayResource> getFile(String site, String test) {
        Optional<ByteArrayResource> resource = null;
        Path file = Paths.get(BASE_DIR + site + s, test + ".xlsx");
        try (InputStream inputStream = Files.newInputStream(file)) {
            resource = Optional.ofNullable(new ByteArrayResource(ByteStreams.toByteArray(inputStream)));
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

    public byte[] getZipFromFolder(String site) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
            File f = new File(BASE_DIR + site);
            List<File> files = new ArrayList<>(Arrays.asList(f.listFiles()));
            for (File file : files) {
                ZipEntry e = new ZipEntry(file.getName());
                e.setSize(file.length());
                e.setTime(System.currentTimeMillis());
                zipOutputStream.putNextEntry(e);
                InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                IOUtils.copy(is, zipOutputStream);
                is.close();
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
           log.error("IOException Zip folder {}", e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }
}
