package com.example.cw3.controller;

import com.example.cw3.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/socks/files")
@Tag(name = "API для работы с файлами", description = "Импорт/Экспорт файлов")
@RequiredArgsConstructor
public class FileController {

    private final SocksService socksService;

    @GetMapping("/export")
    @Operation(summary = "Скачать файл")
    public ResponseEntity<InputStreamResource> downloadFile() {
        try {
            File socksFile = socksService.exportFile();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(socksFile));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(socksFile.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename " + socksFile.getName())
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить файл")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        try {
            socksService.importFromFile(file);
            return ResponseEntity.ok("Файл загружен");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
