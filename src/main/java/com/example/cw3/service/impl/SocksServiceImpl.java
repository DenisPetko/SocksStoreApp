package com.example.cw3.service.impl;

import com.example.cw3.exception.ValidationException;
import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.Socks;
import com.example.cw3.model.SocksBatch;
import com.example.cw3.repository.SocksRepository;
import com.example.cw3.service.FileService;
import com.example.cw3.service.SocksService;
import com.example.cw3.service.ValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    private final String dataFilePath;
    private final String dataFileName;
    private final Path path;
    private final SocksRepository socksRepository;
    private final ValidationService validationService;
    private final FileService fileService;

    public SocksServiceImpl(SocksRepository socksRepository, ValidationService validationService, FileService fileService,
                            @Value("${path.to.data.file}") String dataFilePath,
                            @Value("${name.socks.of.data.file}") String dataFileName) {
        this.socksRepository = socksRepository;
        this.validationService = validationService;
        this.fileService = fileService;
        this.dataFilePath = dataFilePath;
        this.dataFileName = dataFileName;
        this.path = Path.of(dataFilePath, dataFileName);
    }

    @Override
    public int accept(SocksBatch socksBatch) {
        validateSocksBatch(socksBatch);
        return socksRepository.save(socksBatch);
    }

    @Override
    public int issuance(SocksBatch socksBatch) {
        validateSocksBatch(socksBatch);
        return socksRepository.remove(socksBatch);
    }

    @Override
    public int reject(SocksBatch socksBatch) {
        validateSocksBatch(socksBatch);
        return socksRepository.remove(socksBatch);
    }

    @Override
    public int getCount(Color color, Size size, int amountCottonMin, int amountCottonMax) {
        if (!validationService.validate(color, size, amountCottonMin, amountCottonMax)) {
            throw new ValidationException();
        }
        Map<Socks, Integer> socksMap = socksRepository.getAll();
        int count = 0;
        for (Map.Entry<Socks, Integer> socksItem : socksMap.entrySet()) {
            Socks socks = socksItem.getKey();
            if (socks.getColor().equals(color)
                    && socks.getSize().equals(size)
                    && socks.getAmountCotton() >= amountCottonMin
                    && socks.getAmountCotton() <= amountCottonMax) {
                count = count + socksItem.getValue();
            }
        }
        return count;
    }

    @Override
    public File exportFile() throws IOException {
        return fileService.saveToFile(socksRepository.getList(), path).toFile();
    }

    @Override
    public void importFromFile(MultipartFile file) throws IOException {
        List<SocksBatch> socksBatchList = fileService.uploadFromFile(file, path, new TypeReference<List<SocksBatch>>() {
        });
        socksRepository.replace(socksBatchList);
    }

    private void validateSocksBatch(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new ValidationException();
        }
    }
}
