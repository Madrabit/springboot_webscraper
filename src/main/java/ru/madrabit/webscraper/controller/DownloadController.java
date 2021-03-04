package ru.madrabit.webscraper.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.madrabit.webscraper.exception.InvalidInputException;
import ru.madrabit.webscraper.selenium.consts.SitesConst;
import ru.madrabit.webscraper.service.DownloadService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
@Api(value = "hello", description = "REST API description")
public class DownloadController {

    private final DownloadService service;
    private final SitesConst sites;

    public DownloadController(DownloadService service, SitesConst sites) {
        this.service = service;
        this.sites = sites;
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
            @RequestParam(required = true) String test) throws IOException, InvalidInputException {
        if (test == null || test.isEmpty() ) {
            throw new InvalidInputException("Empty parameter - test");
        }
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + test + ".xlsx");
        final Optional<ByteArrayResource> file = service.getFile(site, test);
        if (file == null) {
            throw new InvalidInputException("Wrong file name or does not exist");
        }
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

    @ApiOperation(value = "Download file")
    @GetMapping(
            value = "/download/folder/",
            produces="application/zip"
    )
    public byte[] downloadFolder(
            @ApiParam(name = "site", required = true, example = "test24ru",
                    allowableValues = "test24ru, test24su")
            @RequestParam(required = true) String site, HttpServletResponse response) throws IOException, InvalidInputException {
        if (!sites.getMap().containsKey(site)) {
            throw new InvalidInputException("Wrong parameter: site.");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + site + ".zip\"");
        return service.getZipFromFolder(site);
    }
}
