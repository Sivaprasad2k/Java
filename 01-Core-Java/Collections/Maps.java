import java.util.HashMap;
import java.util.Map;

public class Maps {
    public static void main(String[] args)
    {
      Map<String , Integer> students = new HashMap<>();

      students.put("Siva",22);
      students.put("Prasad",24);
      students.put("Avis",26);
      students.put("Dasarp",28);
      students.put("Siva",30);
      students.put("Avis",32);
      
      for(String key : students.keySet())
      {
        System.out.println(key + " : " + students.get(key));
      }

    

}

}
