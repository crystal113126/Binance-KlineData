package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.KlineDTO;
import com.binanceproject.binance.repository.BinanceRepository;
import com.binanceproject.binance.repository.KlineDAO;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private KlineDAO klineDAO;


    private static final Logger logger = LoggerFactory.getLogger(GetKlineDataService.class);


    /**
     * Retrieves Kline data for a specific trading symbol within a given time interval and specified interval size.
     *
     * @param symbol        The trading symbol for which to retrieve Kline data.
     * @param startTime     The starting time of the data interval.
     * @param endTime       The ending time of the data interval.
     * @param intervalSize  The size of intervals for grouping Kline data.
     * @return              A list of Kline objects representing the grouped data.
     */
    public List<Kline> getKlineDataFormDB(@NotNull String symbol, @NotNull Long startTime, @NotNull  Long endTime, @NotNull int intervalSize) {
       logger.info("the data read from sql not from redis!!");
        List<Kline> initialData = binanceRepository.findSymbolAndOpenTimeAndCloseTime(symbol, startTime, endTime);
        return IntStream.range(0, initialData.size())
                .parallel()
                .filter(i -> i % intervalSize == 0 )
                .mapToObj(i -> {
                    int endIndex = Math.min(i + intervalSize, initialData.size());
                    return Kline.mapKlineData(initialData, i, endIndex);
                }).toList();
    }

    public List<Kline> getKlineDataInterval(@NotNull String symbol, @NotNull Long startTime, @NotNull Long endTime, @NotNull Integer interval){
        KlineDTO klineDTO = new KlineDTO(symbol, interval, startTime, endTime);
        List<Kline> klineListR = klineDAO.findByKlineDTO(klineDTO);
        if (klineListR != null)
            return klineListR;
        else {
            List<Kline> klineList = getKlineDataFormDB(symbol, startTime, endTime, interval);
            klineDAO.save(klineDTO, klineList);
            return klineList;
        }
    }

}






