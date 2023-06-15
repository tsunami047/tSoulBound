package git.tsunami047.tsoulbound;

import git.tsunami047.tsoulbound.utils.FileConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static git.tsunami047.tsoulbound.MessageUtility.messageUtility;
import static git.tsunami047.tsoulbound.TSoulBound.plugin;


/**
 *@Author: natsumi
 *@CreateTime: 2023-06-04  11:36
 *@Description: ?
 */
public class ConfigManager {

    public static YamlConfiguration basicConfig;
    public static String pluginPrefix;



    /**
     * @date 2023/6/4 11:51
     * @param path

     * @description 加载指定本地磁盘上的yml文件
     */
    private static YamlConfiguration validateAndLoad(String path){
        File configFile = new File(plugin.getDataFolder(), path);
        if (!configFile.exists()) {
            plugin.saveResource(path, false);
        }
        return FileConfig.loadConfiguration(configFile);
    }


    /**
     * @date 2023/6/4 12:28
     * @param folderPath

     * @description 提取jar包内的文件
     */
    private static void extractFolderFiles(String folderPath) {
        File folder = new File(plugin.getDataFolder(), folderPath);
        String[] fileNames = folder.list();
        if (fileNames == null) {
            messageUtility.warning("文件夹 " + folderPath + " 不存在！");
            return;
        }
        for (String fileName : fileNames) {
            plugin.saveResource(folderPath + "/" + fileName, false);
        }
    }

    private static void saveRaceFiles(String dirName,String... fileName){
        for (String s : fileName) {
            String path = dirName+"/"+s+".yml";
            File configFile = new File(plugin.getDataFolder(), path);
            if (!configFile.exists()) {
                plugin.saveResource(path, false);
            }
        }
    }

    /**
     * @date 2023/6/4 11:54


     * @description ?
     */
    public static void loadConfig(){
        basicConfig = validateAndLoad("config.yml");

        //版本号错误插件抛出错误
        double version = basicConfig.getDouble("version");
        Optional.of(version)
                .filter(v -> v == 1.0)
                .orElseThrow(() -> new IllegalArgumentException("Invalid version: " + version));
        pluginPrefix = basicConfig.getString("message.prefix").replaceAll("&","§");


    }

}
