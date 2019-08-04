package net.neferett.tradingplugin;

import lombok.Getter;
import net.neferett.coreengine.CoreEngine;
import net.neferett.coreengine.Processors.Config.PreConfig;
import net.neferett.coreengine.Processors.Plugins.ExtendablePlugin;
import net.neferett.coreengine.Processors.Plugins.Plugin;
import net.neferett.hookerplugin.HookerManager.HookerManager;
import net.neferett.hookerplugin.HookerPlugin;
import net.neferett.redisapi.RedisAPI;
import net.neferett.tradingplugin.Commands.addTrade;
import net.neferett.tradingplugin.Commands.closeTrade;
import net.neferett.tradingplugin.Commands.findTrade;
import net.neferett.tradingplugin.Config.ConfigFile;
import net.neferett.tradingplugin.Manager.TradeManager;
import net.neferett.tradingplugin.Routes.FindTrade;
import net.neferett.tradingplugin.Routes.TradeInformation;

@Plugin(name = "TradingPlugin", configPath = "TradingPlugin/config.json")
@Getter
public class TradingPlugin extends ExtendablePlugin {

    private RedisAPI redisAPI;

    private ConfigFile configFile;

    private TradeManager tradeManager;

    public static TradingPlugin getInstance() {
        System.out.println("dedans");
        return (TradingPlugin) CoreEngine.getInstance().getPlugin(TradingPlugin.class);
    }

    private void loadConfig() {
        PreConfig preConfig = new PreConfig(this.getConfigPath(), ConfigFile.class);
        this.configFile = (ConfigFile) preConfig.loadPath().loadClazz().getConfig();
    }

    private void addRoute() {
        this.addRoute(TradeInformation.class);
        this.addRoute(FindTrade.class);
    }

    @Override
    public void onEnable() {
        this.loadConfig();

        this.redisAPI = CoreEngine.getInstance().getRedisAPI();

        this.tradeManager = new TradeManager();

        this.addRoute();

        this.addCommand(addTrade.class);
        this.addCommand(closeTrade.class);
        this.addCommand(findTrade.class);
    }

    @Override
    public void onDisable() {

    }

}
