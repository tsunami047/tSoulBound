package git.tsunami047.tsoulbound;

import git.tsunami047.tsoulbound.core.TaskHandInPlace;
import git.tsunami047.tsoulbound.event.*;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TSoulBound extends JavaPlugin {

    public static TSoulBound plugin;

    @Override
    public void onEnable() {
        plugin = this;


        ConfigManager.loadConfig();
        afterLoadConfig();
        TaskHandInPlace.start();
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerPickupItemListener(),this);
        Bukkit.getPluginManager().registerEvents(new AttrEquipmentUpdateListener(),this);
        this.getCommand("tSoulBound").setExecutor(new CommandHandler());
        PlayerInteractListener.restrictLeftClickOnCertainItems();
    }

    @Override
    public void onDisable() {

    }

    public void afterLoadConfig() {
        ConfigBean.load(ConfigManager.basicConfig);
        AttrEquipmentUpdateListener.load();
        PlayerInteractListener.load();
    }
}
