package git.tsunami047.tsoulbound.utils;

import git.tsunami047.tsoulbound.TSoulBound;
import git.tsunami047.tsoulbound.event.ConfigBean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (!needToHandle(item)) {
            return -1;
        }

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        for (int i = 0; i < lore.size(); i++) {
            String current = lore.get(i);
            if (current.contains(bindBefore)) {
                lore.set(i, bindLater.replace("(.+?)", playerName).replace("^","").replace("$",""));
                itemMeta.setLore(lore);

                if (Bukkit.isPrimaryThread()) {
                    item.setItemMeta(itemMeta);
                } else {
                    Bukkit.getScheduler().runTask(TSoulBound.plugin, () -> item.setItemMeta(itemMeta));
                }

                return i;
            }
        }

        return -1;
    }

    /**
     * @date 2023/6/15 23:39
     * @param item
     * @param key
     * @return boolean
     * @description 判断一个物品有无绑定标签
     */
    public static boolean isItemLoreHasKey(ItemStack item,String key){
        for (String lore : item.getItemMeta().getLore()) {
            if (lore.contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @date 2023/7/16 22:13
     * @param input
     * @return String
     * @description 取玩家名字
     */
    public static String extractPlayerName(String input) {
        Pattern pattern = Pattern.compile(ConfigBean.bound_lore);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // 如果没有匹配到，则返回空字符串或者抛出异常，具体根据需求决定
        return null;
    }




    /**
     * @date 2023/6/15 19:01
     * @param playerName
     * @param item
     * @return -1 没有匹配到绑定关键词 1 是自己的 0是别人的
     * @description 判断物品属不属于此玩家
     */
    public static int isItemStackOwner(String playerName,ItemStack item){
        if(!ItemUtil.needToHandle(item)){
            return -1;
        }
        List<String> lore = item.getItemMeta().getLore();
        if(lore.size() == 0){
            return -1;
        }
        String s = lore.get(0);
        String s1 = extractPlayerName(s);
        if(s1 !=null){
            if(s1.equals(playerName)){
                return 1;
            }else{
                return 0;
            }
        }
        return -1;
    }

    /**
     * @date 2023/6/15 19:10
     * @param item
     * @return boolean 返回是否成功
     * @description ?
     */
    public static boolean unbind(ItemStack item ){
        if (!needToHandle(item)) {
            return false;
        }
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = item.getItemMeta().getLore();
        int line = -1;
        for (int i = 0; i < lore.size(); i++) {
            String sumLore = lore.get(i);
            String itemPlayerName = extractPlayerName(sumLore);
            if(itemPlayerName !=null){
                line = i;
            }
        }
        lore.remove(line);
        itemMeta.setLore(lore);
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(TSoulBound.plugin,()->item.setItemMeta(itemMeta));
        }else item.setItemMeta(itemMeta);
        return false;
    }
}
