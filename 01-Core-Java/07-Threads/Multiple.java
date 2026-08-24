public class Multiple {
    public static void main(String[] args) {
        A obj1 = new A();
        B obj2 = new B();

        // Call start() to execute concurrently on separate threads
        obj1.start();
        obj2.start();
    }
}

class A extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Running");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Thread A interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}

class B extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Jogging");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Thread B interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}

