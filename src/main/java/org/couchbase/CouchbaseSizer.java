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

    public CouchbaseSizer() {

        setTitle("Couchbase Cluster Sizer");
//        setSize(600, 300); // Doubled width and height

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new GridLayout(9, 6, 15, 20)); // Added horizontal and vertical gaps
//        setLayout(new FlowLayout((FlowLayout.TRAILING))); // Added horizontal and vertical gaps

        setSize(600, 700); // Adjust the size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Changed to BoxLayout

        setLayout(new GridLayout(9, 2, 10, 10)); // Changed to GridLayout with 9 rows and 2 columns

        add(new JLabel("Currently Used RAM per Node (GB):"));
        currentlyUsedRamPerNodeField = new JTextField("236");
        currentlyUsedRamPerNodeField.setEditable(true); // Make it non-editable
        currentlyUsedRamPerNodeField.setColumns(10);
        add(currentlyUsedRamPerNodeField);

        add(new JLabel("RAM Allocated per Node (GB):"));
        ramAllocatedPerNodeField = new JTextField("256");
        ramAllocatedPerNodeField.setEditable(true); // Make it non-editable
        ramAllocatedPerNodeField.setColumns(10);
        add(ramAllocatedPerNodeField);

        add(new JLabel("Total Nodes:"));
        totalNodesField = new JTextField("36");
        totalNodesField.setEditable(true); // Make it non-editable
        totalNodesField.setColumns(10);
        add(totalNodesField);
/////////////
   /*     add(new JLabel("RAM Usage (%):"));
        ramUsageSlider = new JSlider(0, 100, 90);
        ramUsageSlider.addChangeListener(e -> calculateRamUsage());
        add(ramUsageSlider);

        ramUsageLabel = new JLabel("90%");
        add(ramUsageLabel);*/


        // Create a new panel with BorderLayout
        JPanel sliderPanel = new JPanel(new BorderLayout());
        ramUsageSlider = new JSlider(0, 100, 90);
        ramUsageSlider.addChangeListener(e -> calculateRamUsage());
        sliderPanel.add(ramUsageSlider, BorderLayout.CENTER);

        ramUsageLabel = new JLabel("90%");
        sliderPanel.add(ramUsageLabel, BorderLayout.EAST);

        add(new JLabel("RAM Usage (%):"));
        add(sliderPanel);
        /////////////
        // Added space
        add(new JLabel());
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

        // New button for calculating additional nodes
        JButton calculateButton = new JButton("Calculate Additional Nodes");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAdditionalNodes();
            }
        });
        add(calculateButton);


      /*  // Create a new panel with BoxLayout for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(calculateButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Add the button panel to the main layout
        add(buttonPanel);*/
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
