import java.util.ArrayList;
import java.util.List;

class MyBST {
    private PeopleRecord root;

    public MyBST() {
        this.root = null;
    }

    // insert adds a node into the tree
    public void insert(PeopleRecord newRecord) {
        root = insertRec(root, newRecord);
    }

    private PeopleRecord insertRec(PeopleRecord current, PeopleRecord newRecord) {
        if (current == null) {
            return newRecord;
        }

        // compare based on the family name first then the given name
        int cmpFamily = newRecord.getFamilyName().compareToIgnoreCase(current.getFamilyName());

        if (cmpFamily < 0) {
            current.left = insertRec(current.left, newRecord);
        } 
        else if (cmpFamily > 0) {
            current.right = insertRec(current.right, newRecord);
        } 
        else {
            // family names are the same - compare given names
            int cmpGiven = newRecord.getGivenName().compareToIgnoreCase(current.getGivenName());
            if (cmpGiven <= 0) {
                // if even the full name is the same - just place it on the left
                current.left = insertRec(current.left, newRecord);
            } 
            else {
                current.right = insertRec(current.right, newRecord);
            }
        }
        return current;
    }

    // getInfo() returns the total number of nodes and the height of the tree
    public String getInfo() {
        int count = countNodes(root);
        int height = calculateHeight(root);
        return "Total nodes: " + count + ", Tree height: " + height;
    }

    private int countNodes(PeopleRecord node) {
        if (node == null) {
            return 0;
        }
        else {
            return 1 + countNodes(node.left) + countNodes(node.right);
        }
    }

    private int calculateHeight(PeopleRecord node) {
        if (node == null) {
            return 0;
        }
        else {
            return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
        }
    }

    // search() takes given name and family name as parameters, and returns all nodes / records that have the same names
    public List<PeopleRecord> search(String givenName, String familyName) {
        List<PeopleRecord> results = new ArrayList<>();
        searchRec(root, givenName, familyName, results);
        return results;
    }

    private void searchRec(PeopleRecord node, String givenName, String familyName, List<PeopleRecord> results) {
        if (node == null) {
            return;
        }

        int cmpFamily = familyName.compareToIgnoreCase(node.getFamilyName());

        if (cmpFamily < 0) {
            searchRec(node.left, givenName, familyName, results);
        } 
        else if (cmpFamily > 0) {
            searchRec(node.right, givenName, familyName, results);
        } 
        else {
            // family name matches, now check the given name
            int cmpGiven = givenName.compareToIgnoreCase(node.getGivenName());

            if (cmpGiven == 0) {
                // exact match is found - add it to results
                results.add(node);
                // still need to search the left subtree because duplicates (<= 0)
                // go to the left in our insert logic
                searchRec(node.left, givenName, familyName, results);
            }
            else if (cmpGiven < 0) {
                searchRec(node.left, givenName, familyName, results);
            } 
            else {
                searchRec(node.right, givenName, familyName, results);
            }
        }
    }
}
