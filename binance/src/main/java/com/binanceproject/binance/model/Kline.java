package com.binanceproject.binance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Creates a Kline data model for a trading symbol on Binance.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Kline implements Serializable {

    @NotBlank
    private String symbol;
    @NotNull
    private Long klineOpenTime;
    @NotNull
    private Long klineCloseTime;
    @NotNull
    private BigDecimal openPrice;
    @NotNull
    private BigDecimal highPrice;
    @NotNull
    private BigDecimal lowPrice;
    @NotNull
    private BigDecimal closePrice;
    @NotNull
    private BigDecimal volume;
    @NotNull
    private BigDecimal quoteAssetVolume;
    @NotNull
    private Integer numberOfTrades;
    @NotNull
    private BigDecimal takerBuyBaseAssetVolume;
    @NotNull
    private BigDecimal takerBuyQuoteAssetVolume;

    /**
     * Combines a list of Kline data into a single Kline object based on a specified range.
     *
     * @param initialData The list of Kline data to combine.
     * @param start       The starting index of the range.
     * @param end         The ending index of the range.
     * @return            A Kline object representing the combined data within the specified range.
     */
    public static Kline mapKlineData(List<Kline> initialData, Integer start, Integer end) {
//        List<Kline> subList = initialData.subList(start, end);
        Kline firstKline = initialData.get(start);
        Kline endKline = initialData.get(end - 1);
        Kline newData = new Kline();
        BigDecimal maxHighPrice = BigDecimal.ZERO;
        BigDecimal minLowPrice = firstKline.getLowPrice();
        BigDecimal volume = BigDecimal.ZERO;
        int sumNumberOfTrades = 0;
        BigDecimal sumQuoteAssetVolume = BigDecimal.ZERO;
        BigDecimal sumTakerBuyBaseAssetVolume = BigDecimal.ZERO;
        BigDecimal sumTakerBuyQuoteAssetVolume = BigDecimal.ZERO;

        newData.setSymbol(firstKline.getSymbol());
        newData.setKlineOpenTime(firstKline.getKlineOpenTime());
        newData.setKlineCloseTime(endKline.getKlineCloseTime());
        newData.setOpenPrice(firstKline.getOpenPrice());
        newData.setClosePrice(endKline.getClosePrice());

        for (int i = start; i < end; i++) {
            Kline kline = initialData.get(i);
            maxHighPrice = maxHighPrice.max(kline.getHighPrice());
            minLowPrice = minLowPrice.min(kline.getLowPrice());
            volume = volume.add(kline.getVolume());
            sumNumberOfTrades += kline.getNumberOfTrades();
            sumQuoteAssetVolume = sumQuoteAssetVolume.add(kline.getQuoteAssetVolume());
            sumTakerBuyBaseAssetVolume = sumTakerBuyBaseAssetVolume.add(kline.getTakerBuyBaseAssetVolume());
            sumTakerBuyQuoteAssetVolume = sumTakerBuyQuoteAssetVolume.add(kline.getTakerBuyQuoteAssetVolume());
        }

        newData.setHighPrice(maxHighPrice);
        newData.setLowPrice(minLowPrice);
        newData.setVolume(volume);
        newData.setNumberOfTrades(sumNumberOfTrades);
        newData.setQuoteAssetVolume(sumQuoteAssetVolume);
        newData.setTakerBuyBaseAssetVolume(sumTakerBuyBaseAssetVolume);
        newData.setTakerBuyQuoteAssetVolume(sumTakerBuyQuoteAssetVolume);

        return newData;
    }

}
