package git.tsunami047.tsoulbound;

import com.google.common.io.Files;

import git.tsunami047.tsoulbound.utils.DateUtil;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.*;

import static git.tsunami047.tsoulbound.ConfigManager.pluginPrefix;
import static git.tsunami047.tsoulbound.TSoulBound.plugin;


/**
 *@Author: natsumi
 *@CreateTime: 2023-06-04  11:35
 *@Description: ?
 */
public class MessageUtility extends Logger {

     class CustomLogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            builder.append("[tintensify]").append(" ");
            builder.append(formatMessage(record));
            return builder.toString();
        }
    }

    public static MessageUtility messageUtility = new MessageUtility();

    protected MessageUtility() {
        super("soulbound", null);
        setLevel(Level.INFO);
        setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        addHandler(handler);
        CustomLogFormatter formatter = new CustomLogFormatter();
        Handler[] handlers = getHandlers();
        for (Handler handler_ : handlers) {
            handler_.setFormatter(formatter);
        }
    }

    public static MessageUtility getMessageUtility() {
        return messageUtility;
    }

    public static void sendMessage(Object sender, String msg) {
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) {
            return;
        }
        if (sender instanceof Player) {
            ((Player)sender).sendMessage(pluginPrefix + msg.replaceAll("&","§"));
        } else {
            messageUtility.info(msg.replaceAll("§.", ""));
        }
    }

    /**
     * @date 2023/6/7 14:29
     * @param sender
     * @param msg

     * @description 不带信息前缀的信息发送
     */
    public static void sendNonPrefixMessage(Object sender,String msg){
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) {
            return;
        }
        if (sender instanceof Player) {
            ((Player)sender).sendMessage(msg.replaceAll("&","§"));
        } else {
            messageUtility.info(msg.replaceAll("§.", ""));
        }
    }

    /**
     * @date 2023/6/8 14:58
     * @param message

     * @description 不必带日期
     */
    public static void errorToLog(String message){
        logToFile("["+ DateUtil.getDateText()+"] "+message);
    }

    @Override
    public void warning(String msg) {
        errorToLog(msg);
        log(Level.WARNING,"\u001B[31m"+ msg+"\u001B[0m");
    }

    public void warningNotLog(String msg) {
        log(Level.WARNING,"\u001B[31m"+ msg+"\u001B[0m");
    }


    private static void logToFile(String message) {
        try {
            File logFile = new File(plugin.getDataFolder(), "error.log");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            // 将日志消息追加到.log文件中
            Files.append(message + System.lineSeparator(), logFile, Charset.defaultCharset());
        } catch (IOException e) {
            messageUtility.log(Level.SEVERE, "Error writing to log file.", e);
        }
    }
}
