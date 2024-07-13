// TreeNode class represents a node in the binary tree
class TreeNode {
    int val;        // Value of the node (magical coin)
    TreeNode left;  // Left child of the node
    TreeNode right; // Right child of the node

    // Constructor to initialize the node with a value
    TreeNode(int x) {
        val = x;
    }
}

// Result class stores the result of each subtree traversal
class Result {
    int maxSum; // Maximum sum of coins in the subtree
    int min;    // Minimum value in the subtree
    int max;    // Maximum value in the subtree

    // Constructor to initialize the result with maxSum, min, and max values
    Result(int maxSum, int min, int max) {
        this.maxSum = maxSum;
        this.min = min;
        this.max = max;
    }
}

// MagicalGroveFinder class finds the largest magical grove in the binary tree
public class MagicalGroveFinder {

    // Method to find the largest magical grove sum starting from the root node
    public int findLargestMagicalGrove(TreeNode root) {
        Result result = findLargestMagicalGroveHelper(root); // Call helper method to find the result
        return result.maxSum; // Return the maximum sum found
    }

    // Helper method to recursively find the largest magical grove starting from a given node
    private Result findLargestMagicalGroveHelper(TreeNode node) {
        // Base case: If the node is null (no subtree), return a result with 0 sum, and extreme min and max values
        if (node == null) {
            System.out.println("Node is null. Returning result: (0, " + Integer.MAX_VALUE + ", " + Integer.MIN_VALUE + ")");
            return new Result(0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        // Recursively find results for the left and right subtrees
        Result left = findLargestMagicalGroveHelper(node.left);
        Result right = findLargestMagicalGroveHelper(node.right);

        // Print information about the current node and its subtrees (for understanding the recursion)
        System.out.println("Processing node: " + node.val);
        System.out.println("Left subtree result: maxSum = " + left.maxSum + ", min = " + left.min + ", max = " + left.max);
        System.out.println("Right subtree result: maxSum = " + right.maxSum + ", min = " + right.min + ", max = " + right.max);

        // Check if the current node itself can form a valid magical grove
        if (node.val > left.max && node.val < right.min) {
            // Calculate the sum of coins in the subtree rooted at the current node
            int sum = node.val + left.maxSum + right.maxSum;
            // Determine the minimum and maximum values in the subtree rooted at the current node
            int min = Math.min(node.val, left.min);
            int max = Math.max(node.val, right.max);
            // Create a new result with the calculated sum, min, and max values
            Result result = new Result(sum, min, max);
            // Print a message indicating that the current node forms a valid grove with the calculated sum
            System.out.println("Node " + node.val + " forms a valid grove with sum = " + sum);
            // Return the result for the current node
            return result;
        } else {
            // If the current node does not form a valid grove, return the subtree with the maximum sum found so far
            int maxSum = Math.max(left.maxSum, right.maxSum);
            // Create a result with the maximum sum found so far and extreme min and max values
            Result result = new Result(maxSum, Integer.MIN_VALUE, Integer.MAX_VALUE);
            // Print a message indicating that the current node does not form a valid grove
            System.out.println("Node " + node.val + " does not form a valid grove. Returning maxSum = " + maxSum);
            // Return the result for the current node
            return result;
        }
    }

    // Main method to demonstrate usage with an example binary tree
    public static void main(String[] args) {
        // Create an example binary tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);
        root.right.right.left = new TreeNode(4);
        root.right.right.right = new TreeNode(6);

        // Create an instance of MagicalGroveFinder
        MagicalGroveFinder finder = new MagicalGroveFinder();
        // Find the largest magical grove sum starting from the root
        int largestGroveSum = finder.findLargestMagicalGrove(root);
        // Print the largest magical grove sum found
        System.out.println("Largest Magical Grove Sum: " + largestGroveSum); // Output: 20
    }
}
