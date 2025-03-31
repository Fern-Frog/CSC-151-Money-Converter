import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.border.*;

public class Currency_converter {

    // 39 individual variables with random numbers for exchange rates
    private static double option1 = Math.random() * 10;
    private static double option2 = Math.random() * 10;
    private static double option3 = Math.random() * 10;
    private static double option4 = Math.random() * 10;
    private static double option5 = Math.random() * 10;
    private static double option6 = Math.random() * 10;
    private static double option7 = Math.random() * 10;
    private static double option8 = Math.random() * 10;
    private static double option9 = Math.random() * 10;
    private static double option10 = Math.random() * 10;
    private static double option11 = Math.random() * 10;
    private static double option12 = Math.random() * 10;
    private static double option13 = Math.random() * 10;
    private static double option14 = Math.random() * 10;
    private static double option15 = Math.random() * 10;
    private static double option16 = Math.random() * 10;
    private static double option17 = Math.random() * 10;
    private static double option18 = Math.random() * 10;
    private static double option19 = Math.random() * 10;
    private static double option20 = Math.random() * 10;
    private static double option21 = Math.random() * 10;
    private static double option22 = Math.random() * 10;
    private static double option23 = Math.random() * 10;
    private static double option24 = Math.random() * 10;
    private static double option25 = Math.random() * 10;
    private static double option26 = Math.random() * 10;
    private static double option27 = Math.random() * 10;
    private static double option28 = Math.random() * 10;
    private static double option29 = Math.random() * 10;
    private static double option30 = Math.random() * 10;
    private static double option31 = Math.random() * 10;
    private static double option32 = Math.random() * 10;
    private static double option33 = Math.random() * 10;
    private static double option34 = Math.random() * 10;
    private static double option35 = Math.random() * 10;
    private static double option36 = Math.random() * 10;
    private static double option37 = Math.random() * 10;
    private static double option38 = Math.random() * 10;
    private static double option39 = Math.random() * 10;

    private static final String[] OPTIONS = new String[39];

    static {
        // Populate OPTIONS array with 39 options displayed as Option 1, Option 2, etc.
        for (int i = 0; i < 39; i++) {
            OPTIONS[i] = "Option " + (i + 1);  // Option 1, Option 2, ...
        }
    }

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

        // Welcome message
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

        boolean continueUsingApp = true;

        while (continueUsingApp) {
            JPanel panel = new JPanel();
            panel.setBorder(createRainbowBorder());

            // Dropdown menu with 39 options (numbers as placeholders for exchange rates)
            JComboBox<String> optionComboBox = new JComboBox<>(OPTIONS);
            JPanel dropdownPanel = new JPanel();
            dropdownPanel.add(new JLabel("Select a currency conversion option:"));
            dropdownPanel.add(optionComboBox);
            int optionChoice = JOptionPane.showConfirmDialog(null, dropdownPanel, 
                "Select Currency Conversion Option", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (optionChoice != JOptionPane.OK_OPTION) {
                sayGoodbye(userName);
                break;
            }

            String selectedOption = (String) optionComboBox.getSelectedItem();
            
            // Extract the option number (e.g., "Option 1" => 1, "Option 2" => 2, ...)
            int optionIndex = Integer.parseInt(selectedOption.split(" ")[1]) - 1;  // Option 1 => index 0, Option 2 => index 1

            // Get the associated exchange rate from the fixed variables
            double selectedExchangeRate = getExchangeRate(optionIndex);

            // Handle the selected option (currently just displaying the exchange rate)
            handleOption(selectedOption, selectedExchangeRate);

            int againChoice = JOptionPane.showConfirmDialog(null, 
                "Would you like to perform another operation?",
                "Try Again?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (againChoice != JOptionPane.YES_OPTION) {
                continueUsingApp = false;
            }
        }

        sayGoodbye(userName);
    }

    private static void handleOption(String selectedOption, double exchangeRate) {
        // Display the selected option and its associated exchange rate
        JPanel panel = new JPanel();
        panel.add(new JLabel("You selected: " + selectedOption));
        panel.add(new JLabel("The associated exchange rate is: " + exchangeRate));
        JOptionPane.showMessageDialog(null, panel, "Currency Conversion", JOptionPane.PLAIN_MESSAGE);
    }

    private static double getExchangeRate(int optionIndex) {
        // Return the corresponding exchange rate for the selected option
        switch (optionIndex) {
            case 0: return option1;
            case 1: return option2;
            case 2: return option3;
            case 3: return option4;
            case 4: return option5;
            case 5: return option6;
            case 6: return option7;
            case 7: return option8;
            case 8: return option9;
            case 9: return option10;
            case 10: return option11;
            case 11: return option12;
            case 12: return option13;
            case 13: return option14;
            case 14: return option15;
            case 15: return option16;
            case 16: return option17;
            case 17: return option18;
            case 18: return option19;
            case 19: return option20;
            case 20: return option21;
            case 21: return option22;
            case 22: return option23;
            case 23: return option24;
            case 24: return option25;
            case 25: return option26;
            case 26: return option27;
            case 27: return option28;
            case 28: return option29;
            case 29: return option30;
            case 30: return option31;
            case 31: return option32;
            case 32: return option33;
            case 33: return option34;
            case 34: return option35;
            case 35: return option36;
            case 36: return option37;
            case 37: return option38;
            case 38: return option39;
            default: return 0; // Fallback
        }
    }

    private static void sayGoodbye(String name) {
        GradientPanel goodbyePanel = new GradientPanel("Goodbye, " + name + "! Thanks for using the app!");
        goodbyePanel.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(null, goodbyePanel, "Goodbye", 
                                    JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }

    private static Border createRainbowBorder() {
        Color[] rainbowColors = {
            Color.RED, Color.ORANGE, Color.YELLOW, 
            Color.GREEN, Color.BLUE, new Color(75, 0, 130),
            new Color(238, 130, 238)
        };

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
