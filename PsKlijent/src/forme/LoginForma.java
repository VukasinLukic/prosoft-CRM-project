package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginForma extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnUlogujSe;

    private static final Color BG = new Color(15, 23, 42);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT = new Color(21, 101, 192);
    private static final Color BORDER = new Color(210, 216, 226);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);

    public LoginForma() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ŠV-20 Sistem — Prijava");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel pozadina = new JPanel(new GridBagLayout());
        pozadina.setBackground(BG);

        JPanel karta = new JPanel();
        karta.setLayout(new BoxLayout(karta, BoxLayout.Y_AXIS));
        karta.setBackground(CARD_BG);
        karta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(40, 44, 36, 44)));
        karta.setMaximumSize(new Dimension(380, 460));
        karta.setPreferredSize(new Dimension(380, 420));

        JLabel eyebrow = new JLabel("FAKULTET ORGANIZACIONIH NAUKA");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 10));
        eyebrow.setForeground(TEXT_MUTED);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel naslov = new JLabel("ŠV-20 Sistem");
        naslov.setFont(new Font("SansSerif", Font.BOLD, 26));
        naslov.setForeground(new Color(15, 23, 42));
        naslov.setAlignmentX(Component.LEFT_ALIGNMENT);
        naslov.setBorder(new EmptyBorder(4, 0, 30, 0));

        JLabel lblUsername = poljeLabela("Korisničko ime");
        txtUsername = poljeUnosa();

        JLabel lblPassword = poljeLabela("Lozinka");
        txtPassword = new JPasswordField();
        stilizujPolje(txtPassword);

        btnUlogujSe = new JButton("Prijavi se");
        btnUlogujSe.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnUlogujSe.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnUlogujSe.setBackground(ACCENT);
        btnUlogujSe.setForeground(Color.WHITE);
        btnUlogujSe.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnUlogujSe.setFocusPainted(false);
        btnUlogujSe.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnUlogujSe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        karta.add(eyebrow);
        karta.add(naslov);
        karta.add(lblUsername);
        karta.add(Box.createVerticalStrut(6));
        karta.add(txtUsername);
        karta.add(Box.createVerticalStrut(18));
        karta.add(lblPassword);
        karta.add(Box.createVerticalStrut(6));
        karta.add(txtPassword);
        karta.add(Box.createVerticalStrut(26));
        karta.add(btnUlogujSe);

        pozadina.add(karta);
        setContentPane(pozadina);

        FormeUtil.otvoriPunEkran(this);
        getRootPane().setDefaultButton(btnUlogujSe);
    }

    private JLabel poljeLabela(String tekst) {
        JLabel lbl = new JLabel(tekst);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(71, 85, 105));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField poljeUnosa() {
        JTextField f = new JTextField();
        stilizujPolje(f);
        return f;
    }

    private void stilizujPolje(JTextField f) {
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(6, 10, 6, 10)));
    }

    public void loginAddActionListener(java.awt.event.ActionListener actionListener) {
        btnUlogujSe.addActionListener(actionListener);
    }

    public JButton getBtnUlogujSe() { return btnUlogujSe; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JTextField getTxtUsername() { return txtUsername; }
}
