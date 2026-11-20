import java.util.*;
/** Kiem tra tinh dung: so sanh Merge.sort (moi CUTOFF) voi Arrays.sort, gom ca mang rong/1 phan tu. */
public class Test {
    public static void main(String[] args) {
        Random r = new Random(1);
        int fails = 0, tests = 0;
        for (int c : new int[]{1,2,3,5,7,10,16,50,1000})
            for (int n : new int[]{0,1,2,3,7,8,9,10,11,15,16,17,100,1000,12345})
                for (String t : DataGen.TYPES) {
                    Merge.CUTOFF = c;
                    Integer[] a = DataGen.make(t, n, r.nextLong());
                    Integer[] b = a.clone();
                    Merge.sort(a); Arrays.sort(b);
                    tests++;
                    if (!Arrays.equals(a, b)) { fails++; System.out.println("FAIL cutoff=" + c + " n=" + n + " " + t); }
                }
        System.out.println(tests + " tests, " + fails + " failures");
    }
}
