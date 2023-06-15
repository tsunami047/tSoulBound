import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: natsumi
 * @CreateTime: 2023-06-15  23:48
 * @Description: ?
 */
public class test {

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            strings.add("asdas");
        }
        double a = 0;
        for (int i = 0; i < 100; i++) {
            long l = System.nanoTime();
            for (String string : strings) {
                if (string.contains("f")) {
                }
            }
            a +=(double)(System.nanoTime()-l)/1000000;
        }
        System.out.println(a/100+"毫秒");

        a = 0;
        for (int i = 0; i < 1000000; i++) {
            long l = System.nanoTime();
            strings.stream().filter(k->k.contains("f"));
            a +=(double)(System.nanoTime()-l)/1000000;
        }
        System.out.println(a/100+"毫秒");
    }
}
