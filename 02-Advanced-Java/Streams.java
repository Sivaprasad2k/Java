import java.util.Arrays;
import java.util.List;


public class Streams {
    public static void main(String[] args) {
        
        List<Integer> nums = Arrays.asList(1,2,3,4,5);

       int result = nums.stream()
                      .filter(n -> n % 2 == 0)
                      .map(n -> n * 2)
                      .reduce(0, (a,b) -> a + b);

       System.out.println(result);
}
}