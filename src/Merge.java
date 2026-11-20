/**
 * Top-down mergesort CAI DAT TU BAI GIANG (slide 2.2 Mergesort, trang 7, 8, 18).
 * Cai tien duy nhat duoc them vao: CUTOFF sang insertion sort (slide 18).
 * Khong dung "stop if already sorted" hay "eliminate the copy" de do rieng anh huong cua CUTOFF.
 * CUTOFF la bien static (khong phai final) de script thi nghiem thay doi duoc.
 */
public class Merge {
    public static int CUTOFF = 7;   // CUTOFF = 1 tuong duong mergesort goc (khong cutoff)

    private static boolean less(Comparable v, Comparable w) { return v.compareTo(w) < 0; }

    // slide trang 7
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
    }

    // slide trang 18 (sort voi cutoff)
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo + CUTOFF - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    // slide trang 8
    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    public static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) if (less(a[i], a[i-1])) return false;
        return true;
    }
}
