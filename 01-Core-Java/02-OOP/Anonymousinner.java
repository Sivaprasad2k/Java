public class Anonymousinner {
    public static void main(String x[]) {
        A obj = new A() {
            public void show() {
                System.out.println("Annoymous Class Implemented`");

            }
        };
        obj.show();

    }
}

abstract class A {
    public abstract void show();
}

