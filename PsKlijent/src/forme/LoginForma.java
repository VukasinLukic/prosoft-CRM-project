package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginForma extends JFrame {

    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnUlogujSe;

    public LoginForma() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ŠV-20 Sistem — Prijava");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Header
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        headerPanel.setBackground(new Color(21, 101, 192));
        headerPanel.setBorder(new EmptyBorder(28, 30, 28, 30));

        JLabel titleLabel = new JLabel("ŠV-20 Sistem", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Prijavite se na sistem", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(187, 222, 251));

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 50, 10, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        lblUsername = new JLabel("Korisničko ime");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsername.setForeground(new Color(66, 66, 66));
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(280, 34));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 18, 0);
        formPanel.add(txtUsername, gbc);

        lblPassword = new JLabel("Lozinka");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPassword.setForeground(new Color(66, 66, 66));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 4, 0);
        formPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(280, 34));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(txtPassword, gbc);

        // Button
        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(18, 50, 30, 50));

        btnUlogujSe = new JButton("Prijavi se");
        btnUlogujSe.setPreferredSize(new Dimension(280, 40));
        btnUlogujSe.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUlogujSe.setBackground(new Color(21, 101, 192));
        btnUlogujSe.setForeground(Color.WHITE);
        btnUlogujSe.setFocusPainted(false);
        btnUlogujSe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;
        btnPanel.add(btnUlogujSe, gbc2);

        JPanel content = new JPanel(new BorderLayout());
        content.add(headerPanel, BorderLayout.NORTH);
        content.add(formPanel, BorderLayout.CENTER);
        content.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(content);
        pack();
        FormeUtil.otvoriPunEkran(this);

        getRootPane().setDefaultButton(btnUlogujSe);
    }

    public void loginAddActionListener(java.awt.event.ActionListener actionListener) {
        btnUlogujSe.addActionListener(actionListener);
    }

    public JButton getBtnUlogujSe() { return btnUlogujSe; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JTextField getTxtUsername() { return txtUsername; }
}
