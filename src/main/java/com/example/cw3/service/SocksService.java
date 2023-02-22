package com.example.cw3.service;

import com.example.cw3.model.Color;
import com.example.cw3.model.Size;
import com.example.cw3.model.SocksBatch;
import org.springframework.stereotype.Service;

@Service
public interface SocksService {
    int accept(SocksBatch socksBatch);

    int issuance(SocksBatch socksBatch);

    int reject(SocksBatch socksBatch);

    int getCount(Color color, Size size, int amountCotton);

}
