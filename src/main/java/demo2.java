import java.util.HashSet;
import java.util.Set;

/**
 * @Author llh
 * @Date 2020/6/20 --- 10:20
 */
public class demo2 {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            set.add(i);
        }
        boolean add = set.add(2);
        boolean add2 = set.add(5);
        boolean add3 = set.add(6);
        boolean add4 = set.add(7);
        boolean add5 = set.add(55);
        System.out.println(add);
        System.out.println(add2);
        System.out.println(add3);
        System.out.println(add4);
        System.out.println(add5);
    }
}
