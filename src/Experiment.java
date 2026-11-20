import java.io.*;
import java.util.*;

/**
 * Thi nghiem tim CUTOFF tot nhat cho mergesort.
 * Moi (kieu du lieu, N): voi moi vong lap (rep), duyet cac gia tri CUTOFF theo thu tu NGAU NHIEN
 * (tranh sai lech do JIT/cache/nhiet), do thoi gian sort (ms) tren ban sao moi cua cung du lieu.
 * Xuat CSV: type,N,cutoff,rep,ms
 */
public class Experiment {
    static final int[] CUTOFFS = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,18,20,22,25,30,35,40,50,64,100};

    public static void main(String[] args) throws Exception {
        String out = args[0];
        int[] sizes = {10_000, 100_000, 1_000_000};
        Random shuffler = new Random(2024);
        try (PrintWriter w = new PrintWriter(new FileWriter(out))) {
            w.println("type,N,cutoff,rep,ms");
            warmup();
            for (int n : sizes) {
                int reps = n <= 10_000 ? 41 : (n <= 100_000 ? 21 : 9);
                for (String type : DataGen.TYPES) {
                    Integer[] master = DataGen.make(type, n, 12345L);
                    List<Integer> order = new ArrayList<>();
                    for (int c : CUTOFFS) order.add(c);
                    for (int rep = 0; rep < reps; rep++) {
                        Collections.shuffle(order, shuffler);
                        for (int c : order) {
                            Merge.CUTOFF = c;
                            Integer[] a = master.clone();
                            long t0 = System.nanoTime();
                            Merge.sort(a);
                            long t1 = System.nanoTime();
                            if (!Merge.isSorted(a)) throw new IllegalStateException("Not sorted! cutoff=" + c);
                            w.printf(Locale.ROOT, "%s,%d,%d,%d,%.4f%n", type, n, c, rep, (t1 - t0) / 1e6);
                        }
                    }
                    w.flush();
                    System.err.println("done " + type + " N=" + n);
                }
            }
        }
    }

    static void warmup() {
        for (int i = 0; i < 40; i++) for (int c : new int[]{1, 7, 10, 20}) {
            Merge.CUTOFF = c;
            Merge.sort(DataGen.make("random", 100_000, i));
        }
    }
}
