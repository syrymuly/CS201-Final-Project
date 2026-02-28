import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
}
