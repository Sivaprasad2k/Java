import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OptionalEx {
    public static void main(String[] args) {
     List<String> names = Arrays.asList("Siva","Shevay","Prasad","Avis", "Dasarp");
     
     Optional<String> result = names.stream()
          .filter(str->str.contains("x"))
          .findFirst();

     if(result.isPresent())
     {
        System.out.println("Name is "+result.get());
     }
     else
     {
        System.out.println("Name not found");
     }

    }
}
