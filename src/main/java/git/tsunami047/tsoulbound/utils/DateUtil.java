package git.tsunami047.tsoulbound.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-08  15:02
 * @Description: ?
 */
public class DateUtil {

    /**
     * @date 2023/6/8 15:03
     * @param time
     * @return String
     * @description 日期转文字格式
     */
    public static String getDateText() {
        long currentTimeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    /**
     * @date 2023/6/8 15:03
     * @param time
     * @return String
     * @description 日期转文字格式
     */
    public static String getDateText(long time){
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    /**
     * @date 2023/6/8 15:03
     * @param time
     * @return String
     * @description 日期转文字格式
     */
    public static String getDateText(long time,String pattern){
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * @date 2023/6/8 15:04
     * @param pattern
     * @return String
     * @description 取当前时间
     */
    public static String getDateText(String pattern){
        long currentTimeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

}
