package net.neferett.tradingplugin.Routes;

import com.sun.net.httpserver.HttpExchange;
import net.neferett.coreengine.Utils.ClassSerializer;
import net.neferett.httpserver.api.Routing.Route;
import net.neferett.httpserver.api.Routing.RoutingProperties;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Trade.Trade;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@Route(name = "/find", params = {"spec"})
public class FindTrade extends RoutingProperties {

    @Override
    public JSONObject routeAction(HttpExchange t)
    {

        List<Trade> trades = TradeManager.getInstance().findTrade(this.params.get("spec").split(","));

        if (trades.size() == 0)
            return new JSONObject().put("error", "No trade found");

        JSONArray array = new JSONArray();

        trades.forEach(e -> array.put(ClassSerializer.serialize(e)));

        return new JSONObject().put("Trades", array);
    }

}
