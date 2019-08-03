package net.neferett.tradingplugin;

import lombok.Getter;
import net.neferett.coreengine.CoreEngine;
import net.neferett.coreengine.Processors.Config.PreConfig;
import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Plugins.ExtendablePlugin;
import net.neferett.coreengine.Processors.Plugins.Plugin;
import net.neferett.redisapi.RedisAPI;
import net.neferett.tradingplugin.Commands.addTrade;
import net.neferett.tradingplugin.Commands.closeTrade;
import net.neferett.tradingplugin.Config.ConfigFile;
import net.neferett.tradingplugin.Manager.TradeManager;

@Plugin(name = "TradingPlugin", configPath = "TradingPlugin/config.json")
@Getter
public class TradingPlugin extends ExtendablePlugin {

    private RedisAPI redisAPI;

    private ConfigFile configFile;

    private TradeManager tradeManager;

    public static TradingPlugin getInstance() {
        return (TradingPlugin) CoreEngine.getInstance().getPlugin(TradingPlugin.class);
    }

    private void loadConfig() {
        PreConfig preConfig = new PreConfig(this.getConfigPath(), ConfigFile.class);
        this.configFile = (ConfigFile) preConfig.loadPath().loadClazz().getConfig();
    }

    @Override
    public void onEnable() {
        this.loadConfig();

        this.redisAPI = new RedisAPI(this.configFile.getIp(), this.configFile.getPassword(), this.configFile.getRedisPort());
        this.tradeManager = new TradeManager();

        this.addCommand(addTrade.class);
        this.addCommand(closeTrade.class);
    }

    @Override
    public void onDisable() {

    }

}
