package com.binanceproject.binance.service;

import com.binanceproject.binance.model.exception.InvalidInputException;
import com.binanceproject.binance.repository.BinanceRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Service class for validating input data related to Binance operations.
 */

@Service
@Validated
public class ValidationService {
    @Autowired
    private BinanceApiService binanceApiService;
    @Autowired
    private BinanceRepository binanceRepository;

    /**
     * Validates the provided trading symbol to ensure it exists.
     *
     * @param symbol The trading symbol to validate.
     */
    public void isValidSymbol(@NotBlank String symbol) {
        binanceApiService.querySymbol(symbol);
    }

    /**
     * Validates the provided start and end times to ensure that the start time is less than the end time.
     *
     * @param startTime The starting time of the data interval.
     * @param endTime   The ending time of the data interval.
     * @throws InvalidInputException if the start time is not less than the end time.
     */
    public void isValidTime(@NotNull Long startTime, @NotNull Long endTime){
        if (startTime > endTime) {
            throw new InvalidInputException(
                    String.format("The startTime %s should less than endTime %s ", startTime, endTime)
            );
        }
    }

    /**
     * Validates whether a trading symbol exists in the repository.
     *
     * @param symbol The trading symbol to check for existence.
     * @throws InvalidInputException if the symbol is not found.
     */
    public void isExistingSymbol(String symbol) {
        if (binanceRepository.findAllOfSymbol(symbol).isEmpty())
            throw new InvalidInputException(
                    String.format(" This symbol %s is not existing.", symbol ));
    }

}
