package net.neferett.tradingplugin.Trade;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeState {

    WON("Won"),
    LOST("Lost"),
    NONE("Working");

    private final String state;

}
