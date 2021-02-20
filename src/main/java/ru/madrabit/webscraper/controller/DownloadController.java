package ru.madrabit.webscraper.controller;

import com.google.common.io.ByteStreams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.madrabit.webscraper.service.DownloadService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/")
@Api(value = "hello", description = "REST API description")
public class DownloadController {

    private final DownloadService service;

    public DownloadController(DownloadService service) {
        this.service = service;
    }

    @ApiOperation(value = "Download files")
    @GetMapping(
            value = "/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public HttpEntity<ByteArrayResource> getFile() throws IOException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=A.1.xlsx");
        InputStream resourceAsStream = getClass().getResourceAsStream("/files/A.1.xlsx");
        ByteArrayResource resource = new ByteArrayResource(ByteStreams.toByteArray(resourceAsStream));
        return new HttpEntity<>(resource, header);
    }

    @ApiOperation(value = "Get list of files")
    @GetMapping(value = "files")
    public ResponseEntity<String> getFilesList() throws IOException {
        List<String> filesList = service.getFilesList();
        return new ResponseEntity<>(
                filesList.toString(),
                HttpStatus.OK
        );
    }
}
