package com.example.cw3.controller;

import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.SocksBatch;
import com.example.cw3.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "API для учета носков", description = "Операции по учету носков")
@RequiredArgsConstructor
public class SocksController {

    private final SocksService socksService;

    @PostMapping
    @Operation(summary = "Регистрирует приход товара на склад.")
    @ApiResponse(responseCode = "200", description = "Операция выполнена успешно")
    @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<String> accept(@RequestBody SocksBatch socksBatch) {
        int socksCount = socksService.accept(socksBatch);
        return ResponseEntity.ok("Добавлено носков " + socksCount);
    }

    @PutMapping
    @Operation(summary = "Регистрирует отпуск носков со склада.")
    @ApiResponse(responseCode = "200", description = "Операция выполнена успешно")
    @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<String> issuance(@RequestBody SocksBatch socksBatch) {
        int socksCount = socksService.issuance(socksBatch);
        return ResponseEntity.ok("Отпущено носков " + socksCount);
    }

    @GetMapping
    @Operation(summary = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.")
    @ApiResponse(responseCode = "200", description = "Операция выполнена успешно")
    @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<String> getCount(@RequestParam Color color,
                                           @RequestParam Size size,
                                           @RequestParam int amountCottonMin,
                                           @RequestParam int amountCottonMax) {
        int socksCount = socksService.getCount(color, size, amountCottonMin, amountCottonMax);
        return ResponseEntity.ok("Запрошенных носков в наличии " + socksCount);
    }

    @DeleteMapping
    @Operation(summary = "Регистрирует списание испорченных (бракованных) носков")
    @ApiResponse(responseCode = "200", description = "Операция выполнена успешно")
    @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<String> reject(@RequestBody SocksBatch socksBatch) {
        int socksCount = socksService.reject(socksBatch);
        return ResponseEntity.ok("Списано носков " + socksCount);
    }
}
