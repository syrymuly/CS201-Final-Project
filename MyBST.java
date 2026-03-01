import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class MyBST<T> {
    private class Node {
        T data;
        Node left;
        Node right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private Comparator<T> comp;

    public MyBST(Comparator<T> comp) {
        this.root = null;
        this.comp = comp;
    }

    // insert adds a node into the tree
    public void insert(T newData) {
        root = insertRec(root, newData);
    }

    private Node insertRec(Node current, T newData) {
        if (current == null) {
            return new Node(newData);
        }

        int cmp = comp.compare(newData, current.data);
        if (cmp < 0) {
            current.left = insertRec(current.left, newData);
        } else if (cmp > 0) {
            current.right = insertRec(current.right, newData);
        } else {
            // equal based on comparator, arbitrarily place it on the left
            current.left = insertRec(current.left, newData);
        }
        return current;
    }

    // getInfo() returns the total number of nodes and the height of the tree
    public String getInfo() {
        int count = countNodes(root);
        int height = calculateHeight(root);
        return "Total nodes: " + count + ", Tree height: " + height;
    }

    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + countNodes(node.left) + countNodes(node.right);
        }
    }

    private int calculateHeight(Node node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
        }
    }

    // search() returns all nodes/records that match the target using the tree's comparator
    public List<T> search(T target) {
        List<T> results = new ArrayList<>();
        searchRec(root, target, results);
        return results;
    }

    private void searchRec(Node node, T target, List<T> results) {
        if (node == null) {
            return;
        }

        int cmp = comp.compare(target, node.data);
        if (cmp < 0) {
            searchRec(node.left, target, results);
        } else if (cmp > 0) {
            searchRec(node.right, target, results);
        } else {
            // exact match is found - add it to results
            results.add(node.data);
            // still need to search the left subtree because duplicates (<= 0) go to the left
            searchRec(node.left, target, results);
        }
    }

    public List<T> getAllRecords() {
        List<T> list = new ArrayList<>();
        getAllNodesRec(root, list);
        return list;
    }

    private void getAllNodesRec(Node node, List<T> list) {
        if (node == null) {
            return;
        }
        getAllNodesRec(node.left, list);
        list.add(node.data);
        getAllNodesRec(node.right, list);
    }

    // Visualization of the tree structure
    public void printConsole() {
        System.out.println("BST Structure:");
        if (root == null) {
            System.out.println("[- empty -]");
        } else {
            printConsoleRec(root, "", true);
        }
    }

    private void printConsoleRec(Node node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "\\-- " : "|-- ") + node.data.toString());
            List<Node> children = new ArrayList<>();
            if (node.left != null)
                children.add(node.left);
            if (node.right != null)
                children.add(node.right);

            for (int i = 0; i < children.size() - 1; i++) {
                printConsoleRec(children.get(i), prefix + (isTail ? "    " : "|   "), false);
            }
            if (children.size() > 0) {
                printConsoleRec(children.get(children.size() - 1), prefix + (isTail ? "    " : "|   "), true);
            }
        }
    }
}
