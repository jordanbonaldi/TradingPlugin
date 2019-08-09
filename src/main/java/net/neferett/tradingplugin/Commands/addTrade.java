package net.neferett.tradingplugin.Commands;

import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.Commands.Command;
import net.neferett.coreengine.Processors.Plugins.Commands.ExtendableCommand;
import net.neferett.hookerplugin.HookerManager.HookerManager;
import net.neferett.hookerplugin.Instances.Pair;
import net.neferett.socialmedia.SocialMedia;
import net.neferett.tradingplugin.Trade.Price.PriceAction;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.Enums.TradeState;
import net.neferett.tradingplugin.Trade.Enums.TradeStatus;
import net.neferett.tradingplugin.Trade.Enums.TradeType;
import net.neferett.tradingplugin.TradingPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Command(name = "addTrade", minLength = 4, desc="Create a new SELL Or BUY Trade",
        help = "<pair> <type> <openPrice> <stopPrice> <target1> <target2...>")
public class addTrade extends ExtendableCommand {

    @Override
    public boolean onCommand(String... strings) {
        String pair = strings[0];
        String type = strings[1];

        PriceAction openPrice = new PriceAction(Float.valueOf(strings[2]), PriceEnum.OPEN);
        PriceAction stopPrice = new PriceAction(Float.valueOf(strings[3]), PriceEnum.STOP);

        List<Float> targets = Arrays.stream(
                Arrays.copyOfRange(strings, 4, strings.length)).map(Float::valueOf).collect(Collectors.toList());

        List<PriceAction> action = new ArrayList<PriceAction>(){{
            add(openPrice);
            add(stopPrice);
        }};

        targets.stream().map(e -> new PriceAction(e, PriceEnum.TARGET)).forEach(action::add);

        TradeType tradeType = TradeType.getType(type);

        if (tradeType == null) {
            Logger.log("Wrong type please choose between:");
            Arrays.stream(TradeType.values()).map(e -> e.getAlias().stream().findAny().orElse(null)).forEach(Logger::log);

            return false;
        }

        HookerManager manager = HookerManager.getInstance();

        Pair pairTrade = manager.getPair(pair);

        if (pairTrade == null) {
            Logger.log("Pair: "+ pair + " doesn't exists");
            return false;
        }

        Trade trade = new Trade(pair, TradeType.getType(type), TradeStatus.OPENED, TradeState.NONE, action, UUID.randomUUID(), pairTrade.getType());

        trade.calculProfitAndLose();

        SocialMedia.getInstance().getBotsManager().sendTrade(trade);

        TradingPlugin.getInstance().getTradeManager().addTrade(trade);

        Logger.log("Trade added successfully");
        return true;
    }

}
