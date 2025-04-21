import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CurrencyConverterGUIXY extends JFrame {

    private final String[] currencyNames = {
      "Canadian Dollar (CAD)", "Mexican Peso (MXN)", "Danish Krone (DKK)", "Nicaraguan Córdoba (NIO)",
            "Honduran Lempira (HNL)", "Cuban Peso (CUP)", "Guatemalan Quetzal (GTQ)", "Panamanian Balboa (PAB)",
            "Costa Rican Colón (CRC)", "Dominican Peso (DOP)", "Haitian Gourde (HTG)", "Belize Dollar (BZD)",
            "Bahamian Dollar (BSD)", "Jamaican Dollar (JMD)", "Trinidad and Tobago Dollar (TTD)", "Euro (EUR)",
            "Eastern Caribbean Dollar (XCD)", "Caribbean Guilder (CG)", "Barbadian Dollar (BBD)", "Cayman Islands Dollar (KYD)",
            "Aruban Florin (AWG)", "Bermudian Dollar (BMD)", "Netherlands Antillean Guilder (ANG)", "Brazilian Real (BRL)",
            "Colombian Peso (COP)", "Argentine Peso (ARS)", "Peruvian Sol (PEN)", "Venezuelan Bolívar (VES)",
            "Chilean Peso (CLP)", "Bolivian Boliviano (BOB)", "Paraguayan Guaraní (PYG)", "Uruguayan Peso (UYU)",
            "Guyanese Dollar (GYD)", "Surinamese Dollar (SRD)", "US Dollar (USD)"      
    };

    // Conversion rates: value of 1 unit in USD
    private final double[] ratesToUsd = {
        0.6992, // CAD 
        0.0489, // MXN
        0.1455, // DKK
        0.0272, // NIO
        0.0391, // HNL
        0.0420, // CUP
        0.1299, // GTQ
        1.0000, // PAB
        0.0020, // CRC
        0.0158, // DOP
        0.0077, // HTG
        0.4990, // BZD
        1.0000, // BSD
        0.0064, // JMD
        0.1480, // TTD
        1.0857, // EUR
        0.3704, // XCD
        0.5586, // CG
        0.5000, // BBD
        1.2000, // KYD
        0.5585, // AWG
        1.0000, // BMD
        0.5590, // ANG
        0.1741, // BRL
        0.0002, // COP
        0.0009, // ARS
        0.2754, // PEN
        0.0139, // VES
        0.0011, // CLP
        0.1461, // BOB
        0.0001, // PYG
        0.0237, // UYU
        0.0048, // GYD
        0.0273, // SRD
        1.0000  // USD 
    };

    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;

    public CurrencyConverterGUIXY() {
        setTitle("Currency Converter");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Input field for the amount
        amountField = new JTextField();
        panel.add(new JLabel("Enter Amount:"));
        panel.add(amountField);

        // Combo box for the "from" currency
        fromCurrencyCombo = new JComboBox<>(currencyNames);
        panel.add(new JLabel("From Currency:"));
        panel.add(fromCurrencyCombo);

        // Combo box for the "to" currency
        toCurrencyCombo = new JComboBox<>(currencyNames);
        panel.add(new JLabel("To Currency:"));
        panel.add(toCurrencyCombo);

        // Conversion button
        convertButton = new JButton("Convert");
        panel.add(convertButton);

        // Result label
        resultLabel = new JLabel("Result: ", JLabel.CENTER);
        panel.add(resultLabel);

        add(panel);

        // Button action to perform conversion
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private void convertCurrency() {
        int fromIndex = fromCurrencyCombo.getSelectedIndex();
        int toIndex = toCurrencyCombo.getSelectedIndex();
        String input = amountField.getText().trim();
        double amount;

        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input.");
            return;
        }

        // Convert the amount from the source currency to USD, then to the target currency
        double amountInUsd = amount * ratesToUsd[fromIndex];
        double result = amountInUsd / ratesToUsd[toIndex];
        result = round(result);

        String message = String.format("%.4f %s = %.4f %s", amount, currencyNames[fromIndex], result, currencyNames[toIndex]);
        resultLabel.setText("Result: " + message);

        // Append the result to rEcipt.txt and print the file's absolute path
        appendResultToFile(message);
    }

    private void appendResultToFile(String message) {
        File file = new File("rEcipt.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
        }
        // Print the absolute path of the file to help locate it
        System.out.println("Result appended to: " + file.getAbsolutePath());
    }

    public static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterGUIXY::new);
    }
}
