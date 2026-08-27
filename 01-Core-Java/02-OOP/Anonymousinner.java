public class Anonymousinner {
    public static void main(String x[]) {
        DemoClass obj = new DemoClass() {
            public void show() {
                System.out.println("Anonymous Class Implemented");

            }
        };
        obj.show();

    }
}

abstract class DemoClass {
    public abstract void show();
}

