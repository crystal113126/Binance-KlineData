package com.binanceproject.binance.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash
public class KlineDTO implements Serializable {
    @NotBlank
    @Id
    private String symbol;
    @NotNull
    private Integer interval;
    @NotNull
    private Long klineOpenTime;
    @NotNull
    private Long klineCloseTime;

}
