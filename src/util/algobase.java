package util;

public class algobase {

    public static<T extends Comparable<T>>int lower_bound(T[]arr, T v) {

        int lo = 0, hi = arr.length;
        int mid;

        while(lo < hi) {
            mid = (lo + hi) / 2;
            if(v.compareTo(arr[mid]) <= 0)
                hi = mid;
            else
                lo = mid + 1;
        }

        return lo;

    }

    private algobase() {}

}
