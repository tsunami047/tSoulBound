package git.tsunami047.tsoulbound.core;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  19:19
 * @Description: ?
 */
public class LinkedHandler {

    private ConcurrentLinkedQueue<UpdateTask> up = new ConcurrentLinkedQueue<>();


    public synchronized void addTask(UpdateTask ut) throws InterruptedException {
        up.offer(ut);
        notify(); // 通知等待的线程有新消息可用
        dequeue();
    }


    public synchronized void dequeue() throws InterruptedException {
        while (up.isEmpty()) {
            wait(); // 等待新消息的到来
        }
        UpdateTask poll = up.poll();
        try{
            poll.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
