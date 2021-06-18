package search;

public class BinarySearchSpan {
    // Pre :  args.length > 0 && correct input : "x a[1] a[2] ... a[x - 1]" && a is sorted by non-growth
    // Post : left - min : a[left] >= x && right - max : a[right] >= x && right - left = count of x
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        // x == searching element
        int[] a = new int[args.length - 1];
        // a == test array
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        int left = iterationBinarySearch(a, x);
        // left - min : a[left] >= x
        int right = recursionBinarySearch(a, x, -1, a.length);
        // right - max : a[right] >= x
        System.out.print(left + " ");
        System.out.println(right - left + 1);
    }

    // Pre : a[-1] = inf && a[a.length] = -inf && a[i] <= a[i - 1](0 <= i <= a.length)
    // Post : min i : a[i] <= x
    public static int iterationBinarySearch(int[] a, int x) {
        int left = -1;
        int right = a.length;
        // Inv : right' - left' < right - left && a[left'] > x >= a[right']
        while (right - left > 1) {
            // Inv && right - left > 1
            int middle = (left + right) / 2;
            // Inv && right - left > 1 && left < middle < right
            if (a[middle] > x) {
                // Inv && right - left > 1 && left < middle < right && a[middle] > x
                left = middle;
                // Inv && right - left >= 1 && left' = middle && a[left'] > x >= a[right]
            } else {
                // Inv && right - left > 1 && left < middle < right && a[middle] <= x
                right = middle;
                // Inv && right - left >= 1 && right' = middle && a[left] > x >= a[right']
            }
            // left < middle < right -> right' - left' < right - left
        }
        // Post : Inv && right - left == 1 -> right = left - 1 -> right - min i: a[i] <= x
        return right;
    }

    // Pre : a[-1] = inf && a[a.length] = -inf && a[i] <= a[i - 1](0 <= i <= a.length)
    // Post : min i : a[i] <= x
    public static int recursionBinarySearch(int[] a, int x, int left, int right) {
        // Inv : right' - left' < right - left && a[left'] < x <= a[right']
        if (right - left <= 1) {
            // Post : I && right - left == 1 -> right - 1 = left -> left - min i: a[i] <= x
            return left;
        }
        // Inv && right - left > 1
        int middle = (left + right) / 2;
        // Inv && right - left > 1 && left < middle < right
        if (a[middle] >= x) {
            // Inv && right - left > 1 && left < middle < right &&  a[middle] => x > a[right]
            return recursionBinarySearch(a, x, middle, right);
        } else {
            // Inv && right - left > 1 && left < middle < right && a[middle] < x <= a[left]
            return recursionBinarySearch(a, x, left, middle);
        }
    }

}