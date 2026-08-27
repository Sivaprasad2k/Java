import java.util.Arrays;
import java.util.List;

public class MethodRefEx {

    public static class Student {
        private String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{name='" + name + "'}";
        }
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Siva", "Avis", "Prasad", "Darsap");

        List<Student> students = names.stream()
                .map(Student::new)
                .toList();

        System.out.println(students);
    }
}
