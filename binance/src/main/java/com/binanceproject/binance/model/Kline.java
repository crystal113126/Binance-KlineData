package com.binanceproject.binance.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Creates a Kline data model for a trading symbol on Binance.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Kline {

    @NotNull
    private String symbol;

    private Long klineOpenTime;

    private Long klineCloseTime;

    private BigDecimal openPrice;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal closePrice;

    private BigDecimal volume;

    private BigDecimal quoteAssetVolume;

    private Integer numberOfTrades;

    private BigDecimal takerBuyBaseAssetVolume;

    private BigDecimal takerBuyQuoteAssetVolume;


    /**
     * Creates a Kline object from an array of input data and associates it with a specific symbol.
     *
     * @param inputData The input data array containing Kline attributes.
     * @param symbol    The symbol associated with this Kline data.
     * @return          A Kline object representing the input data.
     */
    public static Kline covertToKline(String[] inputData, String symbol) {
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

    /**
     * Combines a list of Kline data into a single Kline object based on a specified range.
     *
     * @param initialData The list of Kline data to combine.
     * @param start       The starting index of the range.
     * @param end         The ending index of the range.
     * @return            A Kline object representing the combined data within the specified range.
     */
    public static Kline mapKlineData(List<Kline> initialData, int start, int end) {
        List<Kline> subList = initialData.subList(start, end);
        Kline firstKline = subList.get(0);
        Kline newData = new Kline();
        int endIndex = subList.size() - 1;
        BigDecimal maxHighPrice = BigDecimal.ZERO;
        BigDecimal minLowPrice = firstKline.getLowPrice();
        BigDecimal volume = BigDecimal.ZERO;
        int sumNumberOfTrades = 0;
        BigDecimal sumQuoteAssetVolume = BigDecimal.ZERO;
        BigDecimal sumTakerBuyBaseAssetVolume = BigDecimal.ZERO;
        BigDecimal sumTakerBuyQuoteAssetVolume = BigDecimal.ZERO;

        newData.setSymbol(firstKline.getSymbol());
        newData.setKlineOpenTime(firstKline.getKlineOpenTime());
        newData.setKlineCloseTime(subList.get(endIndex).getKlineCloseTime());
        newData.setOpenPrice(firstKline.getOpenPrice());
        newData.setClosePrice(subList.get(endIndex).getClosePrice());

        for (Kline kline : subList) {
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
