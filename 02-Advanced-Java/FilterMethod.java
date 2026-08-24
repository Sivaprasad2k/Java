import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FilterMethod {
    public static void main(String[] args) {
        
        List<Integer> nums = Arrays.asList(3,4,5,7,10,17,18);

        
        Predicate<Integer> isEven = n -> n % 2 == 0;

        System.out.println("Even Numbers:");
        nums.stream()
            .filter(isEven)
            .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        
        List<String> names = Arrays.asList("Siva", "Prasad", "Avis", "Shevay", "Dasarp");

        System.out.println("Names starting with 'S':");
        names.stream()
             .filter(name -> name.startsWith("S"))
             .forEach(System.out::println);
    }
}
