package com.example.cw3.repository;
import com.example.cw3.model.Socks;
import com.example.cw3.model.SocksBatch;

import java.util.List;
import java.util.Map;

public interface SocksRepository {
    int save(SocksBatch socksBatch);

    int remove(SocksBatch socksBatch);

    Map<Socks, Integer> getAll();

    List<SocksBatch> getList();

    void replace(List<SocksBatch> socksBatchList);
}
