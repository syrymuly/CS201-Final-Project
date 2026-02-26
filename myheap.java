import java.util.ArrayList;
import java.util.Comparator;

// generic array-based min heap
class MyHeap<T> {
    
    private ArrayList<T> arr;
    private Comparator<? super T> comp;

    public MyHeap() {
        this.arr = new ArrayList<>();
        this.comp = null;
    }

    public MyHeap(Comparator<? super T> comp) {
        this.arr = new ArrayList<>();
        this.comp = comp;
    }

    // safe compare helper
    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comp != null) {
            return comp.compare(a, b);
        }
        return ((Comparable<? super T>) a).compareTo(b);
    }

    public void insert(T val) {
        if (val == null) {
            throw new IllegalArgumentException("no nulls allowed");
        }
        
        arr.add(val);
        
        // bubble up the new item to fix heap property
        // using standard min-heap bubble-up algorithm
        int curr = arr.size() - 1;
        while (curr > 0) {
            int parent = (curr - 1) / 2;
            
            if (compare(arr.get(curr), arr.get(parent)) < 0) {
                swap(curr, parent);
                curr = parent;
            } else {
                break;
            }
        }
    }
    
    // helper to swap
    private void swap(int i, int j) {
        T temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}