package org.couchbase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CouchbaseSizer extends JFrame {
    private JSlider ramUsageSlider;
    private JTextField currentlyUsedRamPerNodeField;
    private JTextField ramAllocatedPerNodeField;
    private JTextField totalNodesField;
    private JLabel ramUsageLabel;
    private JLabel healthLabel;
    private JTextField desiredRamUsageField; // New field for Desired RAM Usage %
    private JTextField moreNodesForDesiredRamField; // New field for More Nodes For Desired RAM Usage %

    private JButton clearButton; // New button for clearing fields

    public CouchbaseSizer() {

        setTitle("Couchbase Node Advisor");

        setSize(600, 700); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 10, 10)); // Changed to GridLayout with 9 rows and 2 columns

        add(new JLabel("RAM Usage per Node (GB):"));
        currentlyUsedRamPerNodeField = new JTextField("236");
        currentlyUsedRamPerNodeField.setEditable(false); // Make it non-editable
        currentlyUsedRamPerNodeField.setColumns(10);
        add(currentlyUsedRamPerNodeField);

        add(new JLabel("RAM Allocated per Node (GB):"));
        ramAllocatedPerNodeField = new JTextField("256");
        ramAllocatedPerNodeField.setEditable(true); // Make it non-editable
        ramAllocatedPerNodeField.setColumns(10);
        add(ramAllocatedPerNodeField);

        add(new JLabel("Total Cluster Nodes (Data/Index):"));
        totalNodesField = new JTextField("36");
        totalNodesField.setEditable(true); // Make it non-editable
        totalNodesField.setColumns(10);
        add(totalNodesField);

        // Create a new panel with BorderLayout
        JPanel sliderPanel = new JPanel(new BorderLayout());
        ramUsageSlider = new JSlider(0, 100, 90);
        ramUsageSlider.addChangeListener(e -> calculateRamUsage());
        sliderPanel.add(ramUsageSlider, BorderLayout.CENTER);

        ramUsageLabel = new JLabel("90%");
        sliderPanel.add(ramUsageLabel, BorderLayout.EAST);

        add(new JLabel("Average RAM Utilization (%):"));
        add(sliderPanel);

        // Added space
        add(new JLabel());
        add(new JLabel());
        add(new JLabel("Cluster Health Status:"));
        healthLabel = new JLabel();
        add(healthLabel);


        add(new JLabel("Desired Average RAM Usage (%):"));
        desiredRamUsageField = new JTextField();
        desiredRamUsageField.setColumns(10);
        add(desiredRamUsageField);


        add(new JLabel("Total Node Adjustment(Data/Index):"));
        moreNodesForDesiredRamField = new JTextField();
        moreNodesForDesiredRamField.setColumns(10);
        add(moreNodesForDesiredRamField);

        // Calculate the RAM usage
        calculateRamUsage();

        // New button for calculating additional nodes
        JButton calculateButton = new JButton("Analyze Node Adjustment");
        calculateButton.setBorder(BorderFactory.createRaisedBevelBorder());
        calculateButton.setBackground(Color.lightGray); // Set the background color to red
        calculateButton.setOpaque(true); // Needed for MacOS
        calculateButton.setBorderPainted(false); // Needed for MacOS
        calculateButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set the font to Arial Bold
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAdditionalNodes();
            }
        });
        add(calculateButton);

        // New button for clearing fields
        clearButton = new JButton("Clear Fields");
        clearButton.setBorder(BorderFactory.createRaisedBevelBorder());
        clearButton.setBackground(Color.lightGray); // Set the background color to red
        clearButton.setOpaque(true); // Needed for MacOS
        clearButton.setBorderPainted(false); // Needed for MacOS
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set the font to Arial Bold
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desiredRamUsageField.setText("");
                moreNodesForDesiredRamField.setText("");
            }
        });
        add(clearButton);
    }

    private void calculateRamUsage() {
        try {
            double ramAllocatedPerNode = Double.parseDouble(ramAllocatedPerNodeField.getText());
            int totalNodes = Integer.parseInt(totalNodesField.getText());

            double currentlyUsedRamPerNode = (ramAllocatedPerNode * totalNodes * ramUsageSlider.getValue() / 100.0) / totalNodes;
            currentlyUsedRamPerNodeField.setText(String.format("%.2f", currentlyUsedRamPerNode));

            ramUsageLabel.setText(ramUsageSlider.getValue() + "%");

            if (ramUsageSlider.getValue() <= 60) {
                healthLabel.setText("Healthy");
                healthLabel.setForeground(new Color(0, 100, 0)); // Dark green
                ramUsageLabel.setForeground(new Color(0, 100, 0)); // Dark green
            } else if (ramUsageSlider.getValue() <= 80) {
                healthLabel.setText("Warning");
                healthLabel.setForeground(new Color(255, 140, 0)); // Deep orange
                ramUsageLabel.setForeground(new Color(255, 140, 0)); // Deep orange
            } else {
                healthLabel.setText("Critical");
                healthLabel.setForeground(Color.RED);
                ramUsageLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            // Ignore the exception if the input fields are empty or not valid numbers
        }
    }

    // New method for calculating additional nodes
    private void calculateAdditionalNodes() {
        try {
            double ramAllocatedPerNode = Double.parseDouble(ramAllocatedPerNodeField.getText());
            int totalNodes = Integer.parseInt(totalNodesField.getText());
            double currentRamUsage = Double.parseDouble(currentlyUsedRamPerNodeField.getText()) / ramAllocatedPerNode;
            double targetRamUsage = Double.parseDouble(desiredRamUsageField.getText()) / 100.0;

            int addMoreNodes = (int) Math.ceil((currentRamUsage - targetRamUsage) * totalNodes / targetRamUsage);
            moreNodesForDesiredRamField.setText(String.valueOf(addMoreNodes));
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
