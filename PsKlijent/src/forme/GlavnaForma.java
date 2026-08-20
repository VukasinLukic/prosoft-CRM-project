package forme;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

public class GlavnaForma extends JFrame {

    private JButton menuObrasci;
    private JButton menuStudenti;
    private JButton menuZaposleni;
    private JButton menuStudijskiProgram;
    private JButton menuTerminDezurstva;
    private JButton menuTipPolja;
    private JButton menuPodesavanja;
    private JButton menuOdjava;
    private JButton menuOProgramu;
    private JLabel lblUlogovan;

    private final Map<String, JButton> navDugmad = new LinkedHashMap<>();
    private CardLayout cardLayout;
    private JPanel sadrzajPanel;
    private String aktivniEkran;

    public static final String EKRAN_OBRASCI = "OBRASCI";
    public static final String EKRAN_STUDENTI = "STUDENTI";
    public static final String EKRAN_ZAPOSLENI = "ZAPOSLENI";
    public static final String EKRAN_STUDIJSKI_PROGRAM = "STUDIJSKI_PROGRAM";
    public static final String EKRAN_TERMINI = "TERMINI";
    public static final String EKRAN_TIPOVI_POLJA = "TIPOVI_POLJA";

    private static final Color NAV_BG = new Color(30, 41, 59);
    private static final Color NAV_ACTIVE = new Color(21, 101, 192);
    private static final Color NAV_FG = new Color(203, 213, 225);
    private static final Color NAV_FG_ACTIVE = Color.WHITE;
    private static final Color TOPBAR_BG = new Color(15, 23, 42);

    public GlavnaForma() {
        initComponents();
    }

    public GlavnaForma(domen.ZaposleniFakulteta ulogovani) {
        initComponents();
        setTitle("ŠV-20 Sistem");
        if (ulogovani != null) {
            lblUlogovan.setText(ulogovani.getIme() + " " + ulogovani.getPrezime());
        }
    }

    private void initComponents() {
        setTitle("ŠV-20 Sistem");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));

        // ── Gornja traka: naziv aplikacije + korisnik ────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(TOPBAR_BG);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 16));

        JLabel appTitle = new JLabel("ŠV-20 Sistem");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        appTitle.setForeground(Color.WHITE);
        topBar.add(appTitle, BorderLayout.WEST);

        JPanel korisnikPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        korisnikPanel.setOpaque(false);

        lblUlogovan = new JLabel("");
        lblUlogovan.setForeground(new Color(203, 213, 225));
        lblUlogovan.setFont(new Font("SansSerif", Font.PLAIN, 12));

        menuPodesavanja = utilDugme("Podešavanja");
        menuOProgramu = utilDugme("O programu");
        menuOdjava = utilDugme("Odjava");

        korisnikPanel.add(lblUlogovan);
        korisnikPanel.add(menuPodesavanja);
        korisnikPanel.add(menuOProgramu);
        korisnikPanel.add(menuOdjava);
        topBar.add(korisnikPanel, BorderLayout.EAST);

        // ── Navigacija (uvek vidljiva, ne otvara nove prozore) ───────────────
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navBar.setBackground(NAV_BG);
        navBar.setBorder(new EmptyBorder(0, 10, 0, 10));

        menuObrasci = navDugme(EKRAN_OBRASCI, "ŠV-20 Obrasci");
        menuStudenti = navDugme(EKRAN_STUDENTI, "Studenti");
        menuZaposleni = navDugme(EKRAN_ZAPOSLENI, "Zaposleni");
        menuStudijskiProgram = navDugme(EKRAN_STUDIJSKI_PROGRAM, "Studijski program");
        menuTerminDezurstva = navDugme(EKRAN_TERMINI, "Termini dežurstva");
        menuTipPolja = navDugme(EKRAN_TIPOVI_POLJA, "Tipovi polja");

        for (JButton b : navDugmad.values()) navBar.add(b);

        JPanel headerGroup = new JPanel(new BorderLayout());
        headerGroup.add(topBar, BorderLayout.NORTH);
        headerGroup.add(navBar, BorderLayout.SOUTH);

        // ── Sadržaj (CardLayout — jedan prozor, ekrani se smenjuju unutra) ───
        cardLayout = new CardLayout();
        sadrzajPanel = new JPanel(cardLayout);
        sadrzajPanel.setBackground(Color.WHITE);

        setLayout(new BorderLayout());
        add(headerGroup, BorderLayout.NORTH);
        add(sadrzajPanel, BorderLayout.CENTER);

        FormeUtil.otvoriPunEkran(this);
    }

    private JButton navDugme(String kljuc, String tekst) {
        JButton btn = new JButton(tekst);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(NAV_FG);
        btn.setBackground(NAV_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(11, 16, 11, 16));
        navDugmad.put(kljuc, btn);
        return btn;
    }

    private JButton utilDugme(String tekst) {
        JButton btn = new JButton(tekst);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(TOPBAR_BG);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Registruje ekran (jednom, pri prvom otvaranju) i odmah ga prikazuje. */
    public void registrujIPrikaziEkran(String kljuc, JComponent panel) {
        sadrzajPanel.add(panel, kljuc);
        prikaziEkran(kljuc);
    }

    public void prikaziEkran(String kljuc) {
        cardLayout.show(sadrzajPanel, kljuc);
        aktivniEkran = kljuc;
        for (Map.Entry<String, JButton> e : navDugmad.entrySet()) {
            boolean aktivan = e.getKey().equals(kljuc);
            e.getValue().setBackground(aktivan ? NAV_ACTIVE : NAV_BG);
            e.getValue().setForeground(aktivan ? NAV_FG_ACTIVE : NAV_FG);
        }
    }

    public String getAktivniEkran() { return aktivniEkran; }

    public void addSV20ObrazacListener(java.awt.event.ActionListener l) { menuObrasci.addActionListener(l); }
    public void addStudentListener(java.awt.event.ActionListener l) { menuStudenti.addActionListener(l); }
    public void addZaposleniListener(java.awt.event.ActionListener l) { menuZaposleni.addActionListener(l); }
    public void addStudijskiProgramListener(java.awt.event.ActionListener l) { menuStudijskiProgram.addActionListener(l); }
    public void addTerminDezurstvaListener(java.awt.event.ActionListener l) { menuTerminDezurstva.addActionListener(l); }
    public void addTipPoljaListener(java.awt.event.ActionListener l) { menuTipPolja.addActionListener(l); }
    public void addPodesavanjaListener(java.awt.event.ActionListener l) { menuPodesavanja.addActionListener(l); }
    public void addOdjavaListener(java.awt.event.ActionListener l) { menuOdjava.addActionListener(l); }
    public void addOProgramuListener(java.awt.event.ActionListener l) { menuOProgramu.addActionListener(l); }
}
