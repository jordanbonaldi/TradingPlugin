package net.neferett.tradingplugin.Trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.neferett.redisapi.Annotations.Redis;
import net.neferett.tradingplugin.Trade.Enums.TradeState;
import net.neferett.tradingplugin.Trade.Enums.TradeStatus;
import net.neferett.tradingplugin.Trade.Enums.TradeType;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;

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
    private net.neferett.tradingplugin.Trade.Enums.TradeStatus status;

    @NonNull
    private net.neferett.tradingplugin.Trade.Enums.TradeState state;

    @NonNull
    private List<PriceAction> actions;

    @NonNull
    private UUID uuid;

    public PriceAction getPriceOf(PriceEnum priceEnum) {
        return this.actions.stream().filter(e -> e.getType() == priceEnum).findFirst().orElse(null);
    }

    private PriceAction createCurrent(BigDecimal price) {
        return new PriceAction(price, PriceEnum.CURRENT, new Date(), new Date(), false, null, null);
    }

    private void stopTrade(net.neferett.tradingplugin.Trade.Enums.TradeState state) {
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

        if (action == null) {
            action = this.createCurrent(price);

            this.actions.add(action);
        }

        action.setPrice(price);
        action.setUpdatedAt(new Date());

        this.getPriceOf(PriceEnum.OPEN).calculDelta(price, this.type);

        this.calculProfitAndLose();

        if (!updateStopLoss())
            this.updateTargets();

    }

    private boolean updateStopLoss() {
        PriceAction stopPrice = this.getPriceOf(PriceEnum.STOP);
        PriceAction currentPrice = this.getPriceOf(PriceEnum.CURRENT);

        if (!currentPrice.checkHit(stopPrice, this.type))
            return false;

        stopPrice.setDelta(currentPrice.getDelta());
        stopPrice.setClosedAt(new Date());
        stopPrice.setUpdatedAt(new Date());
        this.stopTrade(net.neferett.tradingplugin.Trade.Enums.TradeState.LOST);

        this.removePriceAction();

        return true;
    }

    private void removePriceAction() {
        this.actions.remove(this.getPriceOf(PriceEnum.CURRENT));
    }

    private void updateTarget(PriceAction target) {
        PriceAction currentPrice = this.getPriceOf(PriceEnum.CURRENT);

        if (!target.checkHit(currentPrice, this.type)) return;

        target.setClosedAt(new Date());
        target.setUpdatedAt(new Date());

        target.setHit(true);

        System.out.println("Target hit");
    }

    private void updateTargets() {
        List<PriceAction> targets = actions.stream().filter(e -> e.getType() == PriceEnum.TARGET && !e.isHit()).collect(Collectors.toList());

        targets.forEach(this::updateTarget);

        if (targets.stream().anyMatch(e -> e.getClosedAt() == null))
            return;

        this.stopTrade(TradeState.WON);
        this.removePriceAction();
    }

    public double calculateTargetsDelta() {
        return actions.stream().filter(e -> e.getType() == PriceEnum.TARGET).collect(Collectors.toList()).stream().mapToDouble(PriceAction::getDelta).sum();
    }

}
