// generic hashmap using quadratic probing
class MyHashmap<K, V> {
    
    private Entry<K, V>[] table;
    private int size;
    
    // keeps track of soft deletions (tombstones)
    static class Entry<K, V> {
        K key;
        V val;
        boolean deleted;

        Entry(K key, V val) {
            this.key = key;
            this.val = val;
            this.deleted = false;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashmap() {
        table = new Entry[11];
        size = 0;
    }

    private int hash(K key) {
        // bitwise AND prevents negative index from mod
        return (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    public void put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("no null keys");
        }
        
        if ((double) size / table.length >= 0.5) {
            resize();
        }
        
        int home = hash(key);
        int i = 0;
        int insertIdx = -1;
        
        // using standard quadratic probing formula: (hash + i^2) % capacity
        while (i < table.length) {
            int idx = (int) ((home + (long) i * i) % table.length);
            Entry<K, V> curr = table[idx];
            
            if (curr == null) {
                if (insertIdx == -1) insertIdx = idx;
                break;
            }
            
            if (!curr.deleted && curr.key.equals(key)) {
                curr.val = val; 
                return;
            }
            
            // remember first deleted slot to reuse it
            if (curr.deleted && insertIdx == -1) {
                insertIdx = idx;
            }
            
            i++;
        }
        
        if (insertIdx != -1) {
            table[insertIdx] = new Entry<>(key, val);
            size++;
        } else {
            throw new RuntimeException("table full");
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2 + 1];
        size = 0; 
        
        for (Entry<K, V> e : oldTable) {
            // rehash standard alive entries
            if (e != null && !e.deleted) {
                put(e.key, e.val);
            }
        }
    }
}
