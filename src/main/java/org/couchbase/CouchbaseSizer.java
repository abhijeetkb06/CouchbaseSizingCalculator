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
    private JTextField desiredRamUsageField; // New field for Desired RAM Usage %
    private JTextField moreNodesForDesiredRamField; // New field for More Nodes For Desired RAM Usage %

    public CouchbaseSizer() {

        setTitle("Couchbase Cluster Sizer");
//        setSize(600, 300); // Doubled width and height

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(9, 6, 15, 20)); // Added horizontal and vertical gaps
//        setLayout(new FlowLayout((FlowLayout.TRAILING))); // Added horizontal and vertical gaps

        setSize(800, 350); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Changed to BoxLayout

        setLayout(new GridLayout(9, 2, 10, 10)); // Changed to GridLayout with 9 rows and 2 columns

        add(new JLabel("Currently Used RAM per Node (GB):"));
        currentlyUsedRamPerNodeField = new JTextField("236");
        currentlyUsedRamPerNodeField.setEditable(false); // Make it non-editable
        currentlyUsedRamPerNodeField.setColumns(10);
        add(currentlyUsedRamPerNodeField);

        add(new JLabel("RAM Allocated per Node (GB):"));
        ramAllocatedPerNodeField = new JTextField("256");
        ramAllocatedPerNodeField.setEditable(false); // Make it non-editable
        ramAllocatedPerNodeField.setColumns(10);
        add(ramAllocatedPerNodeField);

        add(new JLabel("Total Nodes:"));
        totalNodesField = new JTextField("36");
        totalNodesField.setEditable(false); // Make it non-editable
        totalNodesField.setColumns(10);
        add(totalNodesField);

        add(new JLabel("RAM Usage (%):"));
        ramUsageSlider = new JSlider(0, 100, 90);
        ramUsageSlider.addChangeListener(e -> calculateRamUsage());
        add(ramUsageSlider);

        ramUsageLabel = new JLabel("90%");
        add(ramUsageLabel);
        // Added space
        add(new JLabel());
        add(new JLabel("Health:"));
        healthLabel = new JLabel();
        add(healthLabel);

        // New fields directly at the bottom


        add(new JLabel("Desired RAM Usage (%):"));
        desiredRamUsageField = new JTextField();
        desiredRamUsageField.setColumns(10);
        add(desiredRamUsageField);



        add(new JLabel("Add More Nodes For Desired RAM Usage (%):"));
        moreNodesForDesiredRamField = new JTextField();
        moreNodesForDesiredRamField.setColumns(10);
        add(moreNodesForDesiredRamField);

        // Prepopulate the new fields
//        desiredRamUsageField.setText("50");
//        moreNodesForDesiredRamField.setText("10");

        calculateRamUsage();
    }

    private void calculateRamUsage() {
        try {
            double ramAllocatedPerNode = Double.parseDouble(ramAllocatedPerNodeField.getText());
            int totalNodes = Integer.parseInt(totalNodesField.getText());

            double currentlyUsedRamPerNode = (ramAllocatedPerNode * totalNodes * ramUsageSlider.getValue() / 100.0) / totalNodes;
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
