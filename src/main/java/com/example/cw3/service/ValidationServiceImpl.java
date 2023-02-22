package com.example.cw3.service;

import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.SocksBatch;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(SocksBatch socksBatch) {
        return socksBatch.getSocks() != null
                && socksBatch.getQuantity() != 0
                && socksBatch.getSocks().getColor() != null
                && socksBatch.getSocks().getSize() != null
                && socksBatch.getSocks().getAmountCotton() >= 0 && socksBatch.getSocks().getAmountCotton() <= 100;
    }

    @Override
    public boolean validate(Color color, Size size, int amountCotton) {
        return color != null && size != null && amountCotton >= 0 && amountCotton <= 100;
    }

}
