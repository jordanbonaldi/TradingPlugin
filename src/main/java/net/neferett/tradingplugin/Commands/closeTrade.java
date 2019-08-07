package net.neferett.tradingplugin.Commands;

import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.Commands.Command;
import net.neferett.coreengine.Processors.Plugins.Commands.ExtendableCommand;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Trade;

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
