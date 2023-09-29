package com.binanceproject.binance.repository;

import com.binanceproject.binance.model.Kline;
import jakarta.validation.constraints.NotEmpty;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Mapper
@Validated
public interface BinanceRepository {

    /*@Insert("INSERT INTO kline_data(symbol, kline_open_time, open_price, " +
            "high_price,low_price, close_price, volume, kline_close_time, " +
            "quote_asset_volume, number_of_trades, " +
            "taker_buy_base_asset_volume, taker_buy_quote_asset_volume)" +
            "VALUES (#{symbol}, #{klineOpenTime}, #{openPrice}, #{highPrice}, " +
            "#{lowPrice}, #{closePrice}, #{volume}, #{klineCloseTime}, " +
            "#{quoteAssetVolume}, #{numberOfTrades}, #{takerBuyBaseAssetVolume}, #{takerBuyQuoteAssetVolume})")
    public Integer insert(Kline klineData);
  */

    @Insert("<script>" +
            "INSERT INTO kline_data (symbol, kline_open_time, open_price, " +
            "high_price, low_price, close_price, volume, kline_close_time, " +
            "quote_asset_volume, number_of_trades, " +
            "taker_buy_base_asset_volume, taker_buy_quote_asset_volume) " +
            "VALUES " +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "(#{item.symbol}, #{item.klineOpenTime}, #{item.openPrice}, #{item.highPrice}, " +
            "#{item.lowPrice}, #{item.closePrice}, #{item.volume}, #{item.klineCloseTime}, " +
            "#{item.quoteAssetVolume}, #{item.numberOfTrades}, #{item.takerBuyBaseAssetVolume}, #{item.takerBuyQuoteAssetVolume})" +
            "</foreach>" +
            "</script>")
    public Integer batchInsert(@NotEmpty List<Kline> klineList);

    @Select("SELECT * from kline_data WHERE symbol = #{symbol} And kline_open_time BETWEEN #{klineOpenTime} and #{klineCloseTime} -60000")
    public List<Kline> findSymbolAndOpenTimeAndCloseTime(String symbol, Long klineOpenTime, Long klineCloseTime);

    @Delete("DELETE FROM kline_data WHERE symbol = #{symbol} And kline_open_time = #{klineOpenTime}  And kline_close_time= #{klineCloseTime}")
    public Integer deleteBySymbolOpenCloseTime(String symbol, Long klineOpenTime, Long klineCloseTime);

    @Select("SELECT * from kline_data where symbol = #{symbol}")
    public List<Kline> findAllOfSymbol(String symbol);

    @Select("SELECT * from kline_data WHERE symbol = #{symbol} And kline_open_time = #{klineOpenTime}")
    public Kline findSymbolAndOpenTime(String symbol, Long klineOpenTime);



}
