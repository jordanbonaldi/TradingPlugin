package net.neferett.tradingplugin.Manager;

import lombok.Data;
import net.neferett.redisapi.RedisAPI;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Price.PriceEnum;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.TradeStatus;
import net.neferett.tradingplugin.TradingPlugin;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TradeManager {

    private RedisAPI redisAPI = TradingPlugin.getInstance().getRedisAPI();

    public static TradeManager getInstance() {
        return TradingPlugin.getInstance().getTradeManager();
    }

    public void addTrade(Trade trade) {
        trade.calculProfitAndLose();
        this.redisAPI.serialize(trade, trade.getUuid().toString());
    }

    public void closeTrade(Trade trade) {
        PriceAction action = trade.getActions().stream().filter(e -> e.getType() == PriceEnum.OPEN).findFirst().orElse(null);

        if (null == action)
            return;

        Float open = action.getPrice();

        action = action.cloneAction();

        action.setClosedAt(new Date());
        action.setType(PriceEnum.CLOSE);
        action.calculDelta(open, trade.getType());

        trade.getActions().add(action);
        trade.setStatus(TradeStatus.CLOSED);

        this.redisAPI.serialize(trade, trade.getUuid().toString());
    }

    public List<Trade> findTrade(String ...specs) {
        return this.redisAPI.contains(Trade.class, specs).values().stream().map(e -> (Trade)e).collect(Collectors.toList());
    }

    public Trade retrieveTrade(String uuid) {
        return (Trade) this.redisAPI.deSerialize(Trade.class, uuid);
    }
}
