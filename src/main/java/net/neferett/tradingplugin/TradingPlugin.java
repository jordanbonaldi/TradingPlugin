package net.neferett.tradingplugin;

import lombok.Data;
import net.neferett.coreengine.Processors.Logger.Logger;
import net.neferett.coreengine.Processors.Logger.LoggerChannel;
import net.neferett.coreengine.Processors.Plugins.ExtendablePlugin;
import net.neferett.coreengine.Processors.Plugins.Plugin;

@Data
@Plugin(name = "TradingPlugin", configPath = "TradingPlugin/config.json")
public class TradingPlugin extends ExtendablePlugin {

    @Override
    public void onEnable() {
        Logger.logInChannel("hihi", "Trading");
    }

    @Override
    public void onDisable() {

    }

}
