/*
 * The MovieTheaterSeating class is designed to solve a problem where you need to determine if there are two friends who can sit together in a movie theater based on two constraints: the maximum allowed seat difference between the friends (index difference) and the maximum allowed difference in their movie preferences (value difference). The class includes a method to check this condition and a main method to demonstrate its usage.

Class: MovieTheaterSeating
This class contains two main components:

Method: canSitTogether

Purpose: Checks if there are two friends who can sit together based on the provided seating distance and movie preference constraints.
Parameters:
nums: An array representing seat numbers.
indexDiff: The maximum allowed seat difference between two friends.
valueDiff: The maximum allowed difference in seat numbers for movie preference.
Return: true if there exists a pair of indices (i, j) such that:
The absolute difference between i and j is less than or equal to indexDiff.
The absolute difference between nums[i] and nums[j] is less than or equal to valueDiff.
Otherwise, it returns false.
Implementation:
Iterates through all pairs of indices (i, j) in the array.
For each pair, calculates the seat difference and the movie preference difference.
Checks if both differences are within the allowed limits.
If a valid pair is found, it returns true; otherwise, it continues checking.
If no valid pair is found after all iterations, it returns false.
Method: main

Purpose: Demonstrates the usage of the canSitTogether method.
Parameters: args (Command-line arguments, not used in this example).
Implementation:
Defines an example array of seat numbers (nums).
Defines the maximum allowed seat difference (indexDiff) and movie preference difference (valueDiff).
Calls the canSitTogether method with the example values.
Prints the result to the console.
 */


public class MovieTheaterSeating {

    /**
     * Checks if there are two friends who can sit together based on seating distance
     * and movie preference constraints.
     * 
     * @param nums An array representing seat numbers.
     * @param indexDiff Maximum allowed seat difference between two friends.
     * @param valueDiff Maximum allowed difference in seat numbers for movie preference.
     * @return true if there exists a pair of indices (i, j) such that:
     *         - The absolute difference between i and j is <= indexDiff.
     *         - The absolute difference between nums[i] and nums[j] is <= valueDiff.
     *         false otherwise.
     */
    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        int n = nums.length;

        // Iterate through all pairs (i, j)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Print the pair being checked
                System.out.println("Checking pair (" + i + ", " + j + ")");
                System.out.println("  Seats: " + nums[i] + ", " + nums[j]);

                // Calculate the seat difference between indices i and j
                int seatDifference = Math.abs(i - j);
                
                // Check if the seat difference is within the allowed index difference
                if (seatDifference <= indexDiff) {
                    System.out.println("  Seat difference (" + seatDifference + ") within indexDiff (" + indexDiff + ")");

                    // Calculate the movie preference difference between seat numbers nums[i] and nums[j]
                    int movieDifference = Math.abs(nums[i] - nums[j]);
                    
                    // Check if the movie preference difference is within the allowed value difference
                    if (movieDifference <= valueDiff) {
                        System.out.println("  Movie difference (" + movieDifference + ") within valueDiff (" + valueDiff + ")");
                        return true; // Found a valid pair
                    } else {
                        System.out.println("  Movie difference (" + movieDifference + ") exceeds valueDiff (" + valueDiff + ")");
                    }
                } else {
                    System.out.println("  Seat difference (" + seatDifference + ") exceeds indexDiff (" + indexDiff + ")");
                }
            }
        }

        return false; // No valid pair found
    }

    /**
     * Main method to demonstrate the usage of canSitTogether method.
     * 
     * @param args Command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        // Example usage:
        int[] nums = {2, 3, 5, 4, 9};
        int indexDiff = 2;
        int valueDiff = 1;

        // Check if there exists a pair of friends who can sit together
        boolean result = canSitTogether(nums, indexDiff, valueDiff);

        // Print the result
        System.out.println("Output: " + result); // Output: true
    }
}
