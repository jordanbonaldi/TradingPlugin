package net.neferett.tradingplugin.Trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.neferett.redisapi.Annotations.Redis;
import net.neferett.tradingplugin.Trade.Price.PriceAction;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Redis(db = 1, folder = true)
public class Trade {

    @NonNull
    private String pair;

    @NonNull
    private TradeType type;

    @NonNull
    private List<PriceAction> actions;

    @NonNull
    private UUID uuid;

}
