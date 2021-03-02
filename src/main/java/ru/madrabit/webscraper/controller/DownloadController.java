package ru.madrabit.webscraper.controller;

import com.google.common.io.ByteStreams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.madrabit.webscraper.service.DownloadService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
@Api(value = "hello", description = "REST API description")
public class DownloadController {

    private final DownloadService service;

    public DownloadController(DownloadService service) {
        this.service = service;
    }

    @ApiOperation(value = "Download file")
    @GetMapping(
            value = "/download/",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public HttpEntity<ByteArrayResource> getFile(
            @ApiParam(name = "site", required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @RequestParam(required = true) String site,
            @ApiParam(name = "test", required = true,
                    example = "A.1", allowableValues = "A.1, B.1, B.2")
            @RequestParam(required = true) String test) throws IOException {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + test + ".xlsx");
        final Optional<ByteArrayResource> file = service.getFile(site, test);
        return new HttpEntity<>(file.get(), header);
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
