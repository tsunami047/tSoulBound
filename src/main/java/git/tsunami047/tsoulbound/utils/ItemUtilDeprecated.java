package git.tsunami047.tsoulbound.utils;

import git.tsunami047.tsoulbound.TSoulBound;
import git.tsunami047.tsoulbound.event.ConfigBean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  18:33
 * @Description: ?
 */
public class ItemUtilDeprecated {

    /**
     * @date 2023/6/15 19:08
     * @param item
     * @return boolean
     * @description 判断这个物品是否需要处理
     */
    public static boolean needToHandle(ItemStack item){
        if (item==null || item.getType() == Material.AIR) {
            return false;
        }
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasLore();
    }

    /**
     * @date 2023/6/15 19:08
     * @param item
     * @return boolean
     * @description 判断这个物品是否需要处理
     */
    public static boolean needToHandle_(ItemStack item){
        return item != null && item.getType() != Material.AIR;
    }

    /**
     * @date 2023/6/15 19:08
     * @param item
     * @param key
     * @return Optional<String>
     * @description 获取检测到的包含字符串
     */
    public static Optional<String> getKey(ItemStack item,String key){
        if(!needToHandle(item)){
            return Optional.empty();
        }
        List<String> lore = item.getItemMeta().getLore();
        return lore.stream().filter(i -> i.contains(key)).findFirst();
    }

    /**
     * @date 2023/6/15 19:08
     * @param item
     * @param key
     * @return int
     * @description 获取物品检测到的对应行数
     */
    public static int getItemCheckLine(ItemStack item,String key){
        if(!needToHandle(item)){
            return -1;
        }
        List<String> lore = item.getItemMeta().getLore();
        Optional<String> first = lore.stream().filter(i -> i.contains(key)).findFirst();
        return first.map(lore::indexOf).orElse(-1);
    }

    /**
     * @date 2023/6/15 19:10
     * @param item
     * @param notNameBindLater 需要没有玩家名字的绑定后符号
     * @return boolean 返回是否成功
     * @description ?
     */
    public static boolean unbind(ItemStack item,String notNameBindLater){
        if (!needToHandle(item)) {
            return false;
        }
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();
        Optional<String> first = lore.stream().filter(i -> i.contains(notNameBindLater)).findFirst();
        if (first.isPresent()) {
            lore.remove(first.get());
            itemMeta.setLore(lore);
            if (!Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTask(TSoulBound.plugin,()->item.setItemMeta(itemMeta));
            }else item.setItemMeta(itemMeta);
            return false;
        }
        return false;
    }
}
