package git.tsunami047.tsoulbound.core;

import git.tsunami047.tsoulbound.TSoulBound;
import git.tsunami047.tsoulbound.event.ConfigBean;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  19:16
 * @Description: ?
 */
public class TaskHandInPlace {

    public static LinkedHandler handler = new LinkedHandler();
    public static int pauseTime = 999999;

    public static void start() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (true) {
                try {
                    long start = System.currentTimeMillis();
                    handInAllPlayer();
                    long end = System.currentTimeMillis();
                    int timeConsuming  = (int) (end - start);
                    if(timeConsuming<=ConfigBean.delay){
                        Thread.sleep(ConfigBean.delay-timeConsuming);
                        pauseTime += 1000;
                        if(pauseTime>=999999){
                            pauseTime = 999999;
                        }
                    }else{
                        pauseTime -= 1000;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public static void handInAllPlayer() {
        Collection<? extends Player> players = TSoulBound.plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            try {
                Thread.sleep(5);
                addTask(player);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public synchronized static void addTask(Player player) {
        try {
            handler.addTask(new UpdateTask(player));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
