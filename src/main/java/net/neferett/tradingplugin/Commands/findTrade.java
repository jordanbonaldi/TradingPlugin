package net.neferett.tradingplugin.Commands;

import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.Commands.Command;
import net.neferett.coreengine.Processors.Plugins.Commands.ExtendableCommand;
import net.neferett.coreengine.Utils.ClassSerializer;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Trade;

import java.util.List;

@Command(name = "findTrade", minLength = 1, desc="Find existing trade on specifications",
        help = "<spec1> <spec2>...")
public class findTrade extends ExtendableCommand {

    @Override
    public boolean onCommand(String... strings) {

        List<Trade> trades = TradeManager.getInstance().findTrade(strings);

        if (trades.size() == 0) {
            Logger.log("No trade with the following specifications");
            return false;
        }

        trades.forEach(e -> {
            Logger.log("==============");
            Logger.log(ClassSerializer.serialize(e).toString());
            Logger.log("==============");
        });

        return true;
    }

}
