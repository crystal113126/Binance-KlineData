package com.binanceproject.binance.service;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BinanceApiServiceTest {

    private Kline klineData = new Kline("BTCUSDT", 1523577660000L, 1523577719999L, BigDecimal.valueOf(7935.54), BigDecimal.valueOf(7954.99), BigDecimal.valueOf(7930.09), BigDecimal.valueOf(7945.67), BigDecimal.valueOf(59), BigDecimal.valueOf(88975.22), 3456, BigDecimal.valueOf(34565.1), BigDecimal.valueOf(3456.1));

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BinanceApiService binanceApiService = new BinanceApiService();


    private static final int MAX_LIMITED = 500;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(binanceApiService, "urlTemplate", "https://www.binance.com/api/v1/klines?symbol=%s&interval=1m&startTime=%s&endTime=%s");
        ReflectionTestUtils.setField(binanceApiService, "urlExchangeInfo", "https://www.binance.com/api/v3/exchangeInfo");
    }

    @Test
    void testQueryKlineSuccess() {
        //Given
        String symbol = "BTCUSDT";
        String[][] data = new String[][]{{"1233", "1235", "12334", "34563", "7896", "1236", "1237", "12335", "3333", "8987", "23345"},
                {"1236", "1237", "12335", "3333", "8987", "1236", "1237", "12335", "3333", "8987", "3344"}};
        String resourceUrl = "https://www.binance.com/api/v1/klines?symbol=BTCUSDT&interval=1m&startTime=1233&endTime=1237";
        //When
        when(restTemplate.getForEntity(resourceUrl, String[][].class)).thenReturn(new ResponseEntity(data, HttpStatus.OK));
        //Then
        List<Kline> actualKlines = binanceApiService.queryKline(symbol, 1233L, 1237L);
        assertThat(actualKlines).isNotNull();
    }


    @Test
    void testQuerySymbolWithExistingSymbol() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String symbol = "BTCUSDT";
        String expectedResponse = symbol;
        // Use reflection to set the existingSymbolMap
        Map<String, String> existingSymbolMap = new HashMap<>();
        existingSymbolMap.put(symbol, expectedResponse);

        Field field = BinanceApiService.class.getDeclaredField("existingSymbolMap");
        field.setAccessible(true);
        field.set(binanceApiService, existingSymbolMap);

        //When
        String response = binanceApiService.querySymbol(symbol);
        //Then
        assertThat(response).isEqualTo(expectedResponse);

    }


    @Test
    void testQuerySymbolWithValidSymbol() {
        //Given
        String symbol = "BTCUSDT";
        String urlExchange = "https://www.binance.com/api/v3/exchangeInfo";
        String expectedResponse = symbol;
        ResponseEntity<String> mockResponse = new ResponseEntity(symbol, HttpStatus.OK);
        //When
        Mockito.when(restTemplate.getForEntity(urlExchange, String.class)).thenReturn(mockResponse);
        String response = binanceApiService.querySymbol(symbol);

        //Then
        assertThat(response).isEqualTo(expectedResponse);

    }

    @Test
    public void testQuerySymbolWithInvalidSymbol() {
        String symbol = "INVALID_SYMBOL";
        String urlExchange = "https://www.binance.com/api/v3/exchangeInfo";
        String responseBody = "BTCUSDT"; // Replace with an appropriate response

        // Mock the behavior of restTemplate.getForEntity
        ResponseEntity<String> mockResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(urlExchange, String.class))
                .thenReturn(mockResponse);

        try {
            binanceApiService.querySymbol(symbol);
            fail("Expected InvalidInputException");
        } catch (InvalidInputException e) {
            // Ensure that the exception is thrown for an invalid symbol
            assertEquals("Invalid symbol " + symbol, e.getMessage());
        }
    }

    @Test
    public void testQuerySymbolWithNotValidSymbolHttpError() {
        // Given
        String symbol = "BTCUSDT";
        String urlExchange = "https://www.binance.com/api/v3/exchangeInfo";
        ResponseEntity<String> mockResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        // When & Then
        Mockito.when(restTemplate.getForEntity(urlExchange, String.class))
                .thenReturn(mockResponse);
        try {
            binanceApiService.querySymbol(symbol);
            fail("Expected InvalidInputException");
        } catch (InvalidInputException e) {
            // Ensure that the exception is thrown for an invalid symbol
            assertEquals("Invalid symbol " + symbol, e.getMessage());
        }
    }

}