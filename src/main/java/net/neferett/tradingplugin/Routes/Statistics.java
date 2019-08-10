package net.neferett.tradingplugin.Routes;

import com.sun.net.httpserver.HttpExchange;
import net.neferett.coreengine.Utils.ClassSerializer;
import net.neferett.httpserver.api.Routing.Route;
import net.neferett.httpserver.api.Routing.RoutingProperties;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Enums.PriceEnum;
import net.neferett.tradingplugin.Trade.Statistics.Statistic;
import net.neferett.tradingplugin.Trade.Trade;
import net.neferett.tradingplugin.Trade.Enums.TradeState;
import net.neferett.tradingplugin.Trade.Enums.TradeStatus;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.Collectors;

@Route(name = "/stats", params = {"spec"})
public class Statistics extends RoutingProperties {

    @Override
    public JSONObject routeAction(HttpExchange t)
    {
        List<Trade> trades = TradeManager.getInstance()
                .findTrade(this.params.size() > 0 ? this.params.get("spec").split(",") : null);

        return ClassSerializer.serialize(new Statistic().build(trades));
    }

}
