package com.domingueti.upfine.components.StockData.interfaces;

import com.domingueti.upfine.components.StockData.dtos.StockIndicatorsDTO;

public interface GetStockData {

    public StockIndicatorsDTO execute(String ticker);

}
