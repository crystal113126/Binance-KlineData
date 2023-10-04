package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.repository.BinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetKlineDataServiceTest {

    @InjectMocks
    private GetKlineDataService getKlineDataService;

    @Mock
    private BinanceRepository binanceRepository;
    //Given
    String symbol = "BTCUSDT";
    long startTime = 1523577600000L; // Example start time
    long endTime = 1523578199999L;   // Example end time

    @Test
    void testGetKlineDataIntervalWithIntervalThreeSuccess() {
        //Given
        long startTime = 1523577600000L; // Example start time
        long endTime = 1523577780000L;   // Example end time
        int intervalSize = 3;

        Kline klineData1 = new Kline(symbol,1523577600000L,1523577659999L, BigDecimal.valueOf(7922.99),BigDecimal.valueOf(7991.96),BigDecimal.valueOf(7919.84),BigDecimal.valueOf(7980.11),BigDecimal.valueOf(60),BigDecimal.valueOf(30),50,BigDecimal.valueOf(35),BigDecimal.valueOf(13));
        Kline klineData2 = new Kline(symbol,1523577660000L,1523577719999L, BigDecimal.valueOf(7923.99),BigDecimal.valueOf(7994.96),BigDecimal.valueOf(7920.84),BigDecimal.valueOf(7970.22),BigDecimal.valueOf(60),BigDecimal.valueOf(30),50,BigDecimal.valueOf(33),BigDecimal.valueOf(15));
        Kline klineData3 = new Kline(symbol,1523577720000L,1523577899999L, BigDecimal.valueOf(7921.99),BigDecimal.valueOf(7992.96),BigDecimal.valueOf(7921.84),BigDecimal.valueOf(7990.55),BigDecimal.valueOf(60),BigDecimal.valueOf(30),50,BigDecimal.valueOf(32),BigDecimal.valueOf(12));
        Kline expectedKline = new Kline(symbol,1523577600000L,1523577899999L, BigDecimal.valueOf(7922.99), BigDecimal.valueOf(7994.96),BigDecimal.valueOf(7919.84),BigDecimal.valueOf(7990.55), BigDecimal.valueOf(180),BigDecimal.valueOf(90), 150, BigDecimal.valueOf(100), BigDecimal.valueOf(40));
        List<Kline> mockData = Arrays.asList(klineData1, klineData2, klineData3);
        List<Kline> expectedData = Arrays.asList(expectedKline);
        //When
        when(binanceRepository.findSymbolAndOpenTimeAndCloseTime(symbol, startTime, endTime)).thenReturn(mockData);
        //Then
        List<Kline> result = getKlineDataService.getKlineDataInterval(symbol, startTime, endTime, intervalSize);

        assertThat(result).isEqualTo(expectedData);
        verify(binanceRepository).findSymbolAndOpenTimeAndCloseTime(symbol, startTime, endTime);

    }
}