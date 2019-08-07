package net.neferett.tradingplugin.Trade.Enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum TradeType {

    SELL("Short", "Sell"),
    BUY("Long", "Buy");

    @Getter
    private List<String> alias;

    TradeType(String ...alias) {
        this.alias = Arrays.asList(alias);
    }

    public static TradeType getType(String type) {
        return Arrays.stream(values())
                .filter(e -> e.getAlias().stream().filter(a -> a.equalsIgnoreCase(type)).findFirst()
                        .orElse(null) != null).findFirst().orElse(null);
    }
}
