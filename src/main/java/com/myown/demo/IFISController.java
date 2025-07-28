package com.myown.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Main controller for CSV file import and initial data processing
public class IFISController {

    @FXML
    private TextField pathid;

    @FXML
    private Button import_button;

    @FXML
    private Label warning;

    // Handles CSV file import - either from text field path or file chooser dialog
    @FXML
    private void onImportPathButtonClick() {
        String filePath = pathid.getText().trim();

        if (filePath.isEmpty()) {
            // Show file chooser if no path entered
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            Stage stage = (Stage) import_button.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                filePath = selectedFile.getAbsolutePath();
                pathid.setText(filePath);
            } else {
                warning.setText("* No file selected");
                return;
            }
        }

        try {
            List<Records> recordsData = parseCSV(filePath);
            if (recordsData.isEmpty()) {
                warning.setText("* No data found in the CSV file");
                return;
            }

            warning.setText("*"); // Clear warnings
            openTableViewWindow(recordsData);

        } catch (IOException e) {
            warning.setText("* Error reading file: " + e.getMessage());
        } catch (Exception e) {
            warning.setText("* Error processing CSV: " + e.getMessage());
        }
    }

    // Parses CSV file and creates Records objects from each valid row
    public List<Records> parseCSV(String filePath) throws IOException {
        List<Records> recordsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] values = parseCSVLine(line);

                // Ensure minimum required columns (6: incomeCode, description, date, incomeAmount, withHoldingTax, checksum)
                if (values.length >= 5) {
                    try {
                        String incomeCode = values[0].trim();
                        String description = values[1].trim();
                        String date = values[2].trim();
                        double incomeAmount = Double.parseDouble(values[3].trim());
                        double withHoldingTax = Double.parseDouble(values[4].trim());
                        int checksum = Integer.parseInt(values[5].trim());

                        Records record = new Records(incomeCode, description, date, incomeAmount, withHoldingTax, checksum);
                        recordsList.add(record);
                    } catch (NumberFormatException e) {
                        // Skip rows with invalid numeric data
                        System.err.println("Skipping invalid row: " + line);
                    }
                }
            }
        }

        return recordsList;
    }

    // Parses a single CSV line handling quoted values and commas within quotes
    public String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // Toggle quote state
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder(); // Start new value
            } else {
                currentValue.append(c);
            }
        }

        values.add(currentValue.toString()); // Add final value
        return values.toArray(new String[0]);
    }

    // Opens the table view window with imported records data
    private void openTableViewWindow(List<Records> recordsData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IFIS-View.fxml"));
            Scene scene = new Scene(loader.load(), 950, 600);

            // Pass data to the table controller
            TableViewController controller = loader.getController();
            controller.setRecordsData(recordsData);

            // Create modal window for table view
            Stage tableStage = new Stage();
            tableStage.setTitle("Income Records Table");
            tableStage.setScene(scene);
            tableStage.initModality(Modality.APPLICATION_MODAL);
            tableStage.setResizable(true);

            tableStage.show();

        } catch (IOException e) {
            warning.setText("* Error opening table view: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            warning.setText("* Error creating table window: " + e.getMessage());
            e.printStackTrace();
        }
    }
}