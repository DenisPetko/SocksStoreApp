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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    @Value("${path.to.data.file}")
    private  String dataFilePath;

    @Value("${name.socks.of.data.file}")
    private  String dataFileName;

    private final Path path = Path.of(dataFilePath, dataFileName);
    private final SocksRepository socksRepository;
    private final ValidationService validationService;
    private final FileService fileService;

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
        List<SocksBatch> socksBatchList = fileService.uploadFromFile(file, path, new TypeReference<List<SocksBatch>>() {});
        socksRepository.replace(socksBatchList);
    }

    private void validateSocksBatch(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new ValidationException();
        }
    }
}
