public class synchronize {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        
        Runnable task1 = () -> {
            for (int i = 1; i <= 1000; i++) {
                counter.increment();
            }
        };

        
        Runnable task2 = () -> {
            for (int i = 1; i <= 1000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

    
        t1.start();
        t2.start();

        
        t1.join();
        t2.join();

    
        System.out.println("Final Count: " + counter.getCount());
    }
}

class Counter {
    private int count;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

