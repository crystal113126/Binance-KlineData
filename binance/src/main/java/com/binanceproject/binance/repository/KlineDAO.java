package com.binanceproject.binance.repository;

import com.binanceproject.binance.model.Kline;
import com.binanceproject.binance.model.KlineDTO;
import com.binanceproject.binance.util.InsertTimeLoggerAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KlineDAO {

    private static final Logger logger = LoggerFactory.getLogger(InsertTimeLoggerAspect.class);

    @Autowired
    private RedisTemplate<String, Object> template;
    public static final String HASH_KEY = "Kline";

    public void save(KlineDTO klineDTO, List<Kline> klineList){
        logger.info("redis saved");
        template.opsForHash().put(HASH_KEY,klineDTO,klineList);

    }

    public List<Kline> findByKlineDTO(KlineDTO klineDTO){
        logger.info("redis get");
        return (List<Kline>) template.opsForHash().get(HASH_KEY, klineDTO);
    }

    public String deleteListKline(KlineDTO klineDTO){
        template.opsForHash().delete(HASH_KEY,klineDTO);
        return "List of Kline removed !!";
    }


}
