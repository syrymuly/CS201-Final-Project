import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DatabaseProcessing {
    private MyBST bst;

    public DatabaseProcessing() {
        this.bst = new MyBST();
    }

    // loadData: takes a file name and loads all data/records into an instance of MyBST
    public void loadData(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming empty lines may exist, we should skip them
                if (!line.trim().isEmpty()) {
                    PeopleRecord record = new PeopleRecord(line);
                    bst.insert(record);
                }
            }
            System.out.println("Data loaded successfully.");
            System.out.println(bst.getInfo()); // Print tree info after loading
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // search: takes given name and family name, uses MyBST search and returns all records that match the names
    public List<PeopleRecord> search(String givenName, String familyName) {
        return bst.search(givenName, familyName);
    }

    // sort: extract from bst, load to heap, and return sorted arraylist
    public List<PeopleRecord> sort() {
        List<PeopleRecord> sortedList = new ArrayList<>();
        try {
            // standard min-heap using custom comparator for given name
            MyHeap<PeopleRecord> heap = new MyHeap<>(new Comparator<PeopleRecord>() {
                public int compare(PeopleRecord r1, PeopleRecord r2) {
                    return r1.getGivenName().compareToIgnoreCase(r2.getGivenName());
                }
            });
            
            // grab all records from the tree using standard in-order traversal
            @SuppressWarnings("unchecked")
            List<PeopleRecord> records = bst.getAllRecords();
            for (PeopleRecord pr : records) {
                heap.insert(pr);
            }
            
            // using standard heap sort concept: pop min continuously
            while (true) {
                try {
                    sortedList.add(heap.remove());
                } catch (IllegalStateException e) {
                    break; // heap is empty
                }
            }
        } catch (Exception e) {
            System.err.println("error during sort: " + e.getMessage());
        }
        return sortedList;
    }

    // getMostFrequentWords: read people.txt and get most frequent words in specific fields
    public List<Map.Entry<String, Integer>> getMostFrequentWords(int count, int len) throws ShortLengthException {
        if (len < 3) {
            System.err.println("error: word length cannot be less than 3");
            throw new ShortLengthException("word length cannot be less than 3");
        }
        
        MyHashmap<String, Integer> map = new MyHashmap<>();
        List<String> uniqueWords = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("people.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                
                // only fields 0 through 6 (given name to state)
                int limit = Math.min(7, parts.length);
                for (int i = 0; i < limit; i++) {
                    // strip punctuation/numbers using standard regex [^a-zA-Z]+
                    String[] words = parts[i].split("[^a-zA-Z]+");
                    for (String w : words) {
                        if (w.length() >= len) {
                            String lower = w.toLowerCase();
                            Integer freq = map.get(lower);
                            
                            if (freq == null) {
                                map.put(lower, 1);
                                uniqueWords.add(lower); // track keys naturally
                            } else {
                                map.put(lower, freq + 1);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("error reading people.txt: " + e.getMessage());
        }
        
        // standard max-heap using custom comparator for frequency
        MyHeap<String> maxHeap = new MyHeap<>(new Comparator<String>() {
            public int compare(String a, String b) {
                // reverse order to create a max-heap
                return map.get(b) - map.get(a);
            }
        });
        
        for (String w : uniqueWords) {
            maxHeap.insert(w);
        }
        
        List<Map.Entry<String, Integer>> results = new ArrayList<>();
        int printed = 0;
        while (printed < count) {
            try {
                String topWord = maxHeap.remove();
                results.add(new AbstractMap.SimpleEntry<>(topWord, map.get(topWord)));
                printed++;
            } catch (IllegalStateException e) {
                break; // heap is empty
            }
        }
        return results;
    }

    public static void main(String[] args) {
        DatabaseProcessing db = new DatabaseProcessing();
        
        // 1. test loadData
        System.out.println("--- testing loadData ---");
        db.loadData("people.txt");
        
        // 2. test search
        System.out.println("\n--- testing search ---");
        List<PeopleRecord> results = db.search("James", "Butt");
        System.out.println("search results for James Butt:");
        for (PeopleRecord pr : results) {
            System.out.println(pr);
        }
        
        // 3. test sort
        System.out.println("\n--- testing sort (heap sort by given name) ---");
        List<PeopleRecord> sorted = db.sort();
        System.out.println("first 5 sorted records:");
        for (int i = 0; i < Math.min(5, sorted.size()); i++) {
            System.out.println(sorted.get(i));
        }
        System.out.println("... skipping middle ...");
        System.out.println("last 5 sorted records:");
        for (int i = Math.max(0, sorted.size() - 5); i < sorted.size(); i++) {
            System.out.println(sorted.get(i));
        }
        
        // 4. test getMostFrequentWords
        System.out.println("\n--- testing getMostFrequentWords ---");
        try {
            List<Map.Entry<String, Integer>> topWords = db.getMostFrequentWords(10, 5); // top 10 words, len >= 5
            System.out.println("top 10 words (length >= 5):");
            for (Map.Entry<String, Integer> entry : topWords) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        } catch (ShortLengthException e) {
            e.printStackTrace();
        }
        
        // 5. test exception
        try {
            System.out.println("\n--- testing exception (len < 3) ---");
            db.getMostFrequentWords(5, 2);
        } catch (ShortLengthException e) {
            System.out.println("caught exception successfully: " + e.getMessage());
        }
    }
}
