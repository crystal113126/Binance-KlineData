package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.repository.BinanceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Service class for retrieving Kline data for trading symbols.
 */
@Service
public class GetKlineDataService {

    @Autowired
    private BinanceRepository binanceRepository;

    /**
     * Retrieves Kline data for a specific trading symbol within a given time interval and specified interval size.
     *
     * @param symbol        The trading symbol for which to retrieve Kline data.
     * @param startTime     The starting time of the data interval.
     * @param endTime       The ending time of the data interval.
     * @param intervalSize  The size of intervals for grouping Kline data.
     * @return              A list of Kline objects representing the grouped data.
     */
    public List<Kline> getKlineDataInterval(@NotNull String symbol, @NotNull Long startTime, @NotNull  Long endTime, @NotNull int intervalSize) {
        List<Kline> initialData = binanceRepository.findSymbolAndOpenTimeAndCloseTime(symbol, startTime, endTime);
        return IntStream.range(0, initialData.size())
                .parallel()
                .filter(i -> i % intervalSize == 0 )
                .mapToObj(i -> {
                    int endIndex = Math.min(i + intervalSize, initialData.size());
                    return Kline.mapKlineData(initialData, i, endIndex);
                }).toList();
    }

}






