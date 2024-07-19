/**
 * FileConversionApp is a Java Swing application that allows users to select files for conversion,
 * choose a conversion type, and monitor the conversion progress. The application supports
 * multiple file conversions simultaneously using an ExecutorService for concurrency.
 *
 * Features:
 * - Select multiple files for conversion using a JFileChooser
 * - Choose conversion type from a dropdown (e.g., PDF to Docx, Image Resize)
 * - Display conversion progress using a JProgressBar
 * - Display conversion status and messages in a JTextArea
 * - Cancel ongoing conversions
 * - Open the converted files using the default system application
 *
 * Key Components:
 * - JFileChooser: For selecting files to be converted
 * - JProgressBar: For displaying conversion progress
 * - JTextArea: For displaying status messages
 * - JButton: For triggering file selection, starting conversion, and cancelling conversion
 * - JComboBox: For selecting the conversion type
 * - ExecutorService: For managing concurrent file conversion tasks
 * - SwingWorker: For executing conversion tasks in the background and updating the GUI asynchronously
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class FileConversionApp extends JFrame {

    private JFileChooser fileChooser;
    private JProgressBar progressBar;
    private JTextArea statusArea;
    private JButton selectFilesButton;
    private JButton startButton;
    private JButton cancelButton;
    private JComboBox<String> conversionOptions;
    private List<File> selectedFiles;
    private ExecutorService executorService;
    private SwingWorker<Void, String> conversionWorker;

    public FileConversionApp() {
        setTitle("File Conversion Application");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);

        selectFilesButton = new JButton("Select Files");
        selectFilesButton.addActionListener(new SelectFilesButtonListener());

        startButton = new JButton("Start Conversion");
        startButton.addActionListener(new StartButtonListener());
        startButton.setEnabled(false);

        cancelButton = new JButton("Cancel Conversion");
        cancelButton.addActionListener(new CancelButtonListener());

        conversionOptions = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        statusArea = new JTextArea(10, 50);
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);

        topPanel.add(new JLabel("Select Conversion Type:"));
        topPanel.add(conversionOptions);
        topPanel.add(selectFilesButton);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        bottomPanel.add(progressBar);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private class SelectFilesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fileChooser.showOpenDialog(FileConversionApp.this) == JFileChooser.APPROVE_OPTION) {
                selectedFiles = List.of(fileChooser.getSelectedFiles());
                startButton.setEnabled(true);
                statusArea.append("Selected " + selectedFiles.size() + " files for conversion.\n");
            }
        }
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startConversion();
        }
    }

    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (conversionWorker != null) {
                conversionWorker.cancel(true);
                if (executorService != null && !executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
            }
        }
    }

    private void startConversion() {
        conversionWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                int fileCount = selectedFiles.size();
                int progressStep = 100 / fileCount;
                int progress = 0;

                executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                for (File file : selectedFiles) {
                    if (isCancelled()) {
                        publish("Conversion cancelled.");
                        break;
                    }

                    String conversionType = (String) conversionOptions.getSelectedItem();
                    publish("Converting " + file.getName() + " to " + conversionType);

                    Future<?> future = executorService.submit(() -> {
                        try {
                            File convertedFile = convertFile(file, conversionType);
                            publish("Converted " + file.getName() + " to " + conversionType);
                            openFile(convertedFile);
                        } catch (Exception ex) {
                            publish("Error converting " + file.getName() + ": " + ex.getMessage());
                        }
                    });

                    try {
                        future.get(); // Wait for the task to complete
                        progress += progressStep;
                        setProgress(progress);
                    } catch (InterruptedException | ExecutionException ex) {
                        publish("Error during conversion: " + ex.getMessage());
                    }
                }

                executorService.shutdown();
                try {
                    executorService.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException ex) {
                    publish("Conversion interrupted: " + ex.getMessage());
                }

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String status : chunks) {
                    statusArea.append(status + "\n");
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(FileConversionApp.this, "Conversion completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (CancellationException e) {
                    JOptionPane.showMessageDialog(FileConversionApp.this, "Conversion was cancelled.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        conversionWorker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        conversionWorker.execute();
    }

    private File convertFile(File file, String conversionType) throws Exception {
        // Simulate file conversion with sleep
        Thread.sleep(1000); // Simulate time taken for conversion
        // Actual conversion logic should be implemented here

        // For the sake of this example, let's assume the converted file has the same name with a different extension
        String convertedFileName = file.getName().replace(".pdf", ".docx");
        File convertedFile = new File(file.getParent(), convertedFileName);

        // You would replace this part with actual file conversion logic
        if (!convertedFile.exists()) {
            convertedFile.createNewFile(); // Just to simulate conversion
        }

        return convertedFile;
    }

    private void openFile(File file) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    statusArea.append("Error opening file " + file.getName() + ": " + e.getMessage() + "\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileConversionApp app = new FileConversionApp();
            app.setVisible(true);
        });
    }
}
