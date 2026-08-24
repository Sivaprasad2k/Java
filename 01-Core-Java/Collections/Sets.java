import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Sets {
    public static void main(String a[]) {
        Set<Integer> nums = new TreeSet<>();
         
        // For synchronization and sorted use LinkedHashSet()

        nums.add(50);
        nums.add(20);
        nums.add(30);
        nums.add(40);
        nums.add(10);
        
        Iterator<Integer> values = nums.iterator();
        while(values.hasNext()) {
        System.out.println(values.next());
        }
    }
}
