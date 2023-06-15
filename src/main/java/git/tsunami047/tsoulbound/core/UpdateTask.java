package git.tsunami047.tsoulbound.core;

import git.tsunami047.tsoulbound.ConfigManager;
import git.tsunami047.tsoulbound.TSoulBound;
import git.tsunami047.tsoulbound.event.ConfigBean;
import git.tsunami047.tsoulbound.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static git.tsunami047.tsoulbound.event.ConfigBean.getMessage;
import static git.tsunami047.tsoulbound.utils.ItemUtil.needToHandle;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  18:46
 * @Description: ?
 */
public class UpdateTask {

    private Player player;
    private String playerName;

    public UpdateTask(Player player) {
        this.player = player;
        playerName = player.getName();
    }

    public void execute(){
        if (player.isOp()) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if(!needToHandle(item)){
                continue;
            }
            int itemStackOwner = ItemUtil.isItemStackOwner(playerName, item, ConfigBean.bound_key);
            if (itemStackOwner==0) {
                if (Bukkit.isPrimaryThread()) {
                    inventory.setItem(i, null);
                    player.getWorld().dropItem(player.getLocation(), item);
                    ConfigBean.sendMessage(player,"other_item_throw_out");
                } else {
                    int finalI = i;
                    Bukkit.getScheduler().runTask(TSoulBound.plugin,()->{
                        inventory.setItem(finalI,null);
                        player.getWorld().dropItem(player.getLocation(), item);
                        ConfigBean.sendMessage(player,"other_item_throw_out");
                    });
                }
                continue;
            }
            if (ItemUtil.updateItemStack(item, ConfigBean.bind_lore_key,ConfigBean.bound_lore,playerName)!=-1) {
                if (item.getItemMeta().hasDisplayName()) {
                    player.sendMessage(ConfigManager.pluginPrefix+getMessage("bind_success").replace("%player_name",player.getName()).replace("&","§").replace("%item_name%", item.getItemMeta().getDisplayName()));
                }else {
                    player.sendMessage(ConfigManager.pluginPrefix + getMessage("bind_success").replace("%player_name", player.getName()).replace("&", "§").replace("%item_name%", String.valueOf(item.getTypeId())));
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
