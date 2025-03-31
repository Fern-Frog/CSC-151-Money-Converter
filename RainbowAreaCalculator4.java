package com.mycompany.rainbowareacalculator;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.*;

public class RainbowAreaCalculator4 {
    private static final Color[] rainbowColors = {
        Color.RED, Color.ORANGE, Color.YELLOW, 
        Color.GREEN, Color.BLUE, new Color(75, 0, 130),
        new Color(238, 130, 238)
    };

    private static final String[] AREA_UNITS = {
        "Square Meters", "Square Centimeters", "Square Millimeters",
        "Square Inches", "Square Feet", "Square Yards"
    };

    private static final String[] VOLUME_UNITS = {
        "Cubic Meters", "Cubic Centimeters", "Cubic Millimeters",
        "Cubic Inches", "Cubic Feet", "Cubic Yards"
    };

    private static final double[] AREA_CONVERSION_FACTORS = {
        1.0, 10000.0, 1000000.0,
        1550.003, 10.764, 1.196
    };

    private static final double[] VOLUME_CONVERSION_FACTORS = {
        1.0, 1000000.0, 1000000000.0,
        61023.744, 35.315, 1.308
    };

    private static class GradientPanel extends JPanel {
        private float hue = 0;

        public GradientPanel(String message) {
            setLayout(new BorderLayout());
            JLabel label = new JLabel(message, SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            add(label, BorderLayout.CENTER);
            startAnimation();
        }

        private void startAnimation() {
            Timer timer = new Timer(50, e -> {
                hue = (hue + 0.01f) % 1;
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            GradientPaint gradient = new GradientPaint(
                0, 0, Color.getHSBColor(hue, 0.8f, 0.9f),
                w, h, Color.getHSBColor((hue + 0.3f) % 1, 0.8f, 0.9f)
            );
            g2d.setPaint(gradient);
            g2d.fill(new Rectangle2D.Double(0, 0, w, h));
        }
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        GradientPanel namePanel = new GradientPanel("Please enter your name:");
        namePanel.setPreferredSize(new Dimension(300, 100));
        String userName = JOptionPane.showInputDialog(null, namePanel, "Enter Name", 
                                                    JOptionPane.PLAIN_MESSAGE);
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Guest";
        }

        GradientPanel welcomePanel = new GradientPanel("Welcome, " + userName + "!");
        welcomePanel.setPreferredSize(new Dimension(300, 100));
        JOptionPane.showMessageDialog(null, welcomePanel, "Welcome", 
                                    JOptionPane.PLAIN_MESSAGE);

        boolean continueCalculating = true;

        while (continueCalculating) {
            JPanel panel = new JPanel();
            panel.setBorder(createRainbowBorder());

            String[] options = {"2D Area", "3D Volume"};
            int choice = JOptionPane.showOptionDialog(null, "Choose calculation type:",
                "Rainbow Calculator", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                sayGoodbye(userName);
            }

            if (choice == 0) {
                calculate2DArea(panel);
            } else if (choice == 1) {
                calculate3DVolume(panel);
            }

            int againChoice = JOptionPane.showConfirmDialog(null, 
                "Would you like to perform another calculation?",
                "Calculate Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (againChoice != JOptionPane.YES_OPTION) {
                continueCalculating = false;
            }
        }

        sayGoodbye(userName);
    }

    private static void calculate2DArea(JPanel panel) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField lengthField = new JTextField(10);
        JTextField widthField = new JTextField(10);
        JComboBox<String> unitComboBox = new JComboBox<>(AREA_UNITS);
        
        inputPanel.add(new JLabel("Length:"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("Width:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("Unit:"));
        inputPanel.add(unitComboBox);
        
        int result = JOptionPane.showConfirmDialog(null, inputPanel, 
            "Enter 2D Dimensions", JOptionPane.OK_CANCEL_OPTION);
            
        if (result != JOptionPane.OK_OPTION) exitProgram();
        
        try {
            double length = Double.parseDouble(lengthField.getText());
            double width = Double.parseDouble(widthField.getText());
            int selectedUnit = unitComboBox.getSelectedIndex();
            
            GradientPanel confirmPanel = new GradientPanel(
                "<html>Are you sure you want to calculate with these values?<br>" +
                "Length: " + length + "<br>" +
                "Width: " + width + "<br>" +
                "Unit: " + AREA_UNITS[selectedUnit] + "</html>");
            confirmPanel.setPreferredSize(new Dimension(300, 150));
            
            int confirm = JOptionPane.showConfirmDialog(null, confirmPanel,
                "Confirm Calculation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                double area = length * width;
                double convertedArea = area * AREA_CONVERSION_FACTORS[selectedUnit];
                showRainbowResult(String.format("Area: %.2f %s", convertedArea, AREA_UNITS[selectedUnit]), panel);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void calculate3DVolume(JPanel panel) {
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField lengthField = new JTextField(10);
        JTextField widthField = new JTextField(10);
        JTextField heightField = new JTextField(10);
        JComboBox<String> unitComboBox = new JComboBox<>(VOLUME_UNITS);
        
        inputPanel.add(new JLabel("Length:"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("Width:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("Height:"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("Unit:"));
        inputPanel.add(unitComboBox);
        
        int result = JOptionPane.showConfirmDialog(null, inputPanel, 
            "Enter 3D Dimensions", JOptionPane.OK_CANCEL_OPTION);
            
        if (result != JOptionPane.OK_OPTION) exitProgram();
        
        try {
            double length = Double.parseDouble(lengthField.getText());
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());
            int selectedUnit = unitComboBox.getSelectedIndex();
            
            GradientPanel confirmPanel = new GradientPanel(
                "<html>Are you sure you want to calculate with these values?<br>" +
                "Length: " + length + "<br>" +
                "Width: " + width + "<br>" +
                "Height: " + height + "<br>" +
                "Unit: " + VOLUME_UNITS[selectedUnit] + "</html>");
            confirmPanel.setPreferredSize(new Dimension(300, 150));
            
            int confirm = JOptionPane.showConfirmDialog(null, confirmPanel,
                "Confirm Calculation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                double volume = length * width * height;
                double convertedVolume = volume * VOLUME_CONVERSION_FACTORS[selectedUnit];
                showRainbowResult(String.format("Volume: %.2f %s", convertedVolume, VOLUME_UNITS[selectedUnit]), panel);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void sayGoodbye(String name) {
        GradientPanel goodbyePanel = new GradientPanel("Goodbye, " + name + "! Thanks for your numbers!");
        goodbyePanel.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(null, goodbyePanel, "Goodbye", 
                                    JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }

    private static void exitProgram() {
        System.exit(0);
    }

    private static void showRainbowResult(String message, JPanel panel) {
        JLabel resultLabel = new JLabel(message);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.removeAll();
        panel.add(resultLabel);
        
        Timer timer = new Timer(100, e -> {
            Border border = createRainbowBorder();
            panel.setBorder(border);
            panel.repaint();
        });
        timer.start();
        
        JOptionPane.showMessageDialog(null, panel, "Result", 
            JOptionPane.PLAIN_MESSAGE);
        timer.stop();
    }

    private static Border createRainbowBorder() {
        CompoundBorder[] borders = new CompoundBorder[rainbowColors.length];
        for (int i = 0; i < rainbowColors.length; i++) {
            borders[i] = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(rainbowColors[i], 2),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
            );
        }
        
        CompoundBorder finalBorder = borders[0];
        for (int i = 1; i < borders.length; i++) {
            finalBorder = BorderFactory.createCompoundBorder(finalBorder, borders[i]);
        }
        return finalBorder;
    }
}
