package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.repository.BinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadServiceTest {
    @Mock
    private BinanceRepository repository;
    @Mock
    private BinanceApiService binanceApiService;
    @InjectMocks
    private LoadService loadService = new LoadService();

    @Test
    void testLoadData() {
        //Given
        String symbol = "BTCUSDT";
        long startTime = 1523577600000L; // Example start time
        long endTime = 1523578199999L;   // Example end time


        Kline klineData = new Kline(symbol,1523577600000L,1523578199999L, BigDecimal.valueOf(7922.99),BigDecimal.valueOf(7994.96),BigDecimal.valueOf(7919.84),BigDecimal.valueOf(7990),BigDecimal.valueOf(544.660),BigDecimal.valueOf(8175.9949),950,BigDecimal.valueOf(335.735),BigDecimal.valueOf(73844.13));
        List<Kline> mockKlineListData = Arrays.asList(klineData);

        when(binanceApiService.queryKline(eq(symbol), anyLong(), anyLong())).thenReturn(mockKlineListData);
       // when
        loadService.loadData(symbol, startTime, endTime);

        //Then
        verify(repository).batchInsert(mockKlineListData);

    }
}