package com.binanceproject.binance.controller;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.exception.InvalidInputException;
import com.binanceproject.binance.service.GetKlineDataService;
import com.binanceproject.binance.service.LoadService;
import com.binanceproject.binance.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinanceControllerTest {

    @InjectMocks
    private BinanceController binanceController;

    @Mock
    private LoadService loadService;

    @Mock
    private GetKlineDataService getKlineDataService;

    @Mock
    private ValidationService validationService;

    private static final String EXISTING_SYMBOL = "BTCUSDT";
    private static final String NON_EXISTING_SYMBOL = "INVALID";
    private static final int INTERVAL = 3;
    private static final Long VALID_START_TIME = 1523577600000L;
    private static final Long VALID_END_TIME = 1523578199999L;


    @Test
    void testGetKlineDataWithValidParameters() {
        //Given
        Kline klineData = new Kline(EXISTING_SYMBOL,1523577600000L,1523578199999L, BigDecimal.valueOf(7922.99),BigDecimal.valueOf(7994.96),BigDecimal.valueOf(7919.84),BigDecimal.valueOf(7990),BigDecimal.valueOf(544.660),BigDecimal.valueOf(8175.9949),950,BigDecimal.valueOf(335.735),BigDecimal.valueOf(73844.13));
        List<Kline> expectedData = Arrays.asList(klineData);
        //When
        doNothing().when(validationService).isExistingSymbol(EXISTING_SYMBOL);
        doNothing().when(validationService).isValidTime(VALID_START_TIME, VALID_END_TIME);
        when(getKlineDataService.getKlineDataInterval(EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME, INTERVAL))
                .thenReturn(expectedData);
        // Call the controller method
        List<Kline> result = binanceController.getKlineData(EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME, INTERVAL);

        //Then
        assertThat(result).isEqualTo(expectedData);
    }

    @Test
    public void testLoadKlineDataWithValidParameters() {
        //When
        doNothing().when(validationService).isValidSymbol(EXISTING_SYMBOL);
        doNothing().when(validationService).isValidTime(VALID_START_TIME, VALID_END_TIME);

        binanceController.loadKlineData(EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME);

        Mockito.verify(loadService, Mockito.times(1)).loadData(EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME);
    }

    @Test
    public void testGetKlineDataWithInvalidSymbol() {
       // Mockito.when(validationService.isExistingSymbol(NON_EXISTING_SYMBOL)).thenThrow(InvalidInputException.class);;
        doThrow(InvalidInputException.class).when(validationService).isExistingSymbol(NON_EXISTING_SYMBOL);
        assertThrows(InvalidInputException.class, () -> binanceController.getKlineData(NON_EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME, INTERVAL));
    }

    @Test
    public void testLoadKlineDataWithInvalidSymbol() {
        doThrow(InvalidInputException.class).when(validationService).isValidSymbol(NON_EXISTING_SYMBOL);
        assertThrows(InvalidInputException.class, () -> binanceController.loadKlineData(NON_EXISTING_SYMBOL, VALID_START_TIME, VALID_END_TIME));
    }



    }