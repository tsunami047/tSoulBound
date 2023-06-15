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
public class ItemUtil {

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
     * @date 2023/6/15 18:34
     * @param item
     * @return int
     * @description 返回-1表示无对应行，0以上为响应行数
     */
    public static int updateItemStack(ItemStack item,String bindBefore,String bindLater,String playerName){
        if(!needToHandle(item)){
            return -1;
        }
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();
        Optional<String> first = lore.stream().filter(i -> i.contains(bindBefore)).findFirst();
        if (first.isPresent()) {
            int i = lore.indexOf(first.get());
            lore.set(i,bindLater.replace("%player_name%",playerName));
            itemMeta.setLore(lore);
            if (!Bukkit.isPrimaryThread()) {
                Bukkit.getScheduler().runTask(TSoulBound.plugin,()->item.setItemMeta(itemMeta));
            }else item.setItemMeta(itemMeta);
            return i;
        }
        return -1;
    }


    /**
     * @date 2023/6/15 19:01
     * @param playerName
     * @param item
     * @param bindLater 需要配置中绑定之后的字符串
     * @return -1 没有匹配到绑定关键词 1 是自己的 0是别人的
     * @description 判断物品属不属于此玩家
     */
    public static int isItemStackOwner(String playerName,ItemStack item,String bindLater){
        List<String> lore = item.getItemMeta().getLore();
        Optional<String> first = lore.parallelStream().filter(i -> i.contains(ConfigBean.bound_key)).findFirst();
        if (first.isPresent()){
            String s = first.get();
            if (s.replace(bindLater, "").equals(playerName)) {
                return 1;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
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
