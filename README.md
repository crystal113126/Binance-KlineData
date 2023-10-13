
# Binance-KlineData-API
## Project Description

This project is to stream  a Java Spring Boot scheduling system which extracted Cryptocurrency data from multiple exchanges and converted it into a custom data format.
This api is mainly deal with KlineData from Binance Api.

## EndPoint
### 1. Retrieve Kline Data
- **Endpoint**: `/api/v1/binance/kline`
- **HTTP Method**: GET
- **Description**: Retrieves Kline data for a specific trading symbol within a requested time interval.

  - **Parameters**:
    - `symbol` (String, required): The trading symbol for which Kline data is requested.
    - `startTime` (Long, required): The starting time of the data interval.
    - `endTime` (Long, required): The ending time of the data interval.
    - `interval` (Integer, required): The time range of output data (e.g., 1 for 1 minute).

  - **Response**: A list of Kline objects representing the requested data.
  - The Kline data table:
 
| Field                          | Data Type                      | Description                       |
| ------------------------------ | ------------------------------ | --------------------------------- |
| symbol                         | String                         | The trading symbol                |
| kline_open_time                | Timestamp (e.g., UNIX Epoch)   | Kline open time                   |
| kline_close_time               | Timestamp (e.g., UNIX Epoch)   | Kline close time                  |
| high_price                     | BigDecimal                     | The highest price in the interval |
| open_price                     | BigDecimal                     | The opening price                 |
| low_price                      | BigDecimal                     | The lowest price in the interval  |
| close_price                    | BigDecimal                     | The closing price                 |
| volume                         | BigDecimal                     | The trading volume                |
| quote_asset_volume             | BigDecimal                     | The quote asset volume            |
| number_of_trades               | Integer                        | The number of trades              |
| taker_buy_base_asset_volume    | BigDecimal                     | Taker buy base asset volume       |
| taker_buy_quote_asset_volume   | BigDecimal                     | Taker buy quote asset volume      |


### 2. Load Kline Data
- **Endpoint**: `/api/v1/binance/load`
- **HTTP Method**: POST
- **Description**: Loads Kline data into the MySQL Database for a specified trading symbol and time interval.

  - **Parameters**:
    - `symbol` (String, required): The trading symbol for which to load Kline data.
    - `startTime` (Long, required): The starting time of the data interval to load.
    - `endTime` (Long, required): The ending time of the data interval to load.

  - **Response**: None (HTTP 201 Created if successful).

## Prerequisites
- Proper configuration for the database and other required java springboot.
- Knowledge of the Binance API and the symbols available for Kline data retrieval.

## Usage
The Binance provides endpoints to retrieve Kline data and load data into the MySQL Database. It ensures data validation and integrates with various services to perform these tasks. To use the controller, you'll need to make HTTP requests to the specified endpoints with the required parameters.
## Examples

Here are some example HTTP requests to interact with the Binance Controller:

**Retrieve Kline Data:**
```http
GET /api/v1/binance/kline?symbol=BTCUSDT&startTime=1634127600&endTime=1634149200&interval=1

Replace the parameters in the examples with your specific values.
