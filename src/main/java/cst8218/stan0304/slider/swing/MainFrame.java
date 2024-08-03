/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8218.stan0304.slider.swing;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author rukev
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        // Set the title of the JFrame
        setTitle("Slider Application");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the JFrame
        setSize(500, 500);
        
         // JLabel
        JLabel label = new JLabel("Hello, Enter the id of the Slider you want to change:");
     
        // Slider ID JTextField
        JTextField sliderID = new JTextField();
        sliderID.setPreferredSize(new Dimension(200, 30));
        
         // JTextArea to display slider properties
        JTextArea sliderProperties = new JTextArea();
        sliderProperties.setEditable(false); // Make it non-editable
        
        // Change Slider button
        JButton showSliderButton = new JButton("Show Slider");
        
        // Add an action listener to the button
        showSliderButton.addActionListener(e -> {
            String id = sliderID.getText();
            String properties = getSliderProperties(id);
            sliderProperties.setText(properties);
        });

         // Change Slider button
        JButton button = new JButton("Change Slider");
        // Add an action listener to the button
        button.addActionListener(e -> label.setText("Slider Changed"));

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(label);
        panel.add(sliderID);
        panel.add(showSliderButton);
        panel.add(sliderProperties);
        panel.add(button);
      


        // Add the panel to the JFrame
        add(panel);
    }
    
     // Method to fetch slider properties from a URL


    private String getSliderProperties(String id) {
        System.out.println("In Slider properities");
        String urlString = "http://localhost:8080/Assignment2/resources/cst8218.stan0304.slider.entity.slider/" + id;
        StringBuilder result = new StringBuilder();

        // Fetching the JSON response
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }

            // Manually parsing the JSON response
            String jsonResponse = result.toString();
            String sliderId = extractValue(jsonResponse, "id"); //may delete 
            String currentTravel = extractValue(jsonResponse, "currentTravel");
            String dirChangeCount = extractValue(jsonResponse, "dirChangeCount");
            String maxTravel = extractValue(jsonResponse, "maxTravel");
            String movementDirection = extractValue(jsonResponse, "movementDirection");
            String size = extractValue(jsonResponse, "size");
            String x = extractValue(jsonResponse, "x");
            String y = extractValue(jsonResponse, "y");

            // Example: Use extracted values (for demonstration)
         return String.format("Slider ID: %s\nX: %s\nY: %s\nSize: %s\nMovement Direction: %s\nMax Travel: %s\nDir Change Count: %s\nCurrent Travel: %s",
                     sliderId, x, y, size, movementDirection, maxTravel, dirChangeCount, currentTravel);


        } catch (IOException e) {
            return "Error fetching slider properties";
        }
    }

    private String extractValue(String jsonResponse, String key) {
        String keyValue = "\"" + key + "\":";
        int startIndex = jsonResponse.indexOf(keyValue) + keyValue.length();
        int endIndex = jsonResponse.indexOf(",", startIndex);

        // Handle last value (no comma after it)
        if (endIndex == -1) {
            endIndex = jsonResponse.indexOf("}", startIndex);
        }
        return jsonResponse.substring(startIndex, endIndex).trim().replaceAll("\"", ""); // Remove quotes if needed
    }


    public static void main(String[] args) {
        // Create the JFrame in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
