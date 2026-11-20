import java.io.*;
import java.util.Random;

/** Sinh bo du lieu test (co seed co dinh de tai lap). */
public class DataGen {
    public static final String[] TYPES = {"random", "sorted", "reverse", "nearly_sorted", "few_distinct"};

    public static Integer[] make(String type, int n, long seed) {
        Random r = new Random(seed);
        Integer[] a = new Integer[n];
        switch (type) {
            case "random":        for (int i = 0; i < n; i++) a[i] = r.nextInt(); break;
            case "sorted":        for (int i = 0; i < n; i++) a[i] = i; break;
            case "reverse":       for (int i = 0; i < n; i++) a[i] = n - i; break;
            case "nearly_sorted": // da sap, hoan vi ngau nhien ~1% cap phan tu
                for (int i = 0; i < n; i++) a[i] = i;
                for (int k = 0; n > 0 && k < Math.max(1, n / 100); k++) {
                    int i = r.nextInt(n), j = r.nextInt(n);
                    Integer t = a[i]; a[i] = a[j]; a[j] = t;
                }
                break;
            case "few_distinct":  for (int i = 0; i < n; i++) a[i] = r.nextInt(10); break; // chi 10 gia tri
            default: throw new IllegalArgumentException(type);
        }
        return a;
    }

    /** java DataGen <thu_muc> : ghi file du lieu mau (N = 10^4, 10^5) ra thu muc. */
    public static void main(String[] args) throws IOException {
        File dir = new File(args[0]); dir.mkdirs();
        for (int n : new int[]{10_000, 100_000})
            for (String t : TYPES) {
                Integer[] a = make(t, n, 12345L);
                try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(new File(dir, t + "_" + n + ".txt"))))) {
                    for (Integer x : a) w.println(x);
                }
            }
    }
}
