package com.example.cw3.repository;
import com.example.cw3.model.Socks;
import com.example.cw3.model.SocksBatch;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class SocksRepositoryImpl implements SocksRepository{

    private Map<Socks, Integer> socksMap = new HashMap<>();

    @Override
    public int save(SocksBatch socksBatch) {
        Socks socks = socksBatch.getSocks();
        if (socksMap.containsKey(socks)) {
            socksMap.replace(socks, socksMap.get(socks) + socksBatch.getQuantity());
        } else {
            socksMap.put(socks, socksBatch.getQuantity());
        }
        return socksBatch.getQuantity();
    }

    @Override
    public int remove(SocksBatch socksBatch) {
        Socks socks = socksBatch.getSocks();
        if (socksMap.containsKey(socks)) {
            int quantity = socksMap.get(socks);
            if (quantity > socksBatch.getQuantity()) {
                socksMap.replace(socks, quantity - socksBatch.getQuantity());
                return socksBatch.getQuantity();
            } else {
                socksMap.remove(socks);
                return quantity;
            }
        }
        return 0;
    }

    @Override
    public Map<Socks, Integer> getAll() {
        return socksMap;
    }

    @Override
    public List<SocksBatch> getList() {
        List<SocksBatch> socksBatchList = new ArrayList<>();
        for (Map.Entry<Socks, Integer> socksItem : socksMap.entrySet()) {
            socksBatchList.add(new SocksBatch(socksItem.getKey(), socksItem.getValue()));
        }
        return socksBatchList;
    }

    @Override
    public void replace(List<SocksBatch> socksBatchList) {
        socksMap.clear();
        for (SocksBatch batch : socksBatchList) {
            save(batch);
        }
    }
}
