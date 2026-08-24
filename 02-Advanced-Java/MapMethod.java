import java.util.Arrays;
import java.util.List;

public class MapMethod {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Siva", "Prasad", "Avis", "Shevay", "Dasarp");

        System.out.println("Names in Uppercase:");
        names.stream()
             .map(name -> name.toUpperCase())
             .forEach(name -> System.out.print(name + " "));
        System.out.println("\n");

        System.out.println("Name lengths:");
        names.stream()
             .map(String::length)
             .forEach(len -> System.out.print(len + " "));
        System.out.println();
    }
}
