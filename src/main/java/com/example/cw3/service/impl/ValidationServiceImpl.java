package com.example.cw3.service.impl;
import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.SocksBatch;
import com.example.cw3.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(SocksBatch socksBatch) {
        return socksBatch.getSocks() != null
                && socksBatch.getQuantity() != 0
                && socksBatch.getSocks().getColor() != null
                && socksBatch.getSocks().getSize() != null
                && checkCotton(socksBatch.getSocks().getAmountCotton(), socksBatch.getSocks().getAmountCotton());
    }

    @Override
    public boolean validate(Color color, Size size, int amountCottonMin, int amountCottonMax) {
        return color != null && size != null && amountCottonMin >= 0 && amountCottonMax <= 100;
    }

    private boolean checkCotton(int cottonMin, int cottonMax) {
        return cottonMin >= 0 && cottonMax <= 100;
    }

}
