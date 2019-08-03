package net.neferett.tradingplugin.Commands;

import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.Commands.Command;
import net.neferett.coreengine.Processors.Plugins.Commands.ExtendableCommand;
import net.neferett.redisapi.RedisAPI;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Price.PriceEnum;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.TradeType;
import net.neferett.tradingplugin.TradingPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command(name = "closeTrade", argsLength = 1, desc="Close an existing trade with UUID",
        help = "<uuid>")
public class closeTrade extends ExtendableCommand {

    @Override
    public boolean onCommand(String... strings) {
        Trade trade = TradeManager.getInstance().retrieveTrade(strings[0]);

        if (trade == null) {
            Logger.log("Unknown trade UUID, try listTrade or findTrade");
            return false;
        }

        TradeManager.getInstance().closeTrade(trade);

        Logger.log("Trade closed successfully");

        return true;
    }

}
