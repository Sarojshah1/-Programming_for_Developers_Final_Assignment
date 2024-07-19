/**
 * The SecretDecoderRing class is designed to decode messages based on a series of shifts applied to the characters.
 * Each shift specifies a range of characters and a direction (clockwise or counter-clockwise) to rotate the characters.
 * The main functionality is to process these shifts and produce the final decoded message.
 * 
 * The key method in this class is `decipherMessage`, which performs the following steps:
 * 1. Converts the input string into a character array for easier manipulation.
 * 2. Iterates through each shift instruction, applying the specified shifts to the characters within the given range.
 * 3. Adjusts the characters based on the direction of the shift (1 for clockwise, 0 for counter-clockwise).
 * 4. Converts the manipulated character array back to a string and returns the decoded message.
  * Time Complexity:
 * The time complexity of the `decipherMessage` method is O(m * n), where:
 * - `m` is the number of shift instructions.
 * - `n` is the average length of the range specified by each shift instruction.
 * This complexity arises because for each of the `m` shifts, we may need to iterate over a range of characters, which could be up to `n` characters in length.
 */

public class SecretDecoderRing {

    // Method to decipher the message based on the shifts
    public static String decipherMessage(String s, int[][] shifts) {
        // Convert input string into a character array for manipulation
        char[] message = s.toCharArray();

        // Iterate through each shift instruction
        for (int[] shift : shifts) {
            int start_disc = shift[0];   // Start index of discs to be shifted
            int end_disc = shift[1];     // End index of discs to be shifted
            int direction = shift[2];    // Direction of the shift (1 for clockwise, 0 for counter-clockwise)

            // Apply the shift to each character within the specified range
            for (int i = start_disc; i <= end_disc; i++) {
                // Determine the shift amount based on direction
                if (direction == 1) { // Clockwise shift
                    System.out.print(" Rotate discs "+i+" "+message[i]);
                    int newIndex = (message[i] - 'a' + 1) % 26; // Calculate new index with wrap-around
                    if (newIndex < 0) {
                        newIndex += 26; // Handle negative index wrapping
                    }
                    message[i] = (char) ('a' + newIndex); // Update the character with the shifted value
                    System.out.println("  becomes "+message[i]); // Print intermediate message (for debugging)
                } else if (direction == 0) { // Counter-clockwise shift
                    System.out.print(" Rotate discs "+i+" "+message[i]);
                    int newIndex = (message[i] - 'a' - 1) % 26; // Calculate new index with wrap-around
                    if (newIndex < 0) {
                        newIndex += 26; // Handle negative index wrapping
                    }
                    message[i] = (char) ('a' + newIndex); // Update the character with the shifted value
                    System.out.println("  becomes "+message[i]); // Print intermediate message (for debugging)
                }
            }
        }

        // Convert the character array back to a string and return the deciphered message
        return new String(message);
    }

    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}}; // Example shifts

        // Call the decipherMessage method to decode the message with the specified shifts
        String decipheredMessage = decipherMessage(s, shifts);

        // Print the deciphered message to the console
        System.out.println("Deciphered Message: " + decipheredMessage); // Output: jglko
    }
}
