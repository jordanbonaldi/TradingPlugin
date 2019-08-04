package net.neferett.tradingplugin.Trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.neferett.redisapi.Annotations.Redis;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Price.PriceEnum;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private TradeStatus status;

    @NonNull
    private TradeState state;

    @NonNull
    private List<PriceAction> actions;

    @NonNull
    private UUID uuid;

    public PriceAction getPriceOf(PriceEnum priceEnum) {
        return this.actions.stream().filter(e -> e.getType() == priceEnum).findFirst().orElse(null);
    }

    public void calculProfitAndLose() {
        PriceAction openPrice = this.getPriceOf(PriceEnum.OPEN);
        PriceAction stopPrice = this.getPriceOf(PriceEnum.STOP);
        List<PriceAction> targets = actions.stream().filter(e -> e.getType() == PriceEnum.TARGET).collect(Collectors.toList());

        stopPrice.calculDelta(openPrice.getPrice(), this.type);
        targets.forEach(e -> e.calculDelta(openPrice.getPrice(), this.type));
    }

}
