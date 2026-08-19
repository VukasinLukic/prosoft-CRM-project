package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class GlavnaForma extends JFrame {

    private JButton btnStudenti;
    private JButton btnZaposleni;
    private JButton btnSifarnici;
    private JButton btnObrasci;
    private JButton menuObrasci;
    private JButton menuStudenti;
    private JButton menuZaposleni;
    private JButton menuStudijskiProgram;
    private JButton menuPodesavanja;
    private JButton menuTerminDezurstva;
    private JButton menuTipPolja;
    private JButton menuOdjava;
    private JButton menuOProgramu;

    private static final Color NAV_BG = new Color(21, 101, 192);
    private static final Color NAV_FG = Color.WHITE;
    private static final Color NAV_HOVER = new Color(30, 136, 229);
    private static final Color CARD_BORDER = new Color(200, 220, 245);

    public GlavnaForma() {
        initComponents();
    }

    public GlavnaForma(domen.ZaposleniFakulteta ulogovani) {
        initComponents();
        if (ulogovani != null) {
            setTitle("ŠV-20 Sistem  —  " + ulogovani.getIme() + " " + ulogovani.getPrezime());
        }
    }

    private void initComponents() {
        setTitle("ŠV-20 Sistem");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 680));

        // ── Top bar ─────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(NAV_BG);
        topBar.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel appTitle = new JLabel("ŠV-20 Sistem", SwingConstants.LEFT);
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        appTitle.setForeground(NAV_FG);
        topBar.add(appTitle, BorderLayout.WEST);

        // ── Navigation bar ──────────────────────────────────────────────────
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(new Color(13, 71, 161));
        navBar.setBorder(new EmptyBorder(0, 10, 0, 10));

        menuObrasci = makeNavButton("SV-20 Obrasci");
        menuStudenti = makeNavButton("Studenti");
        menuZaposleni = makeNavButton("Zaposleni");
        menuStudijskiProgram = makeNavButton("Studijski program");
        menuTerminDezurstva = makeNavButton("Termini dežurstva");
        menuTipPolja = makeNavButton("Tipovi polja");
        menuPodesavanja = makeNavButton("Podešavanja");

        navBar.add(menuObrasci);
        navBar.add(menuStudenti);
        navBar.add(menuZaposleni);
        navBar.add(menuStudijskiProgram);
        navBar.add(menuTerminDezurstva);
        navBar.add(menuTipPolja);
        navBar.add(menuPodesavanja);

        JPanel headerGroup = new JPanel(new BorderLayout());
        headerGroup.add(topBar, BorderLayout.NORTH);
        headerGroup.add(navBar, BorderLayout.SOUTH);

        // ── Dashboard content ───────────────────────────────────────────────
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 248, 255));
        contentPanel.setBorder(new EmptyBorder(30, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        btnObrasci = makeDashCard("SV-20 Obrasci",
                "Upravljanje obrascima i\nOCR analizom dokumenata",
                new Color(21, 101, 192));
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(btnObrasci, gbc);

        btnStudenti = makeDashCard("Studenti",
                "Pregled i upravljanje\npodacima o studentima",
                new Color(0, 121, 107));
        gbc.gridx = 1; gbc.gridy = 0;
        contentPanel.add(btnStudenti, gbc);

        btnZaposleni = makeDashCard("Zaposleni",
                "Pregled i upravljanje\npodacima o zaposlenima",
                new Color(123, 31, 162));
        gbc.gridx = 2; gbc.gridy = 0;
        contentPanel.add(btnZaposleni, gbc);

        btnSifarnici = makeDashCard("Šifarnici",
                "Studijski programi, tipovi\npolja i termini dežurstva",
                new Color(183, 28, 28));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        contentPanel.add(btnSifarnici, gbc);

        // Spacer cells
        JPanel sp1 = new JPanel(); sp1.setOpaque(false);
        gbc.gridx = 1; gbc.gridy = 1;
        contentPanel.add(sp1, gbc);
        JPanel sp2 = new JPanel(); sp2.setOpaque(false);
        gbc.gridx = 2; gbc.gridy = 1;
        contentPanel.add(sp2, gbc);

        // ── Bottom bar ──────────────────────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        bottomBar.setBackground(new Color(230, 238, 255));
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 200, 230)));

        menuOdjava = new JButton("Odjava");
        menuOdjava.setFont(new Font("SansSerif", Font.BOLD, 12));
        menuOdjava.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        menuOProgramu = new JButton("O programu");
        menuOProgramu.setFont(new Font("SansSerif", Font.PLAIN, 12));
        menuOProgramu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        bottomBar.add(menuOdjava);
        bottomBar.add(menuOProgramu);

        // ── Assemble ────────────────────────────────────────────────────────
        setLayout(new BorderLayout());
        add(headerGroup, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);

        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    private JButton makeNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(NAV_FG);
        btn.setBackground(new Color(13, 71, 161));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(NAV_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(13, 71, 161));
            }
        });
        return btn;
    }

    private JButton makeDashCard(String title, String desc, Color accentColor) {
        JButton card = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setFocusPainted(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setContentAreaFilled(false);

        JLabel colorBar = new JLabel();
        colorBar.setOpaque(true);
        colorBar.setBackground(accentColor);
        colorBar.setPreferredSize(new Dimension(4, 1));

        JLabel titleLbl = new JLabel("<html><b>" + title + "</b></html>");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLbl.setForeground(new Color(33, 33, 33));

        JLabel descLbl = new JLabel("<html><small>" + desc.replace("\n", "<br>") + "</small></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setForeground(new Color(117, 117, 117));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(descLbl);

        card.add(colorBar, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240, 247, 255));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        return card;
    }

    public void addSV20ObrazacListener(java.awt.event.ActionListener l) { menuObrasci.addActionListener(l); }
    public void addStudentListener(java.awt.event.ActionListener l) { menuStudenti.addActionListener(l); }
    public void addZaposleniListener(java.awt.event.ActionListener l) { menuZaposleni.addActionListener(l); }
    public void addStudijskiProgramListener(java.awt.event.ActionListener l) { menuStudijskiProgram.addActionListener(l); }
    public void addTerminDezurstvaListener(java.awt.event.ActionListener l) { menuTerminDezurstva.addActionListener(l); }
    public void addTipPoljaListener(java.awt.event.ActionListener l) { menuTipPolja.addActionListener(l); }
    public void addPodesavanjaListener(java.awt.event.ActionListener l) { menuPodesavanja.addActionListener(l); }
    public void addOdjavaListener(java.awt.event.ActionListener l) { menuOdjava.addActionListener(l); }
    public void addOProgramuListener(java.awt.event.ActionListener l) { menuOProgramu.addActionListener(l); }
    public void addBtnObrasciListener(java.awt.event.ActionListener l) { if (btnObrasci != null) btnObrasci.addActionListener(l); }
    public void addBtnStudentiListener(java.awt.event.ActionListener l) { if (btnStudenti != null) btnStudenti.addActionListener(l); }
    public void addBtnZaposleniListener(java.awt.event.ActionListener l) { if (btnZaposleni != null) btnZaposleni.addActionListener(l); }
    public void addBtnSifarniciListener(java.awt.event.ActionListener l) { if (btnSifarnici != null) btnSifarnici.addActionListener(l); }
}
