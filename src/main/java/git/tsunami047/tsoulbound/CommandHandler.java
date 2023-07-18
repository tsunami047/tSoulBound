package git.tsunami047.tsoulbound;

import Ly.levelplus.Data;
import Ly.levelplus.Main;
import git.tsunami047.tsoulbound.core.UpdateTask;
import git.tsunami047.tsoulbound.event.ConfigBean;
import git.tsunami047.tsoulbound.utils.ItemUtil;
import git.tsunami047.tsoulbound.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static git.tsunami047.tsoulbound.MessageUtility.sendMessage;
import static git.tsunami047.tsoulbound.TSoulBound.plugin;

/**
 *@Author: natsumi
 *@CreateTime: 2023-06-04  00:58
 *@Description: ?
 */
public class CommandHandler implements CommandExecutor {

    private static void sendHelpMessage(CommandSender sender,boolean delay) {
        if(delay){
            MyThreadPool.asyncExecute(()->{
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sendHelpMessage_(sender);
            });

        }else sendHelpMessage_(sender);
    }


    /**
     * @date 2023/6/7 14:32
     * @param sender

     * @description 发送帮助信息
     */
    private static void sendHelpMessage_(CommandSender sender) {
        MessageUtility.sendNonPrefixMessage(sender," §ftSoulBound Used Help #Power by Tsunami047");
        MessageUtility.sendNonPrefixMessage(sender," §f给予负数则为扣值,[]=可选，<>=必填");
        MessageUtility.sendNonPrefixMessage(sender,"§7- §3/sb §a显示全部命令");
        if (sender instanceof Player && !sender.isOp()) {
            return;
        }
        MessageUtility.sendNonPrefixMessage(sender,"§7- §3/sb check <delay>  §a后面参数单位是毫秒，延迟指定时间立即检测某个玩家的背包");
        MessageUtility.sendNonPrefixMessage(sender,"§7- §3/sb bind  §a绑定手上");
        MessageUtility.sendNonPrefixMessage(sender,"§7- §3/sb unbind §a解除绑定");
        MessageUtility.sendNonPrefixMessage(sender,"§7- §3/sb reload §a重新加载插件");
    }

    /**
     * @date 2023/6/7 14:24
     * @param value
     * @return String
     * @description 格式化，让负号显示
     */
    private static String format(Object value){
        long longWrapper = Long.parseLong(String.valueOf(value));
        if(longWrapper>0){
            return "+"+longWrapper;
        }
        return String.valueOf(longWrapper);
    }

    /**
     * @date 2023/6/7 14:51
     * @param sender
     * @param handlePlayer
     * @param message

     * @description 发送执行完毕的信息
     */
    private static void sendCompleteMessage(CommandSender sender,Player handlePlayer,String message){
        if(sender == handlePlayer){
            sendMessage(handlePlayer,message);
            return;
        }
        sendMessage(sender,"已将 "+handlePlayer.getName()+" ");
        sendMessage(handlePlayer,message);
    }

    private static Player getPlayer(String[] args, int index){
        return args.length>=index+1 ? Bukkit.getPlayer(args[index]) : null;
    }

    public static void main(String[] args) {
        if (null instanceof String) {
            System.out.println(1);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command myCommand, String lable, String[] args) {
        Player sendCommandPlayer = sender instanceof Player ? ((Player) sender) : null;
        if(!(sender instanceof ConsoleCommandSender) && !(sendCommandPlayer != null && sendCommandPlayer.isOp())){
            return true;
        }
        ItemStack itemInHand = null;
        ItemMeta itemMeta = null;
        List<String> lore = null;
        if(sendCommandPlayer!=null && !sendCommandPlayer.isOp()){
            return true;
        }
        if(sender instanceof Player){

            try {
                itemInHand = sendCommandPlayer.getItemInHand();
                itemMeta = itemInHand.getItemMeta();
                lore = itemMeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(args.length==0){
            sendHelpMessage(sender,false);
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "test_abc":
                ConcurrentHashMap<String,Map<UUID, Map<String, Data>>> levelMap = (ConcurrentHashMap<String, Map<UUID, Map<String, Data>>>) ReflectionUtil.getPrivateStaticFieldValue(Main.class, "datas");
                System.out.println(levelMap);
                return true;
            case "check":
                if(args.length>=3){
                    MyThreadPool.asyncExecute(()->
                    {
                        Player player = Bukkit.getPlayer(args[1]);
                        if(player==null){
                            return;
                        }
                        try {
                            Thread.sleep(Integer.parseInt(args[2]));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        new UpdateTask(player).execute();
                    });
                }
                return true;
            case "test":
                if (!ItemUtil.needToHandle_(itemInHand)) {
                    sendMessage(sender, "手上物品不能处理");
                    return true;
                }
                lore.add(ConfigBean.bound_lore.replace("%player_name%","tsunami048"));
                itemMeta.setLore(lore);
                itemInHand.setItemMeta(itemMeta);
                sendMessage(sender, "完成");
                return true;
            case "bind":
                if (!ItemUtil.needToHandle_(itemInHand)) {
                    sendMessage(sender, "手上物品不能处理");
                    return true;
                }
                lore.add(ConfigBean.bind_lore_key);
                itemMeta.setLore(lore);
                itemInHand.setItemMeta(itemMeta);
                sendMessage(sender, "完成");
                return true;
            case "unbind":
                if (!ItemUtil.needToHandle_(itemInHand)) {
                    sendMessage(sender, "手上物品不能处理");
                    return true;
                }

                ItemUtil.unbind(itemInHand);
                sendMessage(sender, "完成");
                return true;
            case "reload":
                if(sender instanceof ConsoleCommandSender){
                    MyThreadPool.asyncExecute(()-> {
                        long l = System.currentTimeMillis();
                        ConfigManager.loadConfig();
                        plugin.afterLoadConfig();
                        sendMessage(sender, "插件异步重载完成，用时 " + String.valueOf(System.currentTimeMillis() - l)+"ms");
                    });
                    return true;
                }
                if (!(sender instanceof Player) || !sender.isOp() || !sender.hasPermission("tsoulbound.admin")) {
                    sendMessage(sender, "执行此指令需要 tsoulbound.admin 权限");
                    return true;
                }
                MyThreadPool.asyncExecute(()-> {
                    long l = System.currentTimeMillis();
                    ConfigManager.loadConfig();
                    plugin.afterLoadConfig();
                    sendMessage(sender, "插件异步重载完成，用时 " + (System.currentTimeMillis() - l) +"ms");
                });
                return true;
            default:
                break;
        }
        sendMessage(sender,"§c指令参数长度太短");
        sendHelpMessage(sender,true);
        return false;
    }


}
