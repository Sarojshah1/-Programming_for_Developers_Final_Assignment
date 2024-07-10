import java.util.*;

public class TSPHillClimbing {

    // Class representing a city with x and y coordinates
    static class City {
        int x, y;

        public City(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Method to calculate the Euclidean distance to another city
        public double distanceTo(City other) {
            int dx = x - other.x;
            int dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // Class representing a TSP solution with a tour (list of cities) and its cost
    static class TSPSolution {
        List<City> tour;
        double cost;

        public TSPSolution(List<City> tour) {
            // Make a copy of the tour to ensure it is not modified outside this class
            this.tour = new ArrayList<>(tour);
            // Calculate the total cost of the tour
            this.cost = calculateCost();
        }

        // Method to calculate the total distance (cost) of the tour
        private double calculateCost() {
            double cost = 0.0;
            // Sum up the distances between consecutive cities in the tour
            for (int i = 0; i < tour.size() - 1; i++) {
                cost += tour.get(i).distanceTo(tour.get(i + 1));
            }
            // Add the distance to return to the starting city
            cost += tour.get(tour.size() - 1).distanceTo(tour.get(0));
            return cost;
        }

        @Override
        public String toString() {
            return "Tour: " + tour + ", Cost: " + cost;
        }
    }

    // Class implementing the hill climbing algorithm for solving TSP
    static class TSPhillClimbing {

        // Method to solve the TSP problem using hill climbing
        public TSPSolution solve(List<City> cities) {
            // Start with a random initial solution
            List<City> currentSolution = new ArrayList<>(cities);
            Collections.shuffle(currentSolution);
            // Create a TSPSolution object from the initial solution
            TSPSolution best = new TSPSolution(currentSolution);

            boolean improved;
            // Continue until no further improvement can be made
            do {
                improved = false;
                // Iterate through all pairs of cities in the tour
                for (int i = 0; i < cities.size() - 1; i++) {
                    for (int j = i + 1; j < cities.size(); j++) {
                        // Generate a new solution by swapping two cities
                        List<City> newSolution = swap(currentSolution, i, j);
                        // Create a TSPSolution object for the new solution
                        TSPSolution neighbor = new TSPSolution(newSolution);
                        // If the new solution is better, update the best solution
                        if (neighbor.cost < best.cost) {
                            best = neighbor;
                            currentSolution = newSolution;
                            improved = true;
                        }
                    }
                }
            } while (improved); // Repeat until no improvement

            return best;
        }

        // Method to create a new solution by swapping two cities in the tour
        private List<City> swap(List<City> solution, int i, int j) {
            List<City> newSolution = new ArrayList<>(solution);
            Collections.swap(newSolution, i, j);
            return newSolution;
        }
    }

    // Main method to test the TSP hill climbing algorithm
    public static void main(String[] args) {
        // Example cities
        List<City> cities = new ArrayList<>();
        cities.add(new City(0, 0));
        cities.add(new City(1, 3));
        cities.add(new City(2, 5));
        cities.add(new City(5, 2));
        cities.add(new City(6, 6));
        cities.add(new City(7, 1));

        // Create an instance of the TSPhillClimbing class
        TSPhillClimbing solver = new TSPhillClimbing();
        // Solve the TSP problem with the given cities
        TSPSolution solution = solver.solve(cities);

        // Print the best solution found
        System.out.println("Best Solution found:");
        System.out.println(solution);
    }
}
