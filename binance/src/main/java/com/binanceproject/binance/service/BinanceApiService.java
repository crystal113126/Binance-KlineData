package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service class for interacting with Binance APIs to retrieve Kline data and symbol information.
 */
@Service
public class BinanceApiService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${urlTemplate}")
    private String urlTemplate;
    @Value("${urlExchangeInfo}")
    private String urlExchangeInfo;
    private final Map<String, String> existingSymbolMap = new HashMap<>();
    private static final int MAX_LIMITED = 500;

    /**
     * Retrieves Kline data for a specific symbol within a specified time interval.
     *
     * @param symbol    The trading symbol for which to retrieve Kline data.
     * @param startTime The starting time of the data interval.
     * @param endTime   The ending time of the data interval.
     * @return          A list of Kline objects representing the requested data.
     */
    public List<Kline> queryKline(String symbol, Long startTime, Long endTime){
        String resourceUrl = String.format(urlTemplate, symbol, startTime, endTime, MAX_LIMITED);
        ResponseEntity<String[][]> response = restTemplate.getForEntity(resourceUrl, String[][].class);
        String[][] klineDataSequence = response.getBody();
        assert klineDataSequence != null;
        return Arrays.stream(klineDataSequence)
                .parallel()
                .map(data -> covertToKline(data,symbol))
                .toList();
    }

    /**
     * Queries symbol information for a given symbol, either from the existing symbol map or the Binance API.
     *
     * @param symbol The trading symbol to query.
     * @return       Symbol information as a JSON string.
     * @throws InvalidInputException if the symbol is invalid or not found.
     */
    public String querySymbol (String symbol){
        if(existingSymbolMap.containsKey(symbol)) {
            return existingSymbolMap.get(symbol);
        }
        ResponseEntity<String> response = restTemplate.getForEntity(urlExchangeInfo,String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String exchangeInfoResponse = response.getBody();
            assert exchangeInfoResponse != null;
            if(!exchangeInfoResponse.contains(symbol)){
                throw new InvalidInputException(String.format("Invalid symbol %s", symbol));
            }
            existingSymbolMap.put(symbol, exchangeInfoResponse);
            return exchangeInfoResponse;
        } else {
            throw new InvalidInputException(String.format("Invalid symbol %s", symbol));
        }
    }

    /**
     * Creates a Kline object from an array of input data and associates it with a specific symbol.
     *
     * @param inputData The input data array containing Kline attributes.
     * @param symbol    The symbol associated with this Kline data.
     * @return          A Kline object representing the input data.
     */
    private Kline covertToKline(String[] inputData, String symbol) {
        Kline klineData = new Kline();
        klineData.setSymbol(symbol);
        klineData.setKlineOpenTime(Long.parseLong(inputData[0]));
        klineData.setOpenPrice(new BigDecimal(inputData[1]));
        klineData.setHighPrice(new BigDecimal(inputData[2]));
        klineData.setLowPrice(new BigDecimal(inputData[3]));
        klineData.setClosePrice(new BigDecimal(inputData[4]));
        klineData.setVolume(new BigDecimal(inputData[5]));
        klineData.setKlineCloseTime(Long.parseLong(inputData[6]));
        klineData.setQuoteAssetVolume(new BigDecimal(inputData[7]));
        klineData.setNumberOfTrades(Integer.valueOf(inputData[8]));
        klineData.setTakerBuyBaseAssetVolume(new BigDecimal(inputData[9]));
        klineData.setTakerBuyQuoteAssetVolume(new BigDecimal(inputData[10]));
        return klineData;
    }

}
