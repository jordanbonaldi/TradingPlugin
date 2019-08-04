package net.neferett.tradingplugin.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.neferett.coreengine.Processors.Config.Config;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigFile implements Config {

    @NonNull
    private String channel;

    @NonNull
    private List<String> routes;
}