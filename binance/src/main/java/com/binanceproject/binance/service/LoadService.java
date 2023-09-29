package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.repository.BinanceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.LongStream;

@Service
public class LoadService {
    private static final int MAX_LIMITED = 500;
    private static final int INTERVAL = 60000;
    @Autowired
    private BinanceRepository binanceRepository;
    @Autowired
    private BinanceApiService binanceApiService;

    /**
     * This method is responsible for loading data from Binance API.
     * It loads 1-min interval data for a given symbol within a specified time range.
     *
     * @param symbol     The symbol for which data needs to be loaded.
     * @param startTime  The start time of the data loading process.
     * @param endTime    The end time of the data loading process.
     */
    public void loadData(@NotNull String symbol, @NotNull Long startTime,  @NotNull  Long endTime) {
        long maxIntervalTime = INTERVAL * MAX_LIMITED;
        LongStream.range(startTime, endTime)
                .filter(t -> (t-startTime)%maxIntervalTime == 0)
                //.sequential()
                .parallel()
                .forEach(t->{
                    long end = t + maxIntervalTime - 1 > endTime ? endTime - 1 : t + maxIntervalTime - 1;
                    insertData(symbol, t, end);
                });
    }

    private void insertData(String symbol, Long startTime, Long endTime) {
        List<Kline> kLineListData = binanceApiService.queryKline(symbol, startTime, endTime);
        binanceRepository.batchInsert(kLineListData);
    }

}
