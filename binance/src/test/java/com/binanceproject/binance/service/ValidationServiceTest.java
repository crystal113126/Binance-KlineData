package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.exception.InvalidInputException;
import com.binanceproject.binance.repository.BinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    @Mock
    private BinanceRepository binanceRepository;
    @Mock
    private BinanceApiService binanceApiService;
    @InjectMocks
    private ValidationService validationService = new ValidationService();

    private static final String EXISTING_SYMBOL = "BTCUSDT";
    private static final String NON_EXISTING_SYMBOL = "INVALID";

    @Test
    public void testIsValidSymbolWithValidSymbol() {
        Mockito.when(binanceApiService.querySymbol(EXISTING_SYMBOL)).thenReturn( "BTCUSDT");
        assertDoesNotThrow(() -> validationService.isValidSymbol(EXISTING_SYMBOL));
    }

    @Test
    public void testIsValidSymbolWithNonValidSymbol() {
        Mockito.when(binanceApiService.querySymbol(NON_EXISTING_SYMBOL))
                .thenThrow(InvalidInputException.class);
        assertThrows(InvalidInputException.class, () -> validationService.isValidSymbol(NON_EXISTING_SYMBOL));
    }

    @Test
    void testIsValidTimeWithValidTime() {
        // Given
        long startTime = 1629450000000L;
        long endTime = 1629459999999L;
        assertDoesNotThrow(() -> validationService.isValidTime(startTime, endTime));
    }

    @Test
    void testIsValidTimeWithNonValidTime() {
        long startTime = 1629460000000L;
        long endTime = 1629459999999L;
        // When & Then
        assertThrows(InvalidInputException.class, () -> validationService.isValidTime(startTime, endTime));
    }

    @Test
    public void testIsExistingSymbolWithExistingSymbol() {
        Kline klineData = new Kline(EXISTING_SYMBOL,1523577600000L,1523578199999L, BigDecimal.valueOf(7922.99),BigDecimal.valueOf(7994.96),BigDecimal.valueOf(7919.84),BigDecimal.valueOf(7990),BigDecimal.valueOf(544.660),BigDecimal.valueOf(8175.9949),950,BigDecimal.valueOf(335.735),BigDecimal.valueOf(73844.13));
        List<Kline> mockList = Arrays.asList(klineData);

        Mockito.when(binanceRepository.findAllOfSymbol(EXISTING_SYMBOL)).thenReturn(mockList);

        assertDoesNotThrow(() -> validationService.isExistingSymbol(EXISTING_SYMBOL));

    }

    @Test
    public void testIsExistingSymbolWithNonExistingSymbol() {
        Mockito.when(binanceRepository.findAllOfSymbol(NON_EXISTING_SYMBOL)).thenReturn(List.of());

        assertThrows(InvalidInputException.class, () -> validationService.isExistingSymbol(NON_EXISTING_SYMBOL));
    }
}