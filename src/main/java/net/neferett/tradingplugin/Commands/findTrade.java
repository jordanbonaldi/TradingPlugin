package net.neferett.tradingplugin.Commands;

import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.Commands.Command;
import net.neferett.coreengine.Processors.Plugins.Commands.ExtendableCommand;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;
import net.neferett.tradingplugin.Trade.Trade;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
            PriceAction openAction = e.getPriceOf(PriceEnum.OPEN);
            PriceAction stopAction = e.getPriceOf(PriceEnum.STOP);
            Logger.log("====== " + e.getUuid() + " ======");
            Logger.log("Pair: " + e.getPair());
            Logger.log("Open: " + openAction.getPrice() + "  " + (openAction.getDelta() != null ? openAction.getDelta() + "%" : ""));
            Logger.log("StopLoss: " + stopAction.getPrice() + "  " + (stopAction.getDelta() != null ? stopAction.getDelta() + "%" : ""));
            AtomicInteger targetNumber = new AtomicInteger(1);
            e.getActions().stream().filter(i -> i.getType() == PriceEnum.TARGET).forEach(a -> Logger.log("Target" + targetNumber.getAndIncrement() + ": " + a.getPrice() + "  " + (a.getDelta() != null ? a.getDelta() + "%" : "")));
            Logger.log("====== " + e.getUuid() + " ======");
        });

        return true;
    }

}
