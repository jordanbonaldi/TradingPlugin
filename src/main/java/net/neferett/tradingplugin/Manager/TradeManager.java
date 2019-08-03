package net.neferett.tradingplugin.Manager;

import lombok.Data;
import lombok.SneakyThrows;
import net.neferett.redisapi.Annotations.Redis;
import net.neferett.redisapi.RedisAPI;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Price.PriceEnum;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.TradingPlugin;

import java.util.Date;
import java.util.UUID;

@Data
public class TradeManager {

    private RedisAPI redisAPI = TradingPlugin.getInstance().getRedisAPI();

    public static TradeManager getInstance() {
        return TradingPlugin.getInstance().getTradeManager();
    }

    public void addTrade(Trade trade) {
        this.redisAPI.serialize(trade, trade.getUuid().toString());
    }

    public void closeTrade(Trade trade) {
        PriceAction action = trade.getActions().stream().filter(e -> e.getType() == PriceEnum.OPEN).findFirst().orElse(null);

        if (null == action)
            return;

        action = action.cloneAction();

        action.setClosedAt(new Date());
        action.setType(PriceEnum.CLOSE);

        trade.getActions().add(action);

        this.redisAPI.serialize(trade, trade.getUuid().toString());
    }

    public Trade retrieveTrade(String uuid) {
        return (Trade) this.redisAPI.deSerialize(Trade.class, uuid);
    }
}
