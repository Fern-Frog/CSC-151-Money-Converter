import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class MoneyConverterFINAL extends JFrame {

    private String userName;
    private BufferedWriter receiptWriter;
    private boolean savingEnabled;
    private long sessionStartMillis;

    private final String[] currencyNames = { "Select a currency", "Argentine Peso (ARS)", "Aruban Florin (AWG)", "Bahamian Dollar (BSD)", "Barbadian Dollar (BBD)", "Belize Dollar (BZD)", "Bermudian Dollar (BMD)", "Bolivian Boliviano (BOB)", "Brazilian Real (BRL)", "Canadian Dollar (CAD)", "Cayman Islands Dollar (KYD)", "Chilean Peso (CLP)", "Colombian Peso (COP)", "Costa Rican Colón (CRC)", "Cuban Peso (CUP)", "Danish Krone (DKK)", "Dominican Peso (DOP)", "Eastern Caribbean Dollar (XCD)", "Euro (EUR)", "Guatemalan Quetzal (GTQ)", "Guyanese Dollar (GYD)", "Haitian Gourde (HTG)", "Honduran Lempira (HNL)", "Jamaican Dollar (JMD)", "Mexican Peso (MXN)", "Netherlands Antillean Guilder (ANG)", "Nicaraguan Córdoba (NIO)", "Panamanian Balboa (PAB)", "Paraguayan Guaraní (PYG)", "Peruvian Sol (PEN)", "Surinamese Dollar (SRD)", "Trinidad and Tobago Dollar (TTD)", "Uruguayan Peso (UYU)", "Venezuelan Bolívar (VES)", "US Dollar (USD)" };

    private final double[] ratesToUsd = { 0.0, 0.0009, 0.5585, 1.0000, 0.5000, 0.4990, 1.0000, 0.1461, 0.1741, 0.6992, 1.2000, 0.0011, 0.0002, 0.0020, 0.0420, 0.1455, 0.0158, 0.3704, 1.0857, 0.1299, 0.0048, 0.0077, 0.0391, 0.0064, 0.0489, 0.5590, 0.0272, 1.0000, 0.0001, 0.2754, 0.0273, 0.1480, 0.0237, 0.0139, 1.0000 };

    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextField amountField;
    private JButton convertButton;
    private JLabel resultLabel;
    private JTextField resultField;
    private JTextField finalAmountField;

    public MoneyConverterFINAL(String name) {
        this.userName = name;
        setTitle("Currency Converter - User: " + name);
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupSession();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String exitMsg = savingEnabled
                    ? "Are you sure you want to leave? Results will be saved."
                    : "Are you sure you want to leave? Progress will not be saved.";
                int confirm = JOptionPane.showConfirmDialog(null, exitMsg, "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (savingEnabled) {
                        try {
                            receiptWriter.write("--------------------------------------------------\n");
                            receiptWriter.write("Session End: " + getTimestamp() + "\n");
                            long sessionEndMillis = System.currentTimeMillis();
                            long durationMin = (sessionEndMillis - sessionStartMillis) / 60000;
                            receiptWriter.write("Duration: " + durationMin + " minute(s)\n");
                            receiptWriter.write("==================================================\n\n");
                            receiptWriter.close();
                            System.out.println("✅ Session ended. Receipt saved at: " + new File(userName + "Receipt.txt").getAbsolutePath());
                            int open = JOptionPane.showConfirmDialog(null, "Open receipt file now?", "Open File", JOptionPane.YES_NO_OPTION);
                            if (open == JOptionPane.YES_OPTION) Desktop.getDesktop().open(new File(userName + "Receipt.txt"));
                        } catch (IOException ex) { ex.printStackTrace(); }
                    }
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    private void initComponents() {
        AnimatedGradientPanel gradientPanel = new AnimatedGradientPanel(new GridLayout(11, 1, 10, 10));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        amountField = new JTextField();
        gradientPanel.add(new JLabel("Enter Amount:")); gradientPanel.add(amountField);

        fromCurrencyCombo = new JComboBox<>(currencyNames);
        gradientPanel.add(new JLabel("From Currency:")); gradientPanel.add(fromCurrencyCombo);

        toCurrencyCombo = new JComboBox<>(currencyNames);
        gradientPanel.add(new JLabel("To Currency:")); gradientPanel.add(toCurrencyCombo);

        convertButton = new JButton("Convert"); gradientPanel.add(convertButton);

        resultLabel = new JLabel("Result:", JLabel.LEFT); gradientPanel.add(resultLabel);
        resultField = new JTextField(); resultField.setEditable(false); gradientPanel.add(resultField);

        gradientPanel.add(new JLabel("Final Amount (minus 4% fee):"));
        finalAmountField = new JTextField(); finalAmountField.setEditable(false); gradientPanel.add(finalAmountField);

        setContentPane(gradientPanel);
        convertButton.addActionListener(e -> convertCurrency());
    }

    private void convertCurrency() {
        int fromIndex = fromCurrencyCombo.getSelectedIndex(), toIndex = toCurrencyCombo.getSelectedIndex();
        String input = amountField.getText().trim();
        if (fromIndex == 0 || toIndex == 0) { JOptionPane.showMessageDialog(this, "Please select valid currencies."); return; }
        double amount;
        try { amount = Double.parseDouble(input); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid number input."); return; }
        if (amount < 0) { JOptionPane.showMessageDialog(this, "Please enter positive numbers."); return; }

        double amountInUsd = amount * ratesToUsd[fromIndex];
        double result = Math.round((amountInUsd / ratesToUsd[toIndex]) * 100.0) / 100.0;
        double fee = Math.round(result * 0.04 * 100.0) / 100.0;
        double finalAmount = Math.round((result - fee) * 100.0) / 100.0;

        String message = String.format("%.2f %s = %.2f %s",
            amount, currencyNames[fromIndex], result, currencyNames[toIndex]);
        resultField.setText(message);
        finalAmountField.setText(String.format("%.2f %s", finalAmount, currencyNames[toIndex]));

        if (savingEnabled) {
            try {
                receiptWriter.write(message + "\n");
                receiptWriter.write("Fee (4%): " + fee + " " + currencyNames[toIndex] + "\n");
                receiptWriter.write("\n");
                receiptWriter.write("**Final Amount:    " + finalAmount + " " + currencyNames[toIndex] + "**\n\n");
                receiptWriter.flush();
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "Error writing to receipt."); }
        }
    }

    private void setupSession() {
        File file = new File(userName + "Receipt.txt"); savingEnabled = false;
        if (!file.exists()) {
            if (JOptionPane.showConfirmDialog(null, "Receipt file not found. Create a new file?", "Create Receipt", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) savingEnabled = true;
        } else {
            if (JOptionPane.showConfirmDialog(null, "Want to continue your session?", "Continue Session", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) savingEnabled = true;
        }
        if (savingEnabled) {
            try { receiptWriter = new BufferedWriter(new FileWriter(file, true)); receiptWriter.write("Session Start: " + getTimestamp() + "\n"); receiptWriter.flush(); sessionStartMillis = System.currentTimeMillis(); System.out.println("📁 Receipt file: " + file.getAbsolutePath()); }
            catch (IOException e) { JOptionPane.showMessageDialog(this, "Could not open or create receipt file."); savingEnabled = false; }
        } else JOptionPane.showMessageDialog(null, "Progress will not be saved for this session.");
    }

    private String getTimestamp() {
        // Include year-month-day hour:minute:second
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static class AnimatedGradientPanel extends JPanel implements ActionListener {
        private float hue = 0.0f; private final Timer timer;
        AnimatedGradientPanel(LayoutManager layout) { super(layout); timer = new Timer(50, this); timer.start(); }
        @Override protected void paintComponent(Graphics g) { super.paintComponent(g); int w = getWidth(), h = getHeight(); Graphics2D g2 = (Graphics2D) g; Color c1 = Color.getHSBColor(hue, 1f, 1f), c2 = Color.getHSBColor((hue+0.5f)%1f,1f,1f); g2.setPaint(new GradientPaint(0,0,c1,w,h,c2)); g2.fillRect(0,0,w,h);}        
        @Override public void actionPerformed(ActionEvent e) { hue += 0.005f; if (hue>1f) hue-=1f; repaint(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String name;
            while (true) {
                name = JOptionPane.showInputDialog(null, "Welcome! Please enter your name:");
                if (name == null) System.exit(0);
                name = name.trim();
                if (name.length() > 30) { JOptionPane.showMessageDialog(null, "Name cannot exceed 30 characters. Please enter a shorter name."); continue; }
                if (!name.matches("[A-Za-z ]+")) { JOptionPane.showMessageDialog(null, "Name cannot contain symbols. Please enter a valid name."); continue; }
                if (name.isEmpty()) { JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter your name."); continue; }
                break;
            }
            JOptionPane.showMessageDialog(null, "Welcome, " + name + "!");
            new MoneyConverterFINAL(name);
        });
    }
}
