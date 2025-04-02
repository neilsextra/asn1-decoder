package au.org.tso.decoder.asn1;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.bouncycastle.asn1.*;

public class Main extends javax.swing.JFrame implements WindowListener {

    JEditorPane asn1Pane;

    void centreWindow(Window frame) {
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    void decodeASN1(byte[] encodedData, JEditorPane asn1Pane) {
        asn1Pane.setText("");

        try {
            StringBuffer sb = new StringBuffer("<html>");
            sb.append(" <div style='white-space:nowrap;font-size:12;font-family:\"System\"'>");

            // Decode the ASN.1 structure
            ASN1InputStream asn1InputStream = new ASN1InputStream(encodedData);
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            asn1InputStream.close();

            // Assuming the structure is a sequence
            if (asn1Primitive instanceof ASN1Sequence) {
                ASN1Sequence sequence = (ASN1Sequence) asn1Primitive;
                for (ASN1Encodable element : sequence) {
                    
                    sb.append("<b>" + element.toString() + "</b>");

                    if (element instanceof DLSequence) {
 
                        sb.append("<ul>");

                        for (Object object : (DLSequence) element) {

                                sb.append("<li>" + object.toString() + "</li>");

                        }

                        sb.append("</ul>");
                    }

                    sb.append("<p/>");

                }
            }

            sb.append("</div>");
            sb.append("</html>");

            asn1Pane.setText(sb.toString());
            asn1Pane.select(0, 0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main.this, e.toString(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(e.toString());

        }

    }

    boolean loadFile(JFrame frame, JEditorPane asn1Pane) {
        try {
            var fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Specify ASN1 - file to open");

            int userSelection = fileChooser.showOpenDialog(Main.this);

            if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                byte[] bytes = new byte[(int) file.length()];

                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.read(bytes);
                }

                decodeASN1(bytes, asn1Pane);

                return true;

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Main.this, ex.toString(), "Load Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(ex.toString());
        }

        return false;
    }

    public Main(String title) {
        super(title);

        setSize(980, 640);

        JPanel toolPanel = new JPanel();
        JButton loadButton = new JButton("Load");

        loadButton.setFont(new Font("System", Font.PLAIN, 11));

        toolPanel.add(loadButton);

        javax.swing.GroupLayout toolPanelLayout = new javax.swing.GroupLayout(toolPanel);
        toolPanel.setLayout(toolPanelLayout);

        toolPanelLayout.setHorizontalGroup(toolPanelLayout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(toolPanelLayout.createSequentialGroup().addComponent(loadButton, 
                            javax.swing.GroupLayout.PREFERRED_SIZE, 80,
                            javax.swing.GroupLayout.PREFERRED_SIZE)));

        toolPanelLayout.setVerticalGroup(toolPanelLayout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(loadButton));

        toolPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel mainPanel = new JPanel(new BorderLayout());

        asn1Pane = new JEditorPane();
        asn1Pane.setContentType("text/html");
        asn1Pane.setEditable(false);

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

        jScrollPane1.setViewportView(asn1Pane);

        mainPanel.add(jScrollPane1, BorderLayout.CENTER);

        mainPanel.setPreferredSize((new Dimension(900, 600)));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(toolPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        addWindowListener(this);
        pack();
        centreWindow(this);

        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFile(Main.this, Main.this.asn1Pane);

            }

        });

    }

    public void windowClosing(WindowEvent e) {
        e.getWindow().setVisible(false);
        System.exit(0);
    }

    public void windowOpened(WindowEvent e) {

        if (!loadFile(Main.this, Main.this.asn1Pane)) {
            e.getWindow().setVisible(false);
            System.exit(0);
        }

    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main("Decoder - ASN1 Decoder").setVisible(true);
            }
        });
    }
}
