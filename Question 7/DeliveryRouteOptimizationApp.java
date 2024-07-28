/**
 * The DeliveryRouteOptimizationApp is a Java Swing-based GUI application designed to optimize delivery routes using graph algorithms. It allows users to input city data, select an optimization algorithm, and visualize the optimized route on a map. The main class DeliveryRouteOptimizationApp consists of various components and functionalities to achieve this purpose. Here's a detailed explanation of each part:

Class Overview
Purpose: This class provides a user-friendly interface for optimizing delivery routes by applying graph algorithms (e.g., Dijkstra's Algorithm).
Main Components:
JTextArea: For inputting delivery city data.
JComboBox: For selecting the route optimization algorithm.
JTextFields: For inputting vehicle capacity, source city, and destination city.
JButton: For triggering the route optimization process.
RouteMapPanel: For visualizing cities and routes on a graphical map.
City Class: Represents individual cities and their connections.
Components and GUI Setup
Constructor
DeliveryRouteOptimizationApp(): Sets up the main frame, initializes components, and makes the frame visible.
initComponents()
Sets up the main panel and layout.
Creates and configures various GUI components (labels, text areas, combo box, text fields, button).
Adds components to the main panel using GridBagLayout for flexible layout management.
Initializes the RouteMapPanel and adds it to the main panel for visualizing the route.
Event Handling
OptimizeButtonListener
An inner class implementing ActionListener to handle the click event of the "Optimize Route" button.
actionPerformed(): Reads input data, validates it, updates city connections, and triggers the optimization process asynchronously using an ExecutorService.
Core Functionalities
Data Parsing and Validation
Reads and parses delivery city data from the JTextArea.
Validates input format and creates City objects.
Adds connections between cities based on the input data.
Route Optimization
Uses Dijkstra's Algorithm to find the shortest path between the source and destination cities.
findShortestPathDijkstra(): Implements Dijkstra's Algorithm to calculate the shortest path.
getClosestCity(): Helper method to get the closest city from the set of unvisited cities.
reconstructPath(): Reconstructs the shortest path from the source to the destination using the previous city map.
City Class
Represents a city with a name, coordinates (x, y), and a map of connections to other cities.
addConnection(): Adds a connection to another city with a specified distance.
RouteMapPanel Class
Custom panel for visualizing the city map and the optimized route.
updateRoute(): Updates the route to be displayed and triggers a repaint.
paintComponent(): Draws the cities, connections, and the optimized route on the panel.
adjustCityPositions(): Adjusts city positions to avoid overlaps in the visualization.
Main Method
main(): Entry point of the application, initializes the GUI by creating an instance of DeliveryRouteOptimizationApp.
Detailed Breakdown of Key Methods
actionPerformed()
Input Parsing:

Reads lines from deliveryListTextArea.
Splits each line into parts to extract city details and connections.
Creates or retrieves City objects and adds connections.
Updates cityMap with parsed cities and connections.
Algorithm Selection and Validation:

Retrieves the selected algorithm from algorithmComboBox.
Gets source and destination city names from text fields.
Validates the existence of source and destination cities in cityMap.
Vehicle Capacity Validation:

Parses and validates the vehicle capacity from the text field.
Route Optimization:

Executes the route optimization process in a separate thread.
Calls findShortestPathDijkstra() to find the optimized route.
Updates the UI with the optimized route using SwingUtilities.invokeLater().
findShortestPathDijkstra()
Initializes distance and previous city maps.
Uses a set of unvisited cities and iteratively finds the shortest path.
Updates distances to neighboring cities and reconstructs the shortest path.
 * DeliveryRouteOptimizationApp is a Swing-based GUI application for optimizing delivery routes using graph algorithms.
 * It allows users to input city data, select an optimization algorithm, and visualize the optimized route on a map.
 * 
 * The application performs the following tasks:
 * - Parses city data from a text area.
 * - Provides options to select a route optimization algorithm (e.g., Dijkstra's Algorithm).
 * - Displays the optimized route on a graphical map.
 * - Handles input validation and updates the GUI asynchronously.
 * 
 * The main components of the class include:
 * - JTextArea for inputting delivery city data.
 * - JComboBox for selecting the algorithm.
 * - JTextFields for vehicle capacity, source city, and destination city.
 * - JButton for triggering the route optimization process.
 * - RouteMapPanel for visualizing cities and routes.
 * - City class representing individual cities and their connections.
 * 
 * Time Complexity:
 * - Parsing input and creating city connections: O(n*m), where n is the number of cities and m is the number of connections per city.
 * - Dijkstra's Algorithm for shortest path calculation: O(V^2) with a simple implementation where V is the number of vertices (cities).
 * - The complexity of rendering and adjusting city positions is mainly influenced by the number of cities and connections but is generally O(n^2) for overlap adjustment in the worst case.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DeliveryRouteOptimizationApp extends JFrame {

    private JTextArea deliveryListTextArea;
    private JComboBox<String> algorithmComboBox;
    private JTextField vehicleCapacityField;
    private JTextField sourceCityField;
    private JTextField destinationCityField;
    private JButton optimizeButton;
    private RouteMapPanel routeMapPanel;

    private ExecutorService executorService;

    private Map<String, City> cityMap = new HashMap<>();

    public DeliveryRouteOptimizationApp() {
        setTitle("Delivery Route Optimization");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Delivery List
        JLabel deliveryListLabel = new JLabel("Delivery List:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(deliveryListLabel, gbc);

        deliveryListTextArea = new JTextArea(10, 40);
        JScrollPane deliveryScrollPane = new JScrollPane(deliveryListTextArea);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(deliveryScrollPane, gbc);

        // Algorithm
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JLabel("Algorithm:"), gbc);

        algorithmComboBox = new JComboBox<>(new String[]{"Dijkstra's Algorithm"});
        gbc.gridx = 1;
        inputPanel.add(algorithmComboBox, gbc);

        // Vehicle Capacity
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Vehicle Capacity:"), gbc);

        vehicleCapacityField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(vehicleCapacityField, gbc);

        // Source City
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Source City:"), gbc);

        sourceCityField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(sourceCityField, gbc);

        // Destination City
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Destination City:"), gbc);

        destinationCityField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(destinationCityField, gbc);

        // Optimize Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        optimizeButton = new JButton("Optimize Route");
        optimizeButton.addActionListener(new OptimizeButtonListener());
        inputPanel.add(optimizeButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Route Map Panel
        JPanel routePanel = new JPanel(new BorderLayout());
        routeMapPanel = new RouteMapPanel(cityMap);
    routeMapPanel.setPreferredSize(new Dimension(800, 400));
    routePanel.add(routeMapPanel, BorderLayout.CENTER);
        mainPanel.add(routeMapPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private class OptimizeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Read delivery list from text area
            String[] lines = deliveryListTextArea.getText().split("\n");
            cityMap.clear();

            // Use a temporary map to avoid creating duplicate cities
            Map<String, City> tempCityMap = new HashMap<>();

            for (String line : lines) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 4) {
                    try {
                        String name = parts[0].trim();
                        int x = Integer.parseInt(parts[1].trim());
                        int y = Integer.parseInt(parts[2].trim());
                        int distanceToNext = Integer.parseInt(parts[3].trim());

                        // Create or retrieve the city
                        City city = tempCityMap.computeIfAbsent(name, k -> new City(name, x, y));

                        // Add connection to the city
                        for (int i = 4; i < parts.length; i++) {
                            String[] connectionParts = parts[i].trim().split(";");
                            if (connectionParts.length == 2) {
                                String neighborName = connectionParts[0].trim();
                                int distance = Integer.parseInt(connectionParts[1].trim());

                                City neighbor = tempCityMap.computeIfAbsent(neighborName, k -> new City(neighborName, 0, 0));
                                city.addConnection(neighbor, distance);
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(DeliveryRouteOptimizationApp.this,
                                "Invalid input in delivery list.",
                                "Invalid Input",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(DeliveryRouteOptimizationApp.this,
                            "Delivery list format is incorrect. Use 'name, x, y, distanceToNext, neighborName;distance'.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            // Update cityMap with connections from tempCityMap
            cityMap.putAll(tempCityMap);


            // Get selected algorithm
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

            // Get source and destination
            String sourceName = sourceCityField.getText().trim();
            String destinationName = destinationCityField.getText().trim();

            City source = cityMap.get(sourceName);
            City destination = cityMap.get(destinationName);

            if (source == null || destination == null) {
                JOptionPane.showMessageDialog(DeliveryRouteOptimizationApp.this,
                        "Source or destination city not found.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get vehicle capacity
            int vehicleCapacity;
            try {
                vehicleCapacity = Integer.parseInt(vehicleCapacityField.getText());
                if (vehicleCapacity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DeliveryRouteOptimizationApp.this,
                        "Please enter a valid positive number for vehicle capacity.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Perform route optimization using selected algorithm
            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    List<City> optimizedRoute;
                        optimizedRoute = findShortestPathDijkstra(source, destination);
                    

                    // Update UI with optimized route
                    SwingUtilities.invokeLater(() -> routeMapPanel.updateRoute(optimizedRoute));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        private List<City> findShortestPathDijkstra(City source, City destination) {
            Map<City, City> previous = new HashMap<>();
            Map<City, Integer> distances = new HashMap<>();
            Set<City> unvisited = new HashSet<>(cityMap.values());

            for (City city : cityMap.values()) {
                distances.put(city, Integer.MAX_VALUE);
            }
            distances.put(source, 0);

            while (!unvisited.isEmpty()) {
                City current = getClosestCity(unvisited, distances);
                unvisited.remove(current);

                if (current.equals(destination)) {
                    break;
                }

                // Update distances to neighboring cities
                for (Map.Entry<City, Integer> entry : current.getConnections().entrySet()) {
                    City neighbor = entry.getKey();
                    int distance = entry.getValue();
                    if (unvisited.contains(neighbor)) {
                        int newDist = distances.get(current) + distance;
                        if (newDist < distances.get(neighbor)) {
                            distances.put(neighbor, newDist);
                            previous.put(neighbor, current);
                        }
                    }
                }
            }

            return reconstructPath(previous, source, destination);
        }

        private City getClosestCity(Set<City> unvisited, Map<City, Integer> distances) {
            return unvisited.stream()
                    .min(Comparator.comparingInt(distances::get))
                    .orElse(null);
        }

        private List<City> reconstructPath(Map<City, City> previous, City source, City destination) {
            List<City> path = new ArrayList<>();
            for (City at = destination; at != null; at = previous.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);
            return  path.size() > 1 && path.get(0).equals(source) ? path : Collections.emptyList();
        }

        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeliveryRouteOptimizationApp::new);
    }

    // City class
    static class City {
        private final String name;
        private final int x;
        private final int y;
        private final Map<City, Integer> connections;

        public City(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.connections = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Map<City, Integer> getConnections() {
            return connections;
        }

        public void addConnection(City city, int distance) {
            connections.put(city, distance);
        }
    }

    // RouteMapPanel class (dummy implementation for placeholder)
   
    // RouteMapPanel class
    public class RouteMapPanel extends JPanel {
        private final Map<String, City> cityMap;
        private List<City> optimizedRoute;
    
        public RouteMapPanel(Map<String, City> cityMap) {
            this.cityMap = cityMap;
            this.optimizedRoute = new ArrayList<>();
        }
    
        public void updateRoute(List<City> route) {
            this.optimizedRoute = route;
            repaint();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
    
            if (optimizedRoute.isEmpty()) {
                return; // No route to draw
            }
    
            // Calculate the bounding box of the optimized route
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
    
            for (City city : cityMap.values()) {
                int x = city.getX();
                int y = city.getY();
                if (x < minX) minX = x;
                if (y < minY) minY = y;
                if (x > maxX) maxX = x;
                if (y > maxY) maxY = y;
            }
    
            // Add padding
            int padding = 50;
            int width = maxX - minX + 2 * padding;
            int height = maxY - minY + 2 * padding;
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int offsetX = (panelWidth - width) / 2 - minX + padding;
            int offsetY = (panelHeight - height) / 2 - minY + padding;
    
            // Draw cities and connections
            g2d.setColor(Color.BLACK);
            int cityRadius = 5;
            Map<City, Point> cityPositions = new HashMap<>();
    
            // First, calculate the initial positions
            for (City city : cityMap.values()) {
                int x = city.getX() + offsetX;
                int y = city.getY() + offsetY;
                cityPositions.put(city, new Point(x, y));
            }
    
            // Adjust positions to avoid overlaps
            adjustCityPositions(cityPositions, cityRadius);

             // Draw connections (edges)
        g2d.setColor(Color.GRAY);
        for (City city : cityMap.values()) {
            Point cityPoint = cityPositions.get(city);
            for (Map.Entry<City, Integer> entry : city.getConnections().entrySet()) {
                City neighbor = entry.getKey();
                Point neighborPoint = cityPositions.get(neighbor);
                g2d.drawLine(cityPoint.x, cityPoint.y, neighborPoint.x, neighborPoint.y);
            }
        }
    
            // Draw cities
            for (Map.Entry<City, Point> entry : cityPositions.entrySet()) {
                City city = entry.getKey();
                Point position = entry.getValue();
                int x = position.x;
                int y = position.y;
                g2d.setColor(Color.BLACK); // City color
                g2d.fillOval(x - cityRadius, y - cityRadius, 2 * cityRadius, 2 * cityRadius);
                g2d.drawString(city.getName(), x + 10, y);
            }
    
            // Draw connections
            g2d.setColor(Color.BLUE);
            for (City city : cityMap.values()) {
                Point cityPoint = cityPositions.get(city);
                for (Map.Entry<City, Integer> entry : city.getConnections().entrySet()) {
                    City neighbor = entry.getKey();
                    Point neighborPoint = cityPositions.get(neighbor);
                    g2d.drawLine(cityPoint.x, cityPoint.y, neighborPoint.x, neighborPoint.y);
                }
            }
    
            // Draw optimized route
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2)); // Increase line thickness
            for (int i = 0; i < optimizedRoute.size() - 1; i++) {
                City from = optimizedRoute.get(i);
                City to = optimizedRoute.get(i + 1);
                Point fromPoint = cityPositions.get(from);
                Point toPoint = cityPositions.get(to);
                g2d.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
            }
    
            // Draw circles on route cities
            g2d.setColor(Color.RED);
            for (City city : optimizedRoute) {
                Point position = cityPositions.get(city);
                int x = position.x;
                int y = position.y;
                g2d.fillOval(x - cityRadius, y - cityRadius, 2 * cityRadius, 2 * cityRadius);
                g2d.drawString(city.getName(), x + 10, y);
            }
        }
    
        private void adjustCityPositions(Map<City, Point> cityPositions, int radius) {
            // Simple overlap avoidance algorithm
            final int spacing = 20; // Minimum spacing between cities
            boolean overlapping;
            do {
                overlapping = false;
                for (Map.Entry<City, Point> entry1 : cityPositions.entrySet()) {
                    City city1 = entry1.getKey();
                    Point point1 = entry1.getValue();
                    for (Map.Entry<City, Point> entry2 : cityPositions.entrySet()) {
                        City city2 = entry2.getKey();
                        Point point2 = entry2.getValue();
                        if (city1 != city2 && point1.distance(point2) < 2 * radius + spacing) {
                            overlapping = true;
                            // Move city2 to a new position
                            double angle = Math.random() * 2 * Math.PI;
                            int distance = 2 * radius + spacing - (int) point1.distance(point2);
                            int newX = (int) (point2.x + distance * Math.cos(angle));
                            int newY = (int) (point2.y + distance * Math.sin(angle));
                            point2.setLocation(newX, newY);
                        }
                    }
                }
            } while (overlapping);
        }
    }

}
