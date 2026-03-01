import java.util.ArrayList;
import java.util.Comparator;

// Array-based min-heap. Index-to-parent/child relationships (e.g. parent at (i-1)/2, children at 2*i+1 and 2*i+2), bubble-up on insert, and sink-down on remove follow standard heap algorithms (e.g. "Introduction toAlgorithms" (CLRS))
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

    public T remove() {
        if (arr.isEmpty()) {
            throw new IllegalStateException("heap is empty");
        }
        
        T root = arr.get(0);
        int lastIdx = arr.size() - 1;
        
        if (lastIdx == 0) {
            arr.remove(0);
            return root;
        }
        
        // move last leaf to root to replace deleted min
        T last = arr.remove(lastIdx);
        arr.set(0, last);
        
        // using standard min-heap sink-down logic
        int curr = 0;
        int size = arr.size();
        
        while (2 * curr + 1 < size) {
            int left = 2 * curr + 1;
            int right = left + 1;
            int smallest = left;
            
            if (right < size && compare(arr.get(right), arr.get(left)) < 0) {
                smallest = right;
            }
            
            if (compare(arr.get(curr), arr.get(smallest)) > 0) {
                swap(curr, smallest);
                curr = smallest;
            } else {
                break;
            }
        }
        
        return root;
    }

    // quick printing to see what the heap looks like
    public void printConsole() {
        if (arr.isEmpty()) {
            System.out.println("[- empty -]");
            return;
        }
        
        int levels = (int) (Math.log(arr.size()) / Math.log(2));
        int index = 0;
        
        for (int i = 0; i <= levels; i++) {
            int nodes = (int) Math.pow(2, i);
            int padding = (int) Math.pow(2, levels - i + 1) - 1;
            int spacing = (int) Math.pow(2, levels - i + 2) - 1;
            
            for (int s = 0; s < padding; s++) {
                System.out.print("  ");
            }
            
            for (int j = 0; j < nodes && index < arr.size(); j++) {
                System.out.print("[" + arr.get(index) + "]");
                
                for (int s = 0; s < spacing; s++) {
                    System.out.print("  ");
                }
                index++;
            }
            System.out.println("\n");
        }
    }
}