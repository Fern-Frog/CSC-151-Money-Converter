import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrencyConverterGUI3 extends JFrame {

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
            0.6985, 0.0489, 0.1455, 0.0271, 0.039, 0.0419, 0.13, 1.00, 0.002,
            0.0158, 0.0076, 0.4986, 1.00, 0.0064, 0.1452, 1.0855, 0.148, 0.5586,
            0.5, 1.2, 0.5523, 1.00, 0.559, 0.174, 0.0002, 0.0009, 0.2726,
            0.0000001, 0.001, 0.146, 0.0001, 0.0237, 0.0047, 0.0273, 1.0
    };

    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;

    public CurrencyConverterGUI3() {
        setTitle("Currency Converter");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
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
    }

    public static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterGUI3::new);
    }
}
