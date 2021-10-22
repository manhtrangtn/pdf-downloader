package com.manhtrangtn.pdfdownloader.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.manhtrangtn.pdfdownloader.model.DownloadBody;
import com.manhtrangtn.pdfdownloader.utils.Constance;
import com.manhtrangtn.pdfdownloader.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class Download {

    private final RestTemplate restTemplate;
    private final Utils utils;

    @PostMapping("prepare")
    public ResponseEntity<String> prepareFile(@RequestBody DownloadBody input) throws IOException, DocumentException {
        List<String> urls = utils.generateUrl(input);
        String fileName = UUID.randomUUID() + Constance.PDF_FILE_EXTENSION;
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        PdfWriter.getInstance(document, new FileOutputStream(Constance.DATA_PATH + fileName));
        document.open();

        Image image;
        Path path;
        int page = 0;
        for (String url : urls) {
            log.info("url: " + url);
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            assert imageBytes != null;
            path = Files.write(Paths.get(utils.generateFileName(page).toString()), imageBytes);
            image = Image.getInstance(path.toString());
            image.scaleToFit(PageSize.A4);
            document.add(image);
            page++;
        }
        document.close();

        return ResponseEntity.ok(fileName);
    }

    @GetMapping("download")
    public @ResponseBody ResponseEntity<Resource> download(@RequestParam String fileName, HttpServletRequest request){

        Resource resource = new FileSystemResource(new File(Constance.DATA_PATH + fileName));
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
