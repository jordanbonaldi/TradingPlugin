package net.neferett.tradingplugin.Routes;

import com.sun.net.httpserver.HttpExchange;
import net.neferett.coreengine.Utils.ClassSerializer;
import net.neferett.httpserver.api.Routing.Route;
import net.neferett.httpserver.api.Routing.RoutingProperties;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Trade;
import org.json.JSONObject;

@Route(name = "/trade", params = {"uuid"})
public class TradeInformation extends RoutingProperties {

    @Override
    public JSONObject routeAction(HttpExchange t)
    {
        Trade trade = TradeManager.getInstance().retrieveTrade(this.params.get("uuid"));

        if (trade == null)
            return new JSONObject().put("error", "Unknown uuid");

        return ClassSerializer.serialize(trade);
    }

}
