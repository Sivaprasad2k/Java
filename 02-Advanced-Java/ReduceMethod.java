import java.util.Arrays;
import java.util.List;

public class ReduceMethod {
    public static void main(String[] args) {
        
        List<Integer> nums = Arrays.asList(7,10,17,18);

    
        int product = nums.stream()
                          .reduce(1, (a, b) -> a * b);

        System.out.println("Product of numbers: " + product);

    
        int max = nums.stream()
                      .reduce(Integer.MIN_VALUE, Integer::max);

        System.out.println("Max number: " + max);
    }
}
