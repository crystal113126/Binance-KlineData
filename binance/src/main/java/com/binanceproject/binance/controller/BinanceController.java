package com.binanceproject.binance.controller;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.service.GetKlineDataService;
import com.binanceproject.binance.service.LoadService;
import com.binanceproject.binance.service.ValidationService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BinanceController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoadService loadService;
    @Autowired
    private GetKlineDataService getKlineDataService;
    @Autowired
    private ValidationService validationService;

    /**
     *Retrieves Kline data for a specific symbol within requested time interval
     *
     * @param symbol    The symbol for which Kline data is requested.
     * @param startTime The starting time of the data interval.
     * @param endTime   The ending time of the data interval
     * @return  A list of Kline objects representing the requested data.
     */

    @GetMapping("/binance")
    @ResponseStatus(HttpStatus.OK)
    public List<Kline> getKlineData(@RequestParam @NotNull String symbol, @RequestParam Long startTime, @RequestParam Long endTime, @RequestParam int interval) {
        // validation
        validationService.isExistingSymbol(symbol);
        validationService.isValidTime(startTime, endTime);
        // Fetch Kline data
        return getKlineDataService.getKlineDataInterval(symbol, startTime, endTime, interval);

    }

    /**
     * Endpoint for loading Kline data into the MySQL Database.
     *
     * @param symbol    The trading symbol for which to load Kline data.
     * @param startTime The starting time of the data interval to load.
     * @param endTime   The ending time of the data interval to load.
     */

    @PutMapping("/binance/load")
    @ResponseStatus(HttpStatus.CREATED)
    public void loadKlineData(@RequestParam @NotNull String symbol, @RequestParam Long startTime, @RequestParam Long endTime) {
        // validation
        validationService.isValidSymbol(symbol);
        validationService.isValidTime(startTime, endTime);
        loadService.loadData(symbol, startTime, endTime);
    }
}
