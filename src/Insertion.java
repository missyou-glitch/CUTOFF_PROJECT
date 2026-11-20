/** Insertion sort (Sedgewick & Wayne, algs4) - co them ham sort(a, lo, hi) dung cho CUTOFF. */
public class Insertion {
    public static void sort(Comparable[] a) { sort(a, 0, a.length - 1); }

    // sap xep doan a[lo..hi]
    public static void sort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }
    private static boolean less(Comparable v, Comparable w) { return v.compareTo(w) < 0; }
    private static void exch(Comparable[] a, int i, int j) { Comparable t = a[i]; a[i] = a[j]; a[j] = t; }
}
