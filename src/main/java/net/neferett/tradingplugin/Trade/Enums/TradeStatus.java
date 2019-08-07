package net.neferett.tradingplugin.Trade.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeStatus {

    OPENED("Open"),
    CLOSED("Closed");

    private final String state;

}
