package org.couchbase;

import javax.swing.*;
import java.awt.*;

public class CouchbaseSizer extends JFrame {
    private JSlider ramUsageSlider;
    private JTextField currentlyUsedRamPerNodeField;
    private JTextField ramAllocatedPerNodeField;
    private JTextField totalNodesField;
    private JLabel ramUsageLabel;
    private JLabel healthLabel;

    public CouchbaseSizer() {
        setTitle("Couchbase Cluster Sizer");
        setSize(600, 300); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10)); // Added horizontal and vertical gaps

        add(new JLabel("Currently Used RAM per Node (GB):"));
        currentlyUsedRamPerNodeField = new JTextField();
        add(currentlyUsedRamPerNodeField);

        add(new JLabel("RAM Allocated per Node (GB):"));
        ramAllocatedPerNodeField = new JTextField();
        add(ramAllocatedPerNodeField);

        add(new JLabel("Total Nodes:"));
        totalNodesField = new JTextField();
        add(totalNodesField);

        add(new JLabel("RAM Usage (%):"));
        ramUsageSlider = new JSlider(0, 100, 90);
        ramUsageSlider.addChangeListener(e -> calculateRamUsage());
        add(ramUsageSlider);

        ramUsageLabel = new JLabel("90%");
        add(ramUsageLabel);

        add(new JLabel("Health:"));
        healthLabel = new JLabel();
        add(healthLabel);

        calculateRamUsage();
    }

    private void calculateRamUsage() {
        try {
            double ramAllocatedPerNode = Double.parseDouble(ramAllocatedPerNodeField.getText());
            int totalNodes = Integer.parseInt(totalNodesField.getText());
            double targetRamUsage = ramUsageSlider.getValue() / 100.0;

            double currentlyUsedRamPerNode = (ramAllocatedPerNode * totalNodes * targetRamUsage) / totalNodes;
            currentlyUsedRamPerNodeField.setText(String.format("%.2f", currentlyUsedRamPerNode));

            ramUsageLabel.setText(ramUsageSlider.getValue() + "%");

            if (ramUsageSlider.getValue() <= 50) {
                healthLabel.setText("Healthy");
                healthLabel.setForeground(new Color(0, 100, 0)); // Dark green
            } else if (ramUsageSlider.getValue() <= 80) {
                healthLabel.setText("Warning");
                healthLabel.setForeground(new Color(255, 140, 0)); // Deep orange
            } else {
                healthLabel.setText("Critical");
                healthLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            // Ignore the exception if the input fields are empty or not valid numbers
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CouchbaseSizer sizer = new CouchbaseSizer();
            sizer.setVisible(true);
        });
    }
}