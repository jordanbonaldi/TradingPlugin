package net.neferett.tradingplugin.Trade.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PriceEnum {

    OPEN("Open price"),
    TARGET("Target"),
    STOP("Stop loss"),
    CLOSE("Close"),
    CURRENT("Current");

    private final String name;
}
