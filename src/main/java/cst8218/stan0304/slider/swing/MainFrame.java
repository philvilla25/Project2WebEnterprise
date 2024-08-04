/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.slider.swing;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author rukev
 * @author shale MainFrame class represents the main GUI frame for the Slider
 * Application. It allows users to view and update slider properties through a
 * Swing interface.
 */
public class MainFrame extends JFrame {
    // Text fields for user input to interact with slider properties
    private JTextField sliderID;
    private JTextField newCurrentTravel;
    private JTextField newMaxTravel;
    private JTextField newMovementDirection;
    private JTextField newSize;
    private JTextField newX;
    private JTextField newY;

    /**
     * Constructor to initialize and set up the JFrame.
     */
    public MainFrame() {
        setTitle("Slider Application"); // Set the title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define the operation when the window is closed
        setSize(700, 500); // Set the size of the window

        // Label prompting user to enter slider ID
        JLabel label = new JLabel("Enter the ID of the Slider you want to change:");
        
        // Label for instructions
        JLabel labelNew = new JLabel("Click 'Show Slider' to show the Slider after change");
        
         // Label for slider update status
        JLabel sliderUpdate = new JLabel();

        // Initialize text fields with preferred sizes
        sliderID = new JTextField();
        sliderID.setPreferredSize(new Dimension(200, 30));

        // TextArea to display slider properties; not editable by the user
        JTextArea sliderProperties = new JTextArea();
        sliderProperties.setEditable(false);

        // Initialize text fields for new property values
        newCurrentTravel = new JTextField();
        newMaxTravel = new JTextField();
        newMovementDirection = new JTextField();
        newSize = new JTextField();
        newX = new JTextField();
        newY = new JTextField();

        // Button to fetch and show current slider properties
        JButton showSliderButton = new JButton("Show Slider");
        showSliderButton.addActionListener(e -> {
            String id = sliderID.getText(); // Get the slider ID from text field
            String properties = getSliderProperties(id); // Fetch slider properties
            sliderProperties.setText(properties); // Display properties in text area
        });

        // Button to update slider properties
        JButton changeSliderButton = new JButton("Change Slider");
        changeSliderButton.addActionListener(e -> {
            String id = sliderID.getText(); // Get the slider ID from text field
            String updatedProperties = updateSlider(id); // Update slider properties on server
            sliderUpdate.setText(updatedProperties); // Display update status in label
        });

        // Create and set up the panel with layout and components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to the panel
        panel.add(label);
        panel.add(sliderID);
        panel.add(showSliderButton);
        panel.add(sliderProperties);

        // Add fields for new property values
        panel.add(new JLabel("New Current Travel:"));
        panel.add(newCurrentTravel);
        panel.add(new JLabel("New Max Travel:"));
        panel.add(newMaxTravel);
        panel.add(new JLabel("New Movement Direction:"));
        panel.add(newMovementDirection);
        panel.add(new JLabel("New Size:"));
        panel.add(newSize);
        panel.add(new JLabel("New X:"));
        panel.add(newX);
        panel.add(new JLabel("New Y:"));
        panel.add(newY);

        // Add the change button
        panel.add(changeSliderButton);
        panel.add(labelNew);
        panel.add(sliderUpdate);
        add(panel); // Add panel to the frame
    }

    /**
     * Fetches the current properties of the slider from the server.
     *
     * @param id The ID of the slider to retrieve properties for.
     * @return The slider properties as a JSON string.
     */
    private String getSliderProperties(String id) {
        String urlString = "http://localhost:8080/slider-thomas/resources/cst8218.stan0304.slider.entity.slider/" + id;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // Set HTTP method to GET

            // Read the response from the server
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }

            return result.toString(); // Return the result as a string

        } catch (IOException e) {
            return "Error fetching slider properties"; // Return error message in case of exception
        }
    }

    /**
     * Updates the slider properties on the server.
     *
     * @param id The ID of the slider to update.
     * @return A message indicating success or failure.
     */
    /**
     * Updates the slider properties on the server.
     *
     * @param id The ID of the slider to update.
     * @return A message indicating success or failure.
     */
    private String updateSlider(String id) {
        String urlString = "http://localhost:8080/slider-thomas/resources/cst8218.stan0304.slider.entity.slider/" + id;
        HttpURLConnection conn = null;
        try {
            String currentProperties = getSliderProperties(id);

            if (currentProperties == null || currentProperties.isEmpty()) {
                return "No properties retrieved to update.";
            }

            String updatedJson = updateJsonProperties(currentProperties);

            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = updatedJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            StringBuilder responseMessage = new StringBuilder();
            try (BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = responseReader.readLine()) != null) {
                    responseMessage.append(line);
                }
            }

            if (code == HttpURLConnection.HTTP_OK) {
                return "Slider updated successfully! Response: " + responseMessage.toString();
            } else {
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                }
                return "Failed to update slider. HTTP Code: " + code + " Response: " + errorResponse.toString();
            }

        } catch (NullPointerException e) {
            return " ";
        } catch (IOException e) {
            return "Error updating slider: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Updates the JSON properties with new values from the text fields.
     *
     * @param currentProperties The current properties in JSON format.
     * @return The updated JSON with new values.
     */
    private String updateJsonProperties(String currentProperties) {
        // If the current properties string is empty, return an empty JSON object
        if (currentProperties == null || currentProperties.trim().isEmpty()) {
            return "{}";
        }

        // Update fields only if new values are provided
        String updatedJson = currentProperties;

        if (!newCurrentTravel.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "currentTravel", newCurrentTravel.getText());
        }
        if (!newMaxTravel.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "maxTravel", newMaxTravel.getText());
        }
        if (!newMovementDirection.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "movementDirection", newMovementDirection.getText());
        }
        if (!newSize.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "size", newSize.getText());
        }
        if (!newX.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "x", newX.getText());
        }
        if (!newY.getText().isEmpty()) {
            updatedJson = replaceJsonField(updatedJson, "y", newY.getText());
        }

        return updatedJson;
    }

    /**
     * Replaces a field's value in a JSON string with a new value.
     *
     * @param json The JSON string to modify.
     * @param field The field name to replace.
     * @param newValue The new value for the field.
     * @return The modified JSON string.
     */
    private String replaceJsonField(String json, String field, String newValue) {
        // Regular expression to match the field and its current value
        String regex = "\"" + field + "\":\\d+";
        // Replacement string with new field value
        String replacement = "\"" + field + "\":" + newValue;
        // Replace the first occurrence of the field with the new value
        return json.replaceFirst(regex, replacement);
    }

    /**
     * Main method to run the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create and display the main frame
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}