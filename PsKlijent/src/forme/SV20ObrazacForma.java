package forme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class SV20ObrazacForma extends JDialog {

    // ── Kartica "Lista/unos" ─────────────────────────────────────────────────
    private JComboBox cmbStudent;
    private JComboBox cmbZaposleni;
    private JComboBox cmbStatus;
    private JComboBox cmbKriterijum;
    private JSpinner spnSkolskaGodina;
    private JSpinner spnSemestar;
    private JTextField txtPutanjaFajla;
    private JTextField txtPretraga;
    private JScrollPane jScrollPane1;
    private JTable tblObrasci;
    private JButton btnOdaberiFajl;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;
    private JButton btnOcisti;
    private JButton btnPretrazi;
    private JButton btnPokreniOcr;
    private JButton btnPregledajStavke;
    private JLabel lblSazetakStavki;
    private JLabel lblSlikaLista;
    private JLabel lblStranaLista;
    private JButton btnPrethodnaStranaLista;
    private JButton btnSledecaStranaLista;

    // ── Kartica "OCR pregled" ────────────────────────────────────────────────
    private JLabel lblSlikaPregled;
    private JLabel lblStranaPregled;
    private JButton btnPrethodnaStranaPregled;
    private JButton btnSledecaStranaPregled;
    private JLabel lblSazetakPregled;
    private JPanel pnlStavke;
    private JButton btnNazad;
    private JButton btnSacuvajStavke;
    private JButton btnPonoviOcr;

    private final Map<Integer, JTextField> poljaZaKorekciju = new LinkedHashMap<>();

    private DefaultTableModel tableModelObrasci;
    private domen.SV20Obrazac selektovaniObrazac;
    private CardLayout cardLayout;
    private JPanel cardsPanel;

    private static final Color BORDER = new Color(200, 210, 220);
    private static final Color TEXT_MUTED = new Color(110, 118, 128);
    private static final Color ACCENT = new Color(38, 70, 110);
    private static final Color OK = new Color(46, 125, 50);
    private static final Color WARN = new Color(158, 106, 0);
    private static final Color BAD = new Color(178, 40, 40);

    public static final String KARTICA_LISTA = "LISTA";
    public static final String KARTICA_PREGLED = "PREGLED";

    public SV20ObrazacForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SV20ObrazacForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModels();
    }

    private void initComponents() {
        setTitle("Upravljanje ŠV-20 Obrascima");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.add(izgradiKarticuListe(), KARTICA_LISTA);
        cardsPanel.add(izgradiKarticuPregleda(), KARTICA_PREGLED);

        setLayout(new BorderLayout());
        add(cardsPanel, BorderLayout.CENTER);

        initCustomTableModels();
        setSize(1220, 900);
        setLocationRelativeTo(null);
    }

    public void prikaziKarticu(String naziv) {
        cardLayout.show(cardsPanel, naziv);
    }

    // ── Kartica 1: Lista/unos ────────────────────────────────────────────────

    private JPanel izgradiKarticuListe() {
        cmbStudent = new JComboBox();
        cmbZaposleni = new JComboBox();
        cmbStatus = new JComboBox();
        cmbKriterijum = new JComboBox(new String[]{"Indeks studenta", "Zaposleni", "Status"});
        spnSkolskaGodina = new JSpinner(new SpinnerNumberModel(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), 2000, 2100, 1));
        spnSemestar = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        txtPutanjaFajla = new JTextField();
        txtPretraga = new JTextField();

        tblObrasci = new JTable();
        jScrollPane1 = new JScrollPane(tblObrasci);

        btnOdaberiFajl = dugme("Odaberi fajl...", new Color(97, 97, 97));
        btnDodaj = dugme("Dodaj", new Color(38, 70, 110));
        btnSacuvaj = dugme("Sačuvaj", OK);
        btnObrisi = dugme("Obriši", BAD);
        btnOcisti = dugme("Očisti", new Color(97, 97, 97));
        btnPretrazi = dugme("Pretraži", new Color(38, 70, 110));

        btnPokreniOcr = dugme("Obradi OCR", new Color(180, 90, 20));
        btnPokreniOcr.setPreferredSize(new Dimension(160, 34));
        btnPokreniOcr.setToolTipText("Šalje ceo fajl (obe strane) OCR servisu i otvara pregled za korekciju");

        // ── Pretraga ─────────────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(naslovljenaIvica("Pretraga obrazaca"));
        searchPanel.add(new JLabel("Kriterijum:"));
        searchPanel.add(cmbKriterijum);
        txtPretraga.setPreferredSize(new Dimension(340, 28));
        searchPanel.add(txtPretraga);
        searchPanel.add(btnPretrazi);

        // ── Podaci o obrascu ─────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(naslovljenaIvica("Podaci o obrascu"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Student:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridwidth = 3;
        cmbStudent.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbStudent, g);
        g.weightx = 0; g.gridwidth = 1;

        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Zaposleni:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridwidth = 3;
        cmbZaposleni.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbZaposleni, g);
        g.weightx = 0; g.gridwidth = 1;

        g.gridx = 0; g.gridy = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Školska godina:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        spnSkolskaGodina.setPreferredSize(new Dimension(100, 28));
        formPanel.add(spnSkolskaGodina, g);
        g.gridx = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Semestar:"), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL;
        spnSemestar.setPreferredSize(new Dimension(80, 28));
        formPanel.add(spnSemestar, g);

        g.gridx = 0; g.gridy = 3; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("Status:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        cmbStatus.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbStatus, g);

        g.gridx = 0; g.gridy = 4; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("Sken (PDF, obe strane):"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridwidth = 2;
        txtPutanjaFajla.setPreferredSize(new Dimension(300, 28));
        formPanel.add(txtPutanjaFajla, g);
        g.gridwidth = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        g.gridx = 3;
        formPanel.add(btnOdaberiFajl, g);

        g.gridx = 0; g.gridy = 5; g.gridwidth = 4; g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(12, 8, 5, 8);
        formPanel.add(btnPokreniOcr, g);
        g.gridwidth = 1;
        g.insets = new Insets(5, 8, 5, 8);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        crudPanel.setBackground(Color.WHITE);
        crudPanel.add(btnOcisti);
        crudPanel.add(btnObrisi);
        crudPanel.add(btnSacuvaj);
        crudPanel.add(btnDodaj);

        JPanel formWithButtons = new JPanel(new BorderLayout(0, 4));
        formWithButtons.setBackground(Color.WHITE);
        formWithButtons.add(formPanel, BorderLayout.CENTER);
        formWithButtons.add(crudPanel, BorderLayout.SOUTH);

        // ── Pregled skena (desno) ────────────────────────────────────────────
        JPanel previewPanel = new JPanel(new BorderLayout(0, 6));
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(naslovljenaIvica("Pregled skeniranog obrasca"));

        lblSlikaLista = new JLabel("Fajl nije izabran", SwingConstants.CENTER);
        lblSlikaLista.setForeground(TEXT_MUTED);
        lblSlikaLista.setVerticalAlignment(SwingConstants.CENTER);
        JScrollPane slikaScroll = new JScrollPane(lblSlikaLista);
        slikaScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        slikaScroll.setPreferredSize(new Dimension(400, 420));

        btnPrethodnaStranaLista = ikonicaDugme("‹");
        btnSledecaStranaLista = ikonicaDugme("›");
        lblStranaLista = new JLabel("—", SwingConstants.CENTER);
        lblStranaLista.setPreferredSize(new Dimension(110, 24));
        lblStranaLista.setForeground(TEXT_MUTED);
        btnPrethodnaStranaLista.setEnabled(false);
        btnSledecaStranaLista.setEnabled(false);

        JPanel flipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        flipPanel.setBackground(Color.WHITE);
        flipPanel.add(btnPrethodnaStranaLista);
        flipPanel.add(lblStranaLista);
        flipPanel.add(btnSledecaStranaLista);

        previewPanel.add(slikaScroll, BorderLayout.CENTER);
        previewPanel.add(flipPanel, BorderLayout.SOUTH);

        JPanel k1Grid = new JPanel(new GridLayout(1, 2, 12, 0));
        k1Grid.setBackground(Color.WHITE);
        k1Grid.add(formWithButtons);
        k1Grid.add(previewPanel);

        // ── Lista obrazaca ───────────────────────────────────────────────────
        JPanel obrazaciPanel = new JPanel(new BorderLayout());
        obrazaciPanel.setBackground(Color.WHITE);
        obrazaciPanel.setBorder(naslovljenaIvica("Lista obrazaca  (dvostruki klik = otvori OCR pregled)"));
        jScrollPane1.setPreferredSize(new Dimension(1100, 260));
        obrazaciPanel.add(jScrollPane1, BorderLayout.CENTER);

        // ── Sažetak stavki (zamenjuje staru tabelu stavki) ───────────────────
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 0));
        summaryPanel.setBackground(new Color(246, 248, 250));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(10, 12, 10, 12)));
        lblSazetakStavki = new JLabel("Izaberite obrazac iz liste da vidite OCR stavke.");
        lblSazetakStavki.setForeground(TEXT_MUTED);
        btnPregledajStavke = dugme("Pregledaj OCR stavke", new Color(38, 70, 110));
        btnPregledajStavke.setEnabled(false);
        summaryPanel.add(lblSazetakStavki, BorderLayout.CENTER);
        summaryPanel.add(btnPregledajStavke, BorderLayout.EAST);

        JPanel kartica = new JPanel(new BorderLayout(4, 8));
        kartica.setBackground(Color.WHITE);
        kartica.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel gornjiDeo = new JPanel(new BorderLayout(0, 8));
        gornjiDeo.setBackground(Color.WHITE);
        gornjiDeo.add(searchPanel, BorderLayout.NORTH);
        gornjiDeo.add(k1Grid, BorderLayout.CENTER);

        JPanel donjiDeo = new JPanel(new BorderLayout(0, 8));
        donjiDeo.setBackground(Color.WHITE);
        donjiDeo.add(obrazaciPanel, BorderLayout.CENTER);
        donjiDeo.add(summaryPanel, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gornjiDeo, donjiDeo);
        split.setDividerLocation(430);
        split.setResizeWeight(0.55);
        split.setBorder(null);

        kartica.add(split, BorderLayout.CENTER);
        return kartica;
    }

    // ── Kartica 2: OCR pregled ───────────────────────────────────────────────

    private JPanel izgradiKarticuPregleda() {
        JPanel kartica = new JPanel(new BorderLayout(0, 8));
        kartica.setBackground(Color.WHITE);
        kartica.setBorder(new EmptyBorder(8, 8, 8, 8));

        lblSazetakPregled = new JLabel(" ");
        lblSazetakPregled.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSazetakPregled.setForeground(TEXT_MUTED);
        lblSazetakPregled.setBorder(new EmptyBorder(2, 4, 6, 4));

        // ── Slika (levo) ─────────────────────────────────────────────────────
        JPanel slikaPanel = new JPanel(new BorderLayout(0, 6));
        slikaPanel.setBackground(Color.WHITE);
        slikaPanel.setBorder(naslovljenaIvica("Skenirani obrazac"));
        slikaPanel.setPreferredSize(new Dimension(430, 10));

        lblSlikaPregled = new JLabel("", SwingConstants.CENTER);
        lblSlikaPregled.setForeground(TEXT_MUTED);
        JScrollPane slikaScroll = new JScrollPane(lblSlikaPregled);
        slikaScroll.setBorder(BorderFactory.createLineBorder(BORDER));

        btnPrethodnaStranaPregled = ikonicaDugme("‹");
        btnSledecaStranaPregled = ikonicaDugme("›");
        lblStranaPregled = new JLabel("—", SwingConstants.CENTER);
        lblStranaPregled.setPreferredSize(new Dimension(110, 24));
        lblStranaPregled.setForeground(TEXT_MUTED);

        JPanel flipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        flipPanel.setBackground(Color.WHITE);
        flipPanel.add(btnPrethodnaStranaPregled);
        flipPanel.add(lblStranaPregled);
        flipPanel.add(btnSledecaStranaPregled);

        slikaPanel.add(slikaScroll, BorderLayout.CENTER);
        slikaPanel.add(flipPanel, BorderLayout.SOUTH);

        // ── Polja (desno) ────────────────────────────────────────────────────
        JPanel poljaPanel = new JPanel(new BorderLayout());
        poljaPanel.setBackground(Color.WHITE);
        poljaPanel.setBorder(naslovljenaIvica("Prepoznata polja — koriguj po potrebi"));

        pnlStavke = new JPanel();
        pnlStavke.setBackground(Color.WHITE);
        pnlStavke.setLayout(new BoxLayout(pnlStavke, BoxLayout.Y_AXIS));
        JScrollPane stavkeScroll = new JScrollPane(pnlStavke);
        stavkeScroll.setBorder(null);
        stavkeScroll.getVerticalScrollBar().setUnitIncrement(16);
        poljaPanel.add(stavkeScroll, BorderLayout.CENTER);

        JPanel split = new JPanel(new BorderLayout(12, 0));
        split.setBackground(Color.WHITE);
        split.add(slikaPanel, BorderLayout.WEST);
        split.add(poljaPanel, BorderLayout.CENTER);

        // ── Dugmad ───────────────────────────────────────────────────────────
        btnNazad = dugme("Nazad na obrazac", new Color(97, 97, 97));
        btnPonoviOcr = dugme("Ponovi OCR", new Color(150, 150, 150));
        btnPonoviOcr.setPreferredSize(new Dimension(120, 28));
        btnSacuvajStavke = dugme("Sačuvaj stavke", OK);

        JPanel akcijePanel = new JPanel(new BorderLayout());
        akcijePanel.setBackground(Color.WHITE);
        akcijePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(10, 0, 0, 0)));
        JPanel levo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        levo.setBackground(Color.WHITE);
        levo.add(btnNazad);
        levo.add(btnPonoviOcr);
        JPanel desno = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        desno.setBackground(Color.WHITE);
        desno.add(btnSacuvajStavke);
        akcijePanel.add(levo, BorderLayout.WEST);
        akcijePanel.add(desno, BorderLayout.EAST);

        kartica.add(lblSazetakPregled, BorderLayout.NORTH);
        kartica.add(split, BorderLayout.CENTER);
        kartica.add(akcijePanel, BorderLayout.SOUTH);
        return kartica;
    }

    public void prikaziStavke(List<domen.StavkeObrasca> stavke) {
        pnlStavke.removeAll();
        poljaZaKorekciju.clear();
        int trenutnaStrana = -1;
        for (domen.StavkeObrasca s : stavke) {
            int strana = s.getIdPolja() != null ? s.getIdPolja().getStranica() : 1;
            if (strana != trenutnaStrana) {
                trenutnaStrana = strana;
                pnlStavke.add(sekcijaNaslov("Strana " + trenutnaStrana));
            }
            pnlStavke.add(redPolja(s));
        }
        pnlStavke.add(Box.createVerticalGlue());
        pnlStavke.revalidate();
        pnlStavke.repaint();
    }

    private JComponent sekcijaNaslov(String tekst) {
        JLabel lbl = new JLabel(tekst);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(ACCENT);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 4, 5, 4)));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl.getPreferredSize().height));
        return lbl;
    }

    private JComponent redPolja(domen.StavkeObrasca s) {
        String naziv = s.getIdPolja() != null ? s.getIdPolja().getNazivPolja() : "?";
        double pouzdanost = s.getNivoPodudarnosti();
        Color boja = pouzdanost >= 85 ? OK : (pouzdanost >= 60 ? WARN : BAD);

        JPanel red = new JPanel(new BorderLayout(10, 0));
        red.setBackground(Color.WHITE);
        red.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, boja),
                new EmptyBorder(6, 8, 6, 4)));
        red.setAlignmentX(Component.LEFT_ALIGNMENT);
        red.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel lblNaziv = new JLabel(humanizujNaziv(naziv));
        lblNaziv.setPreferredSize(new Dimension(230, 24));
        lblNaziv.setFont(new Font("SansSerif", Font.PLAIN, 12));

        String pocetnaVrednost = s.getKorigovanaVrednost() != null && !s.getKorigovanaVrednost().isEmpty()
                ? s.getKorigovanaVrednost() : s.getOcrVrednost();
        JTextField txt = new JTextField(pocetnaVrednost != null ? pocetnaVrednost : "");
        txt.setPreferredSize(new Dimension(200, 26));

        JLabel lblBadge = new JLabel(String.format("%.0f%%", pouzdanost), SwingConstants.CENTER);
        lblBadge.setPreferredSize(new Dimension(52, 24));
        lblBadge.setForeground(boja);
        lblBadge.setFont(new Font("SansSerif", Font.BOLD, 11));

        red.add(lblNaziv, BorderLayout.WEST);
        red.add(txt, BorderLayout.CENTER);
        red.add(lblBadge, BorderLayout.EAST);

        poljaZaKorekciju.put(s.getIdStavke(), txt);
        return red;
    }

    private static String humanizujNaziv(String naziv) {
        if (naziv == null || naziv.isEmpty()) return "";
        String[] delovi = naziv.split("_");
        StringBuilder sb = new StringBuilder();
        for (String d : delovi) {
            if (d.isEmpty()) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(d.charAt(0))).append(d.substring(1));
        }
        return sb.toString();
    }

    public Map<Integer, JTextField> getPoljaZaKorekciju() {
        return poljaZaKorekciju;
    }

    // ── Prikaz slike (koristi ista kartica 1 i kartica 2) ───────────────────

    public void prikaziSliku(BufferedImage slika, JLabel meta) {
        if (slika == null) {
            meta.setIcon(null);
            meta.setText("Nema slike za prikaz");
            return;
        }
        int maxSirina = 390;
        int w = slika.getWidth();
        int h = slika.getHeight();
        double razmera = w > maxSirina ? maxSirina / (double) w : 1.0;
        int nw = Math.max(1, (int) (w * razmera));
        int nh = Math.max(1, (int) (h * razmera));
        Image skalirana = slika.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        meta.setText(null);
        meta.setIcon(new ImageIcon(skalirana));
    }

    public JLabel getLblSlikaLista() { return lblSlikaLista; }
    public JLabel getLblSlikaPregled() { return lblSlikaPregled; }
    public JLabel getLblStranaLista() { return lblStranaLista; }
    public JLabel getLblStranaPregled() { return lblStranaPregled; }
    public JLabel getLblSazetakStavki() { return lblSazetakStavki; }
    public JLabel getLblSazetakPregled() { return lblSazetakPregled; }
    public JButton getBtnPregledajStavke() { return btnPregledajStavke; }
    public JButton getBtnPrethodnaStranaLista() { return btnPrethodnaStranaLista; }
    public JButton getBtnSledecaStranaLista() { return btnSledecaStranaLista; }
    public JButton getBtnPrethodnaStranaPregled() { return btnPrethodnaStranaPregled; }
    public JButton getBtnSledecaStranaPregled() { return btnSledecaStranaPregled; }

    // ── Zajednički izgled ─────────────────────────────────────────────────────

    private JButton dugme(String tekst, Color boja) {
        JButton btn = new JButton(tekst);
        btn.setBackground(boja);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton ikonicaDugme(String tekst) {
        JButton btn = new JButton(tekst);
        btn.setPreferredSize(new Dimension(30, 30));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(BORDER));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private TitledBorder naslovljenaIvica(String tekst) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER), " " + tekst + " ");
        tb.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        tb.setTitleColor(ACCENT);
        return tb;
    }

    private void initCustomTableModels() {
        String[] koloneObrasci = {"ID", "Datum", "Šk. godina", "Semestar", "Status",
                "Student (indeks)", "Zaposleni", "OCR", "Putanja fajla"};
        tableModelObrasci = new DefaultTableModel(koloneObrasci, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblObrasci.setModel(tableModelObrasci);
        tblObrasci.setRowHeight(24);
        tblObrasci.setGridColor(new Color(228, 232, 238));
        tblObrasci.getTableHeader().setBackground(new Color(242, 244, 247));
        tblObrasci.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblObrasci.setSelectionBackground(new Color(214, 224, 235));
        tblObrasci.getColumnModel().getColumn(8).setMinWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setMaxWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setWidth(0);
    }

    // ── Javni API ──────────────────────────────────────────────────────────

    public JTable getTblObrasci() { return tblObrasci; }
    public DefaultTableModel getTableModelObrasci() { return tableModelObrasci; }
    public JComboBox<domen.Student> getCmbStudent() { return cmbStudent; }
    public JComboBox<domen.ZaposleniFakulteta> getCmbZaposleni() { return cmbZaposleni; }
    public JSpinner getSpnSkolskaGodina() { return spnSkolskaGodina; }
    public JSpinner getSpnSemestar() { return spnSemestar; }
    public JComboBox<domen.Status> getCmbStatus() { return cmbStatus; }
    public JTextField getTxtPutanjaFajla() { return txtPutanjaFajla; }
    public JTextField getTxtPretraga() { return txtPretraga; }
    public JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }
    public domen.SV20Obrazac getSelektovaniObrazac() { return selektovaniObrazac; }
    public void setSelektovaniObrazac(domen.SV20Obrazac o) { this.selektovaniObrazac = o; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
    public void addOdaberiFajlListener(java.awt.event.ActionListener l) { btnOdaberiFajl.addActionListener(l); }
    public void addPokreniOcrListener(java.awt.event.ActionListener l) { btnPokreniOcr.addActionListener(l); }
    public void addPregledajStavkeListener(java.awt.event.ActionListener l) { btnPregledajStavke.addActionListener(l); }
    public void addNazadListener(java.awt.event.ActionListener l) { btnNazad.addActionListener(l); }
    public void addSacuvajStavkeListener(java.awt.event.ActionListener l) { btnSacuvajStavke.addActionListener(l); }
    public void addPonoviOcrListener(java.awt.event.ActionListener l) { btnPonoviOcr.addActionListener(l); }
    public void addPrethodnaStranaListaListener(java.awt.event.ActionListener l) { btnPrethodnaStranaLista.addActionListener(l); }
    public void addSledecaStranaListaListener(java.awt.event.ActionListener l) { btnSledecaStranaLista.addActionListener(l); }
    public void addPrethodnaStranaPregledListener(java.awt.event.ActionListener l) { btnPrethodnaStranaPregled.addActionListener(l); }
    public void addSledecaStranaPregledListener(java.awt.event.ActionListener l) { btnSledecaStranaPregled.addActionListener(l); }

    public void addTabelaObrasciSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblObrasci.getSelectionModel().addListSelectionListener(l);
    }
}
