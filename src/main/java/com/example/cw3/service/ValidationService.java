package com.example.cw3.service;

import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.SocksBatch;

public interface ValidationService {
    public boolean validate(SocksBatch socksBatch);

    public boolean validate(Color color, Size size, int amountCotton);
}
