package git.tsunami047.tsoulbound.event;

import git.tsunami047.tsoulbound.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  18:32
 * @Description: ?
 */
public class ConfigBean {

    public static long delay;
    public static String bind_lore_key;
    //正则表达式了
    public static String bound_lore;

    public static boolean not_allow_click_other_item;
    public static boolean not_allow_throw_out_owner_item;
    public static boolean not_allow_other_interact;
    public static boolean not_allow_pick_up_other_item;


    public static ConfigurationSection section;

    public static void load(YamlConfiguration yaml){
        delay = yaml.getLong("censor-freq");
        bind_lore_key = yaml.getString("bind_lore_key").replace("&","§");
        bound_lore = yaml.getString("bound_lore").replace("&","§").replace("%player_name%","(.+?)");
        not_allow_click_other_item = yaml.getBoolean("not_allow_click_other_item");
        not_allow_throw_out_owner_item = yaml.getBoolean("not_allow_throw_out_owner_item");
        not_allow_other_interact = yaml.getBoolean("not_allow_other_interact");
        not_allow_pick_up_other_item = yaml.getBoolean("not_allow_pick_up_other_item");

        section = yaml.getConfigurationSection("message");
    }

    public static String getMessage(String key){
        return section.getString(key);
    }

    public static void sendMessage(Player player, String key){
        player.sendMessage(ConfigManager.pluginPrefix+getMessage(key).replace("%player_name",player.getName()).replace("&","§"));
    }

}
