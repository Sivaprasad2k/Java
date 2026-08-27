import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort {

    public static class Student implements Comparable<Student> {
        int age;
        String name;

        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " : " + age;
        }

        @Override
        public int compareTo(Student that) {
            return Integer.compare(this.age, that.age);
        }
    }

    public static void main(String[] args) {

        // Using Lambda Expression for Comparator
        Comparator<Student> com = (a, b) -> Integer.compare(a.age, b.age);

        List<Student> studs = new ArrayList<>();

        studs.add(new Student(23, "Dasarp"));
        studs.add(new Student(20, "Siva"));
        studs.add(new Student(21, "Prasad"));
        studs.add(new Student(22, "Avis"));

        // Sort using Comparator
        Collections.sort(studs, com);

        for (Student s : studs) {
            System.out.println(s);
        }
    }
}
