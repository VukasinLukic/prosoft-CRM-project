package forme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SV20ObrazacForma extends JPanel {

    // ── Kartica "Lista/unos" — čisto upravljanje osnovnim podacima obrasca ──
    private JComboBox cmbStudent;
    private JComboBox cmbStatus;
    private JComboBox cmbKriterijum;
    private JSpinner spnSkolskaGodina;
    private JComboBox<Integer> cmbSemestar;
    private JTextField txtPretraga;
    private JScrollPane jScrollPane1;
    private JTable tblObrasci;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;
    private JButton btnOcisti;
    private JButton btnPretrazi;

    // ── Kartica "OCR pregled" — sken + OCR + korekcija za JEDAN obrazac ──────
    private JLabel lblSlikaPregled;
    private JLabel lblStranaPregled;
    private JButton btnPrethodnaStranaPregled;
    private JButton btnSledecaStranaPregled;
    private JButton btnDodajSliku;
    private JButton btnPokreniOcr;
    private JLabel lblSazetakPregled;
    private JPanel pnlStavke;
    private JPanel poljaPanel;
    private JButton btnNazad;
    private JButton btnSacuvajStavke;

    private CardLayout slikaCardLayout;
    private JPanel slikaCardsPanel;
    private static final String SLIKA_PRAZNO = "PRAZNO";
    private static final String SLIKA_UCITANA = "UCITANA";

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

    public SV20ObrazacForma() {
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.add(izgradiKarticuListe(), KARTICA_LISTA);
        cardsPanel.add(izgradiKarticuPregleda(), KARTICA_PREGLED);

        setLayout(new BorderLayout());
        add(cardsPanel, BorderLayout.CENTER);

        initCustomTableModels();
    }

    public void prikaziKarticu(String naziv) {
        cardLayout.show(cardsPanel, naziv);
    }

    // ── Kartica 1: Lista/unos ────────────────────────────────────────────────

    private JPanel izgradiKarticuListe() {
        cmbStudent = new JComboBox();
        cmbStudent.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list,
                        value == null ? "— Izaberite studenta —" : value, index, isSelected, cellHasFocus);
                if (value == null) { c.setForeground(TEXT_MUTED); }
                return c;
            }
        });
        cmbStatus = new JComboBox();
        cmbKriterijum = new JComboBox(new String[]{"Indeks studenta", "Zaposleni", "Status"});
        spnSkolskaGodina = new JSpinner(new SpinnerNumberModel(
                java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), 2000, 2100, 1));
        cmbSemestar = new JComboBox<>();
        for (int i = 1; i <= 12; i++) { cmbSemestar.addItem(i); }
        txtPretraga = new JTextField();

        tblObrasci = new JTable();
        jScrollPane1 = new JScrollPane(tblObrasci);

        btnDodaj = dugme("Dodaj obrazac", new Color(38, 70, 110), true);
        btnSacuvaj = dugme("Sačuvaj izmene", OK, true);
        btnObrisi = dugme("Obriši", BAD, false);
        btnOcisti = ghostDugme("Očisti formu");
        btnPretrazi = dugme("Pretraži", new Color(38, 70, 110), false);

        // ── Podaci o obrascu — samo osnovni podaci, bez skena/OCR-a ──────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(naslovljenaIvica("Podaci o obrascu"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Student:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridwidth = 3;
        cmbStudent.setPreferredSize(new Dimension(280, 28));
        formPanel.add(cmbStudent, g);
        g.weightx = 0; g.gridwidth = 1;

        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Školska godina:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        spnSkolskaGodina.setPreferredSize(new Dimension(100, 28));
        formPanel.add(spnSkolskaGodina, g);
        g.gridx = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Semestar:"), g);
        g.gridx = 3; g.fill = GridBagConstraints.NONE;
        cmbSemestar.setPreferredSize(new Dimension(60, 28));
        formPanel.add(cmbSemestar, g);

        g.gridx = 0; g.gridy = 2; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("Status:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        cmbStatus.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbStatus, g);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        crudPanel.setBackground(Color.WHITE);
        crudPanel.add(btnDodaj);
        crudPanel.add(btnSacuvaj);
        crudPanel.add(btnObrisi);
        crudPanel.add(btnOcisti);
        // Kontekstualno: "Dodaj" kad se pravi nov zapis, "Sačuvaj/Obriši" kad je red
        // već selektovan iz tabele — sprečava da sva četiri dugmeta stoje pomešano
        // bez jasnog značenja koje se od njih zapravo primenjuje na trenutno stanje.
        prikaziRezimUnosa(true);

        JPanel formWithButtons = new JPanel(new BorderLayout(0, 4));
        formWithButtons.setBackground(Color.WHITE);
        formWithButtons.add(formPanel, BorderLayout.CENTER);
        formWithButtons.add(crudPanel, BorderLayout.SOUTH);

        // ── Pretraga — direktno iznad liste obrazaca ─────────────────────────
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Pretraži po:"));
        searchPanel.add(cmbKriterijum);
        txtPretraga.setPreferredSize(new Dimension(300, 28));
        searchPanel.add(txtPretraga);
        searchPanel.add(btnPretrazi);

        // ── Lista obrazaca ───────────────────────────────────────────────────
        JPanel obrazaciPanel = new JPanel(new BorderLayout(0, 4));
        obrazaciPanel.setBackground(Color.WHITE);
        obrazaciPanel.setBorder(naslovljenaIvica("Lista obrazaca  (najnoviji prvi · dvostruki klik = otvori obrazac)"));
        jScrollPane1.setPreferredSize(new Dimension(1100, 300));
        obrazaciPanel.add(searchPanel, BorderLayout.NORTH);
        obrazaciPanel.add(jScrollPane1, BorderLayout.CENTER);

        JPanel kartica = new JPanel(new BorderLayout(0, 10));
        kartica.setBackground(Color.WHITE);
        kartica.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel gornjiDeo = new JPanel(new BorderLayout(0, 8));
        gornjiDeo.setBackground(Color.WHITE);
        gornjiDeo.add(naslov("Upravljanje ŠV-20 obrascima"), BorderLayout.NORTH);
        gornjiDeo.add(formWithButtons, BorderLayout.CENTER);

        kartica.add(gornjiDeo, BorderLayout.NORTH);
        kartica.add(obrazaciPanel, BorderLayout.CENTER);
        return kartica;
    }

    // ── Kartica 2: OCR pregled (sken + OCR + korekcija za jedan obrazac) ─────

    private JPanel izgradiKarticuPregleda() {
        JPanel kartica = new JPanel(new BorderLayout(0, 8));
        kartica.setBackground(Color.WHITE);
        kartica.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblSazetakPregled = new JLabel(" ");
        lblSazetakPregled.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSazetakPregled.setForeground(ACCENT);
        lblSazetakPregled.setBorder(new EmptyBorder(2, 4, 6, 4));

        // ── Slika (levo) — prazno stanje ILI učitana slika ──────────────────
        JPanel slikaPanel = new JPanel(new BorderLayout());
        slikaPanel.setBackground(Color.WHITE);
        slikaPanel.setBorder(naslovljenaIvica("Skenirani obrazac"));

        slikaCardLayout = new CardLayout();
        slikaCardsPanel = new JPanel(slikaCardLayout);
        slikaCardsPanel.setBackground(Color.WHITE);
        slikaCardsPanel.add(izgradiPraznoStanjeSlike(), SLIKA_PRAZNO);
        slikaCardsPanel.add(izgradiUcitanoStanjeSlike(), SLIKA_UCITANA);
        slikaPanel.add(slikaCardsPanel, BorderLayout.CENTER);

        // ── Polja (desno) — samo polja trenutno prikazane strane ────────────
        poljaPanel = new JPanel(new BorderLayout());
        poljaPanel.setBackground(Color.WHITE);
        poljaPanel.setBorder(naslovljenaIvica("Prepoznata polja — strana 1"));

        pnlStavke = new JPanel();
        pnlStavke.setBackground(Color.WHITE);
        pnlStavke.setLayout(new BoxLayout(pnlStavke, BoxLayout.Y_AXIS));
        JScrollPane stavkeScroll = new JScrollPane(pnlStavke);
        stavkeScroll.setBorder(null);
        stavkeScroll.getVerticalScrollBar().setUnitIncrement(16);
        poljaPanel.add(stavkeScroll, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, slikaPanel, poljaPanel);
        split.setResizeWeight(0.5);
        split.setDividerLocation(0.5);
        split.setBorder(null);
        split.setContinuousLayout(true);

        // ── Dugmad ───────────────────────────────────────────────────────────
        btnNazad = ghostDugme("‹ Nazad na obrazac");
        btnSacuvajStavke = dugme("Sačuvaj stavke", OK, true);

        JPanel akcijePanel = new JPanel(new BorderLayout());
        akcijePanel.setBackground(Color.WHITE);
        akcijePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(10, 0, 0, 0)));
        akcijePanel.add(btnNazad, BorderLayout.WEST);
        akcijePanel.add(btnSacuvajStavke, BorderLayout.EAST);

        kartica.add(lblSazetakPregled, BorderLayout.NORTH);
        kartica.add(split, BorderLayout.CENTER);
        kartica.add(akcijePanel, BorderLayout.SOUTH);
        return kartica;
    }

    private JPanel izgradiPraznoStanjeSlike() {
        JPanel prazno = new JPanel();
        prazno.setBackground(Color.WHITE);
        prazno.setLayout(new BoxLayout(prazno, BoxLayout.Y_AXIS));
        prazno.setBorder(new EmptyBorder(60, 20, 60, 20));

        JLabel poruka = new JLabel("Nema priložene slike — dodajte sken da pokrenete OCR", SwingConstants.CENTER);
        poruka.setForeground(TEXT_MUTED);
        poruka.setFont(new Font("SansSerif", Font.PLAIN, 13));
        poruka.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnDodajSliku = dugme("Dodaj sliku", new Color(38, 70, 110), true);
        btnDodajSliku.setAlignmentX(Component.CENTER_ALIGNMENT);

        prazno.add(poruka);
        prazno.add(Box.createVerticalStrut(16));
        prazno.add(btnDodajSliku);
        return prazno;
    }

    private JPanel izgradiUcitanoStanjeSlike() {
        JPanel ucitano = new JPanel(new BorderLayout(0, 6));
        ucitano.setBackground(Color.WHITE);

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

        btnPokreniOcr = dugme("Obradi OCR", new Color(180, 90, 20), true);
        btnPokreniOcr.setToolTipText("Šalje ceo fajl (obe strane) OCR servisu i popunjava polja desno");
        JPanel ocrDugmePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        ocrDugmePanel.setBackground(Color.WHITE);
        ocrDugmePanel.add(btnPokreniOcr);

        JPanel donjiDeo = new JPanel(new BorderLayout());
        donjiDeo.setBackground(Color.WHITE);
        donjiDeo.add(flipPanel, BorderLayout.NORTH);
        donjiDeo.add(ocrDugmePanel, BorderLayout.SOUTH);

        ucitano.add(slikaScroll, BorderLayout.CENTER);
        ucitano.add(donjiDeo, BorderLayout.SOUTH);
        return ucitano;
    }

    /** Prebacuje levi panel između "nema slike" i "slika + OCR dugme" stanja. */
    public void prikaziStanjeSlike(boolean postojiSlika) {
        slikaCardLayout.show(slikaCardsPanel, postojiSlika ? SLIKA_UCITANA : SLIKA_PRAZNO);
    }

    /** "Obradi OCR" pre prvog pokretanja, "Ponovi OCR" posle — isto dugme, ista akcija. */
    public void postaviTekstDugmetaOcr(boolean vecObradjeno) {
        btnPokreniOcr.setText(vecObradjeno ? "Ponovi OCR" : "Obradi OCR");
    }

    /** Prikazuje SAMO polja trenutno izabrane strane (lista je već filtrirana od strane kontrolera). */
    public void prikaziStavke(List<domen.StavkeObrasca> stavkeZaStranu, int strana) {
        poljaPanel.setBorder(naslovljenaIvica("Prepoznata polja — strana " + strana));
        pnlStavke.removeAll();
        poljaZaKorekciju.clear();

        if (stavkeZaStranu.isEmpty()) {
            JLabel prazno = new JLabel("Pokreni OCR da bi se prepoznala polja obrasca.");
            prazno.setForeground(TEXT_MUTED);
            prazno.setBorder(new EmptyBorder(20, 8, 8, 8));
            prazno.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnlStavke.add(prazno);
        } else {
            for (domen.StavkeObrasca s : stavkeZaStranu) {
                pnlStavke.add(redPolja(s));
            }
        }
        pnlStavke.add(Box.createVerticalGlue());
        pnlStavke.revalidate();
        pnlStavke.repaint();
    }

    private JComponent redPolja(domen.StavkeObrasca s) {
        String naziv = s.getIdPolja() != null ? s.getIdPolja().getNazivPolja() : "?";
        double pouzdanost = s.getNivoPodudarnosti();
        Color boja = pouzdanost >= 85 ? OK : (pouzdanost >= 60 ? WARN : BAD);
        String ocrOriginal = s.getOcrVrednost() != null ? s.getOcrVrednost().trim() : "";

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
        // Čim korisnik ručno promeni vrednost, procenat pouzdanosti OCR-a više nije relevantan
        // za TO polje (vrednost je sad ljudski potvrđena, ne OCR pogodak) — sakrij samo tu značku.
        txt.getDocument().addDocumentListener(new DocumentListener() {
            private void osveziBadge() {
                boolean izmenjeno = !txt.getText().trim().equals(ocrOriginal);
                lblBadge.setVisible(!izmenjeno);
            }
            @Override public void insertUpdate(DocumentEvent e) { osveziBadge(); }
            @Override public void removeUpdate(DocumentEvent e) { osveziBadge(); }
            @Override public void changedUpdate(DocumentEvent e) { osveziBadge(); }
        });

        red.add(lblNaziv, BorderLayout.WEST);
        red.add(txt, BorderLayout.CENTER);
        red.add(lblBadge, BorderLayout.EAST);

        // Ključ je ID tipa polja, ne ID stavke — polja koja korisnik tek popunjava ručno
        // (bez OCR-a) još nemaju sačuvanu stavku u bazi (idStavke=0), pa idStavke ne bi bio
        // jedinstven ključ; ID tipa polja postoji čak i za polja koja se tek prvi put unose.
        poljaZaKorekciju.put(s.getIdPolja().getIdPolja(), txt);
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

    // ── Prikaz slike ─────────────────────────────────────────────────────────

    public void prikaziSliku(BufferedImage slika, JLabel meta) {
        if (slika == null) {
            meta.setIcon(null);
            meta.setText("Nema slike za prikaz");
            return;
        }
        // Iskoristi stvarnu širinu okvira u kom slika sedi (u OCR pregledu je to ~50% ekrana) —
        // bez ovoga slika ostaje mala čak i kad joj je split dao mnogo više prostora.
        int maxSirina = 390;
        Container roditelj = meta.getParent();
        if (roditelj != null && roditelj.getWidth() > 80) {
            maxSirina = roditelj.getWidth() - 16;
        }
        int w = slika.getWidth();
        int h = slika.getHeight();
        double razmera = w > maxSirina ? maxSirina / (double) w : 1.0;
        int nw = Math.max(1, (int) (w * razmera));
        int nh = Math.max(1, (int) (h * razmera));
        Image skalirana = slika.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        meta.setText(null);
        meta.setIcon(new ImageIcon(skalirana));
    }

    public JLabel getLblSlikaPregled() { return lblSlikaPregled; }
    public JLabel getLblStranaPregled() { return lblStranaPregled; }
    public JLabel getLblSazetakPregled() { return lblSazetakPregled; }
    public JButton getBtnPrethodnaStranaPregled() { return btnPrethodnaStranaPregled; }
    public JButton getBtnSledecaStranaPregled() { return btnSledecaStranaPregled; }

    /** true = ništa nije selektovano (prikaži "Dodaj obrazac"); false = red je selektovan
     *  (prikaži "Sačuvaj izmene" + "Obriši"). "Očisti formu" je uvek vidljivo. */
    public void prikaziRezimUnosa(boolean noviZapis) {
        btnDodaj.setVisible(noviZapis);
        btnSacuvaj.setVisible(!noviZapis);
        btnObrisi.setVisible(!noviZapis);
    }

    // ── Zajednički izgled ─────────────────────────────────────────────────────

    private JLabel naslov(String tekst) {
        JLabel naslov = new JLabel(tekst);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 18));
        naslov.setForeground(ACCENT);
        naslov.setBorder(new EmptyBorder(2, 2, 4, 2));
        return naslov;
    }

    private JButton dugme(String tekst, Color boja, boolean istaknuto) {
        JButton btn = new JButton(tekst);
        btn.setBackground(boja);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", istaknuto ? Font.BOLD : Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 16, 9, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton ghostDugme(String tekst) {
        JButton btn = new JButton(tekst);
        btn.setForeground(ACCENT);
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 14, 8, 14)));
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
        // Bojene značke za Status/OCR MORAJU biti postavljene PRE stilizujTabelu poziva —
        // taj helper ne pregazi kolonu koja već ima svoj renderer.
        tblObrasci.getColumnModel().getColumn(4).setCellRenderer(new StatusBadgeRenderer());
        tblObrasci.getColumnModel().getColumn(7).setCellRenderer(new OcrBadgeRenderer());
        FormeUtil.stilizujTabelu(tblObrasci, 46, 90, 70, 70, 130, 210, 150, 60, 0);
        tblObrasci.getColumnModel().getColumn(8).setMinWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setMaxWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setWidth(0);
    }

    /** Boji status obrasca kao značku umesto golog teksta — lakše se uočava na prvi pogled. */
    private static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
            Color bg = new Color(230, 230, 230);
            Color fg = new Color(80, 80, 80);
            if (value instanceof domen.Status) {
                switch ((domen.Status) value) {
                    case PODNET: bg = new Color(227, 237, 251); fg = new Color(26, 77, 143); break;
                    case U_OBRADI: bg = new Color(255, 244, 224); fg = new Color(138, 83, 0); break;
                    case VRACEN_NA_KOREKCIJU: bg = new Color(253, 236, 234); fg = BAD; break;
                    case ODOBREN: bg = new Color(232, 245, 233); fg = OK; break;
                    case ODBIJEN: bg = new Color(253, 236, 234); fg = BAD; break;
                }
            }
            lbl.setBackground(isSelected ? table.getSelectionBackground() : bg);
            lbl.setForeground(fg);
            return lbl;
        }
    }

    /** Da/Ne za OCR status, kao mala značka. */
    private static class OcrBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setOpaque(true);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
            boolean da = "Da".equals(value);
            lbl.setBackground(isSelected ? table.getSelectionBackground()
                    : (da ? new Color(232, 245, 233) : new Color(238, 240, 243)));
            lbl.setForeground(da ? OK : TEXT_MUTED);
            return lbl;
        }
    }

    // ── Javni API ──────────────────────────────────────────────────────────

    public JTable getTblObrasci() { return tblObrasci; }
    public DefaultTableModel getTableModelObrasci() { return tableModelObrasci; }
    public JComboBox<domen.Student> getCmbStudent() { return cmbStudent; }
    public JSpinner getSpnSkolskaGodina() { return spnSkolskaGodina; }
    public JComboBox<Integer> getCmbSemestar() { return cmbSemestar; }
    public JComboBox<domen.Status> getCmbStatus() { return cmbStatus; }
    public JTextField getTxtPretraga() { return txtPretraga; }
    public JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }
    public domen.SV20Obrazac getSelektovaniObrazac() { return selektovaniObrazac; }
    public void setSelektovaniObrazac(domen.SV20Obrazac o) { this.selektovaniObrazac = o; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
    public void addDodajSlikuListener(java.awt.event.ActionListener l) { btnDodajSliku.addActionListener(l); }
    public void addPokreniOcrListener(java.awt.event.ActionListener l) { btnPokreniOcr.addActionListener(l); }
    public void addNazadListener(java.awt.event.ActionListener l) { btnNazad.addActionListener(l); }
    public void addSacuvajStavkeListener(java.awt.event.ActionListener l) { btnSacuvajStavke.addActionListener(l); }
    public void addPrethodnaStranaPregledListener(java.awt.event.ActionListener l) { btnPrethodnaStranaPregled.addActionListener(l); }
    public void addSledecaStranaPregledListener(java.awt.event.ActionListener l) { btnSledecaStranaPregled.addActionListener(l); }

    public void addTabelaObrasciSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblObrasci.getSelectionModel().addListSelectionListener(l);
    }
}
