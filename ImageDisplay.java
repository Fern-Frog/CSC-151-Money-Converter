import javax.swing.*;

public class ImageDisplay {

    public static void main(String[] args) {
        // Load the image from the same folder as the Java file
        ImageIcon imageIcon = new ImageIcon("Sunshine.jpg"); // Image in the same folder

        // Create a label to hold the image
        JLabel imageLabel = new JLabel(imageIcon);

        // Show the image in a JOptionPane dialog
        JOptionPane.showMessageDialog(null, imageLabel, "Image Viewer", JOptionPane.PLAIN_MESSAGE);
    }
}
