import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class CurrencyConverterWithSessionTracking extends JFrame {

    private String userName;
    private BufferedWriter receiptWriter;
    private boolean savingEnabled;

    private final String[] currencyNames = {
        "Select a currency", "Canadian Dollar (CAD)", "Mexican Peso (MXN)", "Danish Krone (DKK)", "Nicaraguan Córdoba (NIO)",
        "Honduran Lempira (HNL)", "Cuban Peso (CUP)", "Guatemalan Quetzal (GTQ)", "Panamanian Balboa (PAB)",
        "Costa Rican Colón (CRC)", "Dominican Peso (DOP)", "Haitian Gourde (HTG)", "Belize Dollar (BZD)",
        "Bahamian Dollar (BSD)", "Jamaican Dollar (JMD)", "Trinidad and Tobago Dollar (TTD)", "Euro (EUR)",
        "Eastern Caribbean Dollar (XCD)", "Caribbean Guilder (CG)", "Barbadian Dollar (BBD)", "Cayman Islands Dollar (KYD)",
        "Aruban Florin (AWG)", "Bermudian Dollar (BMD)", "Netherlands Antillean Guilder (ANG)", "Brazilian Real (BRL)",
        "Colombian Peso (COP)", "Argentine Peso (ARS)", "Peruvian Sol (PEN)", "Venezuelan Bolívar (VES)",
        "Chilean Peso (CLP)", "Bolivian Boliviano (BOB)", "Paraguayan Guaraní (PYG)", "Uruguayan Peso (UYU)",
        "Guyanese Dollar (GYD)", "Surinamese Dollar (SRD)", "US Dollar (USD)"
    };

    private final double[] ratesToUsd = {
        0.0, // Placeholder
        0.6992, 0.0489, 0.1455, 0.0272, 0.0391, 0.0420, 0.1299, 1.0000,
        0.0020, 0.0158, 0.0077, 0.4990, 1.0000, 0.0064, 0.1480, 1.0857,
        0.3704, 0.5586, 0.5000, 1.2000, 0.5585, 1.0000, 0.5590, 0.1741,
        0.0002, 0.0009, 0.2754, 0.0139, 0.0011, 0.1461, 0.0001, 0.0237,
        0.0048, 0.0273, 1.0000
    };

    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;

    public CurrencyConverterWithSessionTracking(String name) {
        this.userName = name;
        setTitle("Currency Converter - User: " + name);
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupSession();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String exitMsg = savingEnabled
                    ? "Are you sure you want to leave? Results will be saved."
                    : "Are you sure you want to leave? Progress will not be saved.";

                int confirm = JOptionPane.showConfirmDialog(
                    null,
                    exitMsg,
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (savingEnabled) {
                        try {
                            receiptWriter.write("Session End: " + getTimestamp() + "\n\n");
                            receiptWriter.close();
                            System.out.println("✅ Session ended. Receipt saved at: " +
                                new File(userName + "Receipt.txt").getAbsolutePath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        amountField = new JTextField();
        panel.add(new JLabel("Enter Amount:"));
        panel.add(amountField);

        fromCurrencyCombo = new JComboBox<>(currencyNames);
        panel.add(new JLabel("From Currency:"));
        panel.add(fromCurrencyCombo);

        toCurrencyCombo = new JComboBox<>(currencyNames);
        panel.add(new JLabel("To Currency:"));
        panel.add(toCurrencyCombo);

        convertButton = new JButton("Convert");
        panel.add(convertButton);

        resultLabel = new JLabel("Result: ", JLabel.CENTER);
        panel.add(resultLabel);

        add(panel);

        convertButton.addActionListener(e -> convertCurrency());
    }

    private void convertCurrency() {
        int fromIndex = fromCurrencyCombo.getSelectedIndex();
        int toIndex = toCurrencyCombo.getSelectedIndex();
        String input = amountField.getText().trim();

        if (fromIndex == 0 || toIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select valid currencies.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number input.");
            return;
        }

        if (amount < 0) {
            JOptionPane.showMessageDialog(this, "Please enter positive numbers.");
            return;
        }

        double amountInUsd = amount * ratesToUsd[fromIndex];
        double result = amountInUsd / ratesToUsd[toIndex];
        result = round(result);

        String message = String.format("%.4f %s = %.4f %s", amount,
            currencyNames[fromIndex], result, currencyNames[toIndex]);
        resultLabel.setText("Result: " + message);

        if (savingEnabled) {
            try {
                receiptWriter.write(message + "\n");
                receiptWriter.flush();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing to receipt.");
            }
        }
    }

    private void setupSession() {
        File file = new File(userName + "Receipt.txt");
        savingEnabled = false;

        if (!file.exists()) {
            int create = JOptionPane.showConfirmDialog(
                null,
                "Receipt file not found. Create a new file?",
                "Create Receipt",
                JOptionPane.YES_NO_OPTION
            );
            if (create == JOptionPane.YES_OPTION) {
                savingEnabled = true;
            }
        } else {
            int cont = JOptionPane.showConfirmDialog(
                null,
                "Want to continue your session?",
                "Continue Session",
                JOptionPane.YES_NO_OPTION
            );
            if (cont == JOptionPane.YES_OPTION) {
                savingEnabled = true;
            }
        }

        if (savingEnabled) {
            try {
                receiptWriter = new BufferedWriter(new FileWriter(file, true));
                receiptWriter.write("Session Start: " + getTimestamp() + "\n");
                receiptWriter.flush();
                System.out.println("📁 Receipt file: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not open or create receipt file.");
                savingEnabled = false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Progress will not be saved for this session.");
        }
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String name;
            while (true) {
                name = JOptionPane.showInputDialog(null, "Welcome! Please enter your name:");
                if (name == null) {
                    System.exit(0);
                }
                name = name.trim();
                if (!name.matches("[A-Za-z ]+")) {
                    JOptionPane.showMessageDialog(null, "Name cannot contain symbols. Please enter a valid name.");
                    continue;
                }
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter your name.");
                    continue;
                }
                break;
            }
            JOptionPane.showMessageDialog(null, "Welcome, " + name + "!");
            new CurrencyConverterWithSessionTracking(name);
        });
    }
}
