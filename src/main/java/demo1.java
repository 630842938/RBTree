import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author llh
 * @Date 2020/5/22 --- 11:09
 */
public class demo1 {
    public static void main(String[] args) {
        //直接通过集合的stream接口创建
        List<String> roles = Arrays.asList("鲁班七号", "伽罗", "云中君");
        Stream<String> stream = roles.stream();
        //Arrays.stream() 创建流
        String[] roles2 = {"鲁班七号", "伽罗", "云中君"};
        Stream<String> stream2 = Arrays.stream(roles2);
        //Stream.of() 创建流
        Stream<String> stream3 = Stream.of("鲁班七号", "伽罗", "云中君");



    }
}
