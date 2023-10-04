package com.binanceproject.binance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class BinanceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void binanceGetKlineIntegrationTest() throws Exception {
		String testData = "[{\"symbol\":\"BTCUSDT\",\"klineOpenTime\":1523577600000,\"klineCloseTime\":1523577899999,\"openPrice\":7922.99000000,\"highPrice\":7979.00000000,\"lowPrice\":7919.84000000,\"closePrice\":7978.89000000,\"volume\":254.36429200,\"quoteAssetVolume\":2022014.83427836,\"numberOfTrades\":1318,\"takerBuyBaseAssetVolume\":176.83165800,\"takerBuyQuoteAssetVolume\":1405518.52026997}]";

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/binance/kline")
						.param("symbol", "BTCUSDT")
						.param("startTime", "1523577600000")
						.param("endTime", "1523578200000")
						.param("interval", "5")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$[0].symbol").value("BTCUSDT"))
				.andExpect(jsonPath("$[0].klineOpenTime").value(1523577600000L))
				.andExpect(jsonPath("$[0].openPrice").value(7922.99000000))
				.andExpect(jsonPath("$[0].highPrice").value(7979.00000000))
				.andExpect(jsonPath("$[0].lowPrice").value(7919.84000000))
				.andExpect(jsonPath("$[0].closePrice").value(7978.89000000));
	}





}
