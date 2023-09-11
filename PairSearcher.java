import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PairSearcher {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java PairSearcher <file_name> <X> <Y>");
            return;
        }

        String fileName = args[0];
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);

        Data data = new Data();
        data.readFromFile(fileName);

        int depth = data.findPair(x, y);
        if (depth == 0) {
            System.out.println("Cannot find X and Y on the same level.");
            return;
        } else {
            System.out.println("Minimum depth of level containing X and Y: " + depth);
        }
    }
}

class Node {
    int data;
    Node left, right;

    Node(int item) {
        data = item;
        left = right = null;
    }
}

class Data {
    Node root;

    Data() {
        root = null;
    }

    Node insertLevelOrder(int[] arr, Node root, int i, int n) {
        if (i < n) {
            Node temp = new Node(arr[i]);
            root = temp;

            root.left = insertLevelOrder(arr, root.left, 2 * i + 1, n);
            root.right = insertLevelOrder(arr, root.right, 2 * i + 2, n);
        }
        return root;
    }

    int findPair(int x, int y) {
        List<Integer> depthValueX = new ArrayList<>();
        List<Integer> depthValueY = new ArrayList<>();
        
        findDepth(root, x, 1, depthValueX);
        Collections.sort(depthValueX);
        findDepth(root, y, 1, depthValueY);
        Collections.sort(depthValueY);

        for (Integer num : depthValueX) {
            if (depthValueY.contains(num)) {
                return num;
            }
        }
        return 0; // No match found 
    }

    void findDepth(Node node, int x, int level, List<Integer> result) {
        if (node == null) {
            return;
        }

        if (node.data == x) {
            result.add(level);
        }

        findDepth(node.left, x, level + 1, result);
        findDepth(node.right, x, level + 1, result);
    }

    void readFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            try {
                String[] elements = scanner.nextLine().split(" ");
                int[] arr = new int[elements.length];

                for (int i = 0; i < elements.length; i++) {
                    arr[i] = Integer.parseInt(elements[i]);
                }

                root = insertLevelOrder(arr, root, 0, arr.length);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
