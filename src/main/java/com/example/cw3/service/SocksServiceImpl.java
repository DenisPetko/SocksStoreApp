package com.example.cw3.service;

import com.example.cw3.exception.ValidationException;
import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.Socks;
import com.example.cw3.model.SocksBatch;
import com.example.cw3.repository.SocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final ValidationService validationService;

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
    public int getCount(Color color, Size size, int amountCotton) {
        if (!validationService.validate(color, size, amountCotton)) {
            throw new ValidationException();
        }
        Map<Socks, Integer> socksMap = socksRepository.getAll();
        for (Map.Entry<Socks, Integer> socksItem : socksMap.entrySet()) {
            Socks socks = socksItem.getKey();
            if (socks.getColor().equals(color)
                    && socks.getSize().equals(size)
                    && socks.getAmountCotton() == amountCotton) {
                return socksItem.getValue();
            }
        }
        return 0;
    }

    private void validateSocksBatch(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new ValidationException();
        }
    }
}
