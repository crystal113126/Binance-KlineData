package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static com.binanceproject.binance.model.Kline.covertToKline;

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
            if(!exchangeInfoResponse.contains(symbol)){
                throw new InvalidInputException(String.format("Invalid symbol %s", symbol));
            }
            existingSymbolMap.put(symbol, exchangeInfoResponse);
            return exchangeInfoResponse;
        } else {
            throw new InvalidInputException(String.format("Invalid symbol %s", symbol));
        }
    }

}
