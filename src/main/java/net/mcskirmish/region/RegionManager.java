package net.mcskirmish.region;

import com.google.common.collect.Maps;
import net.mcskirmish.Module;
import net.mcskirmish.SkirmishPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 */
public class RegionManager extends Module {

    private HashMap<String, Region> regions = new HashMap<String, Region>();
    private Logger logger;

    public RegionManager(SkirmishPlugin plugin) {
        super(plugin);
        this.logger = plugin.getLogger();
    }

    @Override
    protected void start() {
        ConfigurationSerialization.registerClass(CuboidRegion.class);
        ConfigurationSerialization.registerClass(CylindricalRegion.class);
        ConfigurationSerialization.registerClass(SphericalRegion.class);
        loadRegions();
    }

    @Override
    protected void stop() {
        saveRegions();
    }

    private void loadRegions() {
        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (file.isDirectory()) {
                if (new File(file, "regions.yml").exists()) {
                    loadRegion(new File(file, "regions.yml"));
                }
            }
        }
    }

    private void loadRegion(File regionConfiguration) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(regionConfiguration);
        for (String regionName : config.getConfigurationSection("regions").getKeys(false)) {
            Region region = (Region) config.get("regions." + regionName);
            region.setRegionName(regionName);
            regions.put(regionName, region);
        }
    }

    private void saveRegions() {
        HashMap<String, YamlConfiguration> configs = Maps.newHashMap();
        for (String regionName : regions.keySet()) {
            Region region = regions.get(regionName);
            YamlConfiguration config = configs.get(region.getWorldName());
            if (config == null) {
                File configFile = new File(Bukkit.getWorldContainer(), region.getWorldName() + "/regions.yml");
                configFile.mkdirs();
                config = new YamlConfiguration();
                configs.put(region.getWorldName(), config);
            }
            config.set("regions." + regionName, region);
        }
        for (String world : configs.keySet()) {
            try {
                configs.get(world).save(new File(Bukkit.getWorldContainer(), world + "/regions.yml"));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Saving the regions.yml data for the world '" + world + "' failed!", e);
            }
        }
    }

    public void setRegion(String regionName, Region region) {
        regions.put(regionName, region);
        region.setRegionName(regionName);
    }

    public Region getRegion(String regionName) {
        return regions.get(regionName);
    }

    public List<Region> getRegionsByWorld(String world) {
        return regions
                .values()
                .stream()
                .filter(region -> region.getWorldName().equals(world))
                .collect(Collectors.toList());
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

}
