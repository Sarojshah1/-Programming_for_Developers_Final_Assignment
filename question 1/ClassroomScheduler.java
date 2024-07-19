/**
 * The ClassroomScheduler class is designed to manage the scheduling of classes in multiple classrooms,
 * ensuring that as many classes as possible are assigned to the available rooms while minimizing
 * delays and room conflicts. It provides a method to determine the room that holds the maximum number
 * of classes, taking into account potential scheduling conflicts and necessary delays.
 * 
 * The key method in this class is `maxClassrooms`, which performs the following steps:
 * 1. Sorts the classes based on their start times and durations.
 * 2. Attempts to assign each class to an available room without conflict.
 * 3. If a class cannot be assigned immediately, it delays the class to the earliest possible time.
 * 4. Tracks the number of classes held in each room.
 * 5. Determines and returns the index of the room that holds the maximum number of classes.
 */
import java.util.*;
public class ClassroomScheduler {

    public static int maxClassrooms(int n, int[][] classes) {
        // Sort classes by start time, and if start times are the same, by class size (descending)
        Arrays.sort(classes, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]); // Sort by start time
            } else {
                return Integer.compare(b[1] - b[0], a[1] - a[0]); // Larger duration first
            }
        });

        // List to track classrooms and their ongoing classes
        List<List<int[]>> classrooms = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            classrooms.add(new ArrayList<>()); // Initialize each room with an empty list of classes
        }

        // Result tracking: count of classes held in each room
        int[] classCount = new int[n];

        // Process each class in the sorted order
        for (int[] cls : classes) {
            int start = cls[0];
            int end = cls[1];
            boolean assigned = false; // Flag to track if class is assigned to a room

            // Attempt to assign the class to an available room
            for (int roomIndex = 0; roomIndex < n; roomIndex++) {
                List<int[]> roomSchedule = classrooms.get(roomIndex);

                // Check if the room is empty or the last class in the room ends before the current class starts
                if (roomSchedule.isEmpty() || roomSchedule.get(roomSchedule.size() - 1)[1] <= start) {
                    // Assign class to this room
                    roomSchedule.add(new int[]{start, end});
                    classCount[roomIndex]++;
                    assigned = true;
                    System.out.println("Assigning class [" + start + ", " + end + "] to room " + roomIndex);
                    break; // Break out of room assignment loop once class is assigned
                }
            }

            // If not assigned, delay the class until the earliest available time in any room
            if (!assigned) {
                int minEndTime = Integer.MAX_VALUE;
                int roomIndexToDelay = -1;

                // Find the room with the earliest available time after the class starts
                for (int roomIndex = 0; roomIndex < n; roomIndex++) {
                    List<int[]> roomSchedule = classrooms.get(roomIndex);
                    int lastEndTime = roomSchedule.get(roomSchedule.size() - 1)[1];

                    if (lastEndTime < minEndTime) {
                        minEndTime = lastEndTime;
                        roomIndexToDelay = roomIndex;
                    }
                }

                // Calculate the start time for delayed assignment
                int delayedStart = classrooms.get(roomIndexToDelay).get(classrooms.get(roomIndexToDelay).size() - 1)[1];

                // Assign class to this room with delayed start
                classrooms.get(roomIndexToDelay).add(new int[]{delayedStart, delayedStart + (end - start)});
                classCount[roomIndexToDelay]++;
                System.out.println("Delaying class [" + start + ", " + end + "] to room " + roomIndexToDelay);
            }

            // Print current room usage after each class assignment
            System.out.println("Current room usage:");
            for (int i = 0; i < n; i++) {
                System.out.println("Room " + i + " has " + classrooms.get(i).size() + " classes.");
            }
            System.out.println("---");
        }

        // Print final room usage and counts
        System.out.println("Final room usage:");
        for (int i = 0; i < n; i++) {
            System.out.println("Room " + i + " held " + classCount[i] + " classes.");
        }

        // Find room with max class count (and if tied, return the room with minimum index)
        int maxCount = 0;
        int maxCountRoom = 0;
        for (int i = 0; i < n; i++) {
            if (classCount[i] > maxCount || (classCount[i] == maxCount && i < maxCountRoom)) {
                maxCount = classCount[i];
                maxCountRoom = i;
            }
        }

        // Print the room with the maximum class count
        System.out.println("Room " + maxCountRoom + " held the maximum number of classes: " + maxCount);

        return maxCountRoom; // Return the room index with the maximum class count
    }

    public static void main(String[] args) {
        int[][] classes1 = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        int[][] classes2 = {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};
        int[][] classes3 = {{1, 5}, {2, 3}, {3, 8}, {4, 9}, {6, 8},{7,12}};

        System.out.println(maxClassrooms(2, classes1)); // Output: 0
        System.out.println(maxClassrooms(3, classes2)); // Output: 1
        System.out.println(maxClassrooms(3, classes3)); // Output: 1
    }
}
