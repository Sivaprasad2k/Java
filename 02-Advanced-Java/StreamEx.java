import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StreamEx {

    public static void main(String[] args) {
        int size = 10000;
        List<Integer> nums = new ArrayList<>(size);

        Random ran = new Random();

        for (int i = 0; i < size; i++) {
            nums.add(ran.nextInt(1000));
        }

        long startSeq = System.currentTimeMillis();
        int sum1 = nums.stream()
                        .map(i -> i * 2)
                        .reduce(0, (c, e) -> c + e);
        long endSeq = System.currentTimeMillis();

        long startPara = System.currentTimeMillis();
        int sum2 = nums.parallelStream()
                            .mapToInt(i -> i * 2)
                            .sum();
        long endPara = System.currentTimeMillis();

        System.out.println(sum1 + " Sequential: " + (endSeq - startSeq) + " ms");
        System.out.println(sum2 + " Parallel: " + (endPara - startPara) + " ms");
    }
}