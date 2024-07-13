public class LongestHikeWithinElevationGainLimit {

    public static int longestHike(int[] nums, int k) {
        int n = nums.length; // Number of elements in the trail array
        int left = 0; // Start index of the current valid hiking window
        int maxLen = 0; // Maximum length of the valid hike found so far
        int currentGain = 0; // Current cumulative elevation gain within the window

        // Iterate through the trail array using the right pointer
        for (int right = 0; right < n - 1; right++) {
            // Calculate gain only if it's an uphill (positive gain)
            int gain = Math.max(0, nums[right + 1] - nums[right]);
            currentGain += gain; // Add the gain to the current cumulative gain

            // Debugging: Print the current state of the right pointer and current gain
            System.out.println("Right pointer at: " + right);
            System.out.println("Current gain: " + currentGain);

            // Adjust the left pointer if the current gain exceeds the allowed limit (k)
            while (currentGain > k && left < right) {
                // Calculate the gain for the left segment only if it's an uphill
                int leftGain = Math.max(0, nums[left + 1] - nums[left]);
                currentGain -= leftGain; // Subtract the left segment gain from the current gain
                left++; // Move the left pointer to the right

                // Debugging: Print the new state of the left pointer and current gain
                System.out.println("Adjusting left pointer to: " + left);
                System.out.println("Current gain after adjustment: " + currentGain);
            }

            // Check if the current window is a valid hike
            if (currentGain <= k) {
                int currentLength = right - left + 1; // Calculate the length of the current window
                maxLen = Math.max(maxLen, currentLength); // Update the maximum length if current is longer

                // Debugging: Print the valid hike range and the current maximum length
                System.out.println("Found valid hike from " + left + " to " + right);
                System.out.println("Current max length: " + maxLen);
            }

            // Debugging: Print separator for each iteration
            System.out.println("---");
        }

        // Adding 1 to maxLen because it represents the length in terms of the count of elements
        return maxLen + 1;
    }

    public static void main(String[] args) {
        // Example trail with elevations
        int[] trail1 = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k1 = 3; // Maximum allowed elevation gain
        // Print the result of the longest hike that meets the elevation gain limit
        System.out.println("Longest hike: " + longestHike(trail1, k1)); // Expected output: 5
    }
}
