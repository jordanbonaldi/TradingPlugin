package net.neferett.tradingplugin.Trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.neferett.coreengine.CoreEngine;
import net.neferett.coreengine.Processors.Plugins.CorePlugin;
import net.neferett.redisapi.Annotations.Redis;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Price.PriceEnum;
import net.neferett.tradingplugin.TradingPlugin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    private PriceAction createCurrent(BigDecimal price) {
        return new PriceAction(price, PriceEnum.CURRENT, new Date(), new Date(), null, null);
    }

    private void stopTrade(TradeState state) {
        this.setStatus(TradeStatus.CLOSED);
        this.setState(state);

        System.out.println("Trade closed");
    }

    public void calculProfitAndLose() {
        PriceAction openPrice = this.getPriceOf(PriceEnum.OPEN);
        PriceAction stopPrice = this.getPriceOf(PriceEnum.STOP);
        PriceAction actionPrice = this.getPriceOf(PriceEnum.CURRENT);

        List<PriceAction> targets = actions.stream().filter(e -> e.getType() == PriceEnum.TARGET).collect(Collectors.toList());

        stopPrice.calculDelta(openPrice.getPrice(), this.type);
        targets.forEach(e -> e.calculDelta(openPrice.getPrice(), this.type));
        if (actionPrice != null)
            actionPrice.calculDelta(openPrice.getPrice(), this.type);
    }

    public void updatePrice(BigDecimal price) {
        PriceAction action = this.getPriceOf(PriceEnum.CURRENT);

        if (action == null)
            this.actions.add(action = this.createCurrent(price));

        action.setPrice(price);
        action.setUpdatedAt(new Date());

        this.getPriceOf(PriceEnum.OPEN).calculDelta(price, this.type);

        this.calculProfitAndLose();

        if (!updateStopLoss())
            this.updateTargets();

        System.out.println(action);
    }

    private boolean updateStopLoss() {
        PriceAction stopPrice = this.getPriceOf(PriceEnum.STOP);
        PriceAction currentPrice = this.getPriceOf(PriceEnum.CURRENT);

        System.out.println(stopPrice.checkHit(currentPrice, this.type));

        if (!stopPrice.checkHit(currentPrice, this.type))
            return false;

        stopPrice.setClosedAt(new Date());
        stopPrice.setUpdatedAt(new Date());
        this.stopTrade(TradeState.LOST);

        this.removePriceAction();

        return true;
    }

    private void removePriceAction() {
        this.actions.remove(this.getPriceOf(PriceEnum.OPEN));
        this.actions.remove(this.getPriceOf(PriceEnum.CURRENT));
    }

    private void updateTarget(PriceAction target) {
        PriceAction currentPrice = this.getPriceOf(PriceEnum.CURRENT);

        if (!target.checkHit(currentPrice, this.type)) return;

        target.setClosedAt(new Date());
        target.setUpdatedAt(new Date());

        System.out.println("Target hit");
        this.removePriceAction();
    }

    private void updateTargets() {
        List<PriceAction> targets = actions.stream().filter(e -> e.getType() == PriceEnum.TARGET).collect(Collectors.toList());

        targets.forEach(this::updateTarget);

        if (targets.stream().anyMatch(e -> e.getClosedAt() == null))
            return;

        this.stopTrade(TradeState.WON);
    }

}
