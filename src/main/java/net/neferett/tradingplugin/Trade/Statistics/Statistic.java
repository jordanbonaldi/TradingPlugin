package net.neferett.tradingplugin.Trade.Statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NonNull;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;
import net.neferett.tradingplugin.Trade.Enums.TradeState;
import net.neferett.tradingplugin.Trade.Enums.TradeStatus;
import net.neferett.tradingplugin.Trade.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
public class Statistic {

    private List<Trade> won = new ArrayList<>();
    private List<Trade> lost = new ArrayList<>();
    private List<Trade> current = new ArrayList<>();

    private double totalWon;
    private double totalLost;

    private double currentDelta;
    private double currentLoss;
    private double currentWinning;

    @JsonIgnore
    private List<Trade> given;

    public Statistic build(List<Trade> trades) {
        this.given = trades;

        this.won = this.getSpecificTrades(e -> e.getStatus() == TradeStatus.CLOSED && e.getState() == TradeState.WON);
        this.lost = this.getSpecificTrades(e -> e.getStatus() == TradeStatus.CLOSED && e.getState() == TradeState.LOST);
        this.current = this.getSpecificTrades(e -> e.getStatus() == TradeStatus.OPENED && e.getPriceOf(PriceEnum.CURRENT) != null);

        this.sumDeltas();

        return this;
    }

    private void sumDeltas() {
        this.totalWon = this.won.stream().mapToDouble(Trade::calculateTargetsDelta).sum();
        this.totalLost = this.lost.stream().mapToDouble(Trade::calculateTargetsDelta).sum();

        this.currentDelta = this.current.stream().mapToDouble(e -> e.getPriceOf(PriceEnum.CURRENT).getDelta()).sum();
        this.currentLoss = this.current.stream().mapToDouble(e -> e.getPriceOf(PriceEnum.CURRENT).getDelta()).filter(e -> e < 0).sum();
        this.currentWinning = this.current.stream().mapToDouble(e -> e.getPriceOf(PriceEnum.CURRENT).getDelta()).filter(e -> e > 0).sum();
    }

    private List<Trade> getSpecificTrades(Predicate<Trade> predicate) {
        return this.given.stream().filter(predicate).collect(Collectors.toList());
    }

}
