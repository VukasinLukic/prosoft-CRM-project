package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class SV20ObrazacForma extends JDialog {

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
    private JScrollPane jScrollPane2;
    private JTable tblStavke;
    private JButton btnOdaberiFajl;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;
    private JButton btnOcisti;
    private JButton btnPretrazi;
    private JButton btnPokreniOcr;

    private DefaultTableModel tableModelObrasci;
    private DefaultTableModel tableModelStavke;
    private domen.SV20Obrazac selektovaniObrazac;

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

        // ── Components ────────────────────────────────────────────────────────
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

        tblStavke = new JTable();
        jScrollPane2 = new JScrollPane(tblStavke);

        btnOdaberiFajl = makeButton("Odaberi fajl...", new Color(97, 97, 97));
        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));
        btnPretrazi = makeButton("Pretraži", new Color(21, 101, 192));

        btnPokreniOcr = new JButton("  Pokreni OCR analizu  ");
        btnPokreniOcr.setBackground(new Color(230, 81, 0));
        btnPokreniOcr.setForeground(Color.WHITE);
        btnPokreniOcr.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnPokreniOcr.setFocusPainted(false);
        btnPokreniOcr.setPreferredSize(new Dimension(190, 36));
        btnPokreniOcr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPokreniOcr.setToolTipText("Pokreće OCR analizu na odabranom fajlu i upisuje stavke u bazu");

        // ── Search panel ──────────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Pretraga obrazaca "));
        searchPanel.add(new JLabel("Kriterijum:"));
        searchPanel.add(cmbKriterijum);
        txtPretraga.setPreferredSize(new Dimension(350, 28));
        searchPanel.add(txtPretraga);
        searchPanel.add(btnPretrazi);

        // ── Form panel ────────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o obrascu "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        // Row 0: Student + Zaposleni
        g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Student:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        cmbStudent.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbStudent, g);
        g.weightx = 0; g.fill = GridBagConstraints.NONE;
        g.gridx = 2;
        formPanel.add(new JLabel("Zaposleni:"), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        cmbZaposleni.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbZaposleni, g);
        g.weightx = 0;

        // Row 1: Skolska godina + Semestar + Status
        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Školska godina:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        spnSkolskaGodina.setPreferredSize(new Dimension(100, 28));
        formPanel.add(spnSkolskaGodina, g);
        g.gridx = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Semestar:"), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL;
        spnSemestar.setPreferredSize(new Dimension(80, 28));
        formPanel.add(spnSemestar, g);

        // Row 2: Status
        g.gridx = 0; g.gridy = 2; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("Status:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        cmbStatus.setPreferredSize(new Dimension(240, 28));
        formPanel.add(cmbStatus, g);

        // Row 3: File path
        g.gridx = 0; g.gridy = 3; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("Putanja fajla:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridwidth = 2;
        txtPutanjaFajla.setPreferredSize(new Dimension(380, 28));
        formPanel.add(txtPutanjaFajla, g);
        g.gridwidth = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        g.gridx = 3;
        formPanel.add(btnOdaberiFajl, g);

        // ── CRUD buttons panel ────────────────────────────────────────────────
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        crudPanel.add(btnOcisti);
        crudPanel.add(btnObrisi);
        crudPanel.add(btnSacuvaj);
        crudPanel.add(btnDodaj);

        JPanel formWithButtons = new JPanel(new BorderLayout(0, 4));
        formWithButtons.add(formPanel, BorderLayout.CENTER);
        formWithButtons.add(crudPanel, BorderLayout.SOUTH);

        // ── Top group ────────────────────────────────────────────────────────
        JPanel topGroup = new JPanel(new BorderLayout(0, 4));
        topGroup.setBorder(new EmptyBorder(4, 4, 0, 4));
        topGroup.add(searchPanel, BorderLayout.NORTH);
        topGroup.add(formWithButtons, BorderLayout.CENTER);

        // ── Obrasci table ─────────────────────────────────────────────────────
        JPanel obrazaciPanel = new JPanel(new BorderLayout(0, 4));
        obrazaciPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)),
                " Lista obrazaca  (dvostruki klik = uredi stavke) "));
        jScrollPane1.setPreferredSize(new Dimension(1100, 200));
        obrazaciPanel.add(jScrollPane1, BorderLayout.CENTER);

        JPanel obrazaciToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JLabel ocrHint = new JLabel("Selektuj obrazac gore, pa pokreni OCR nad njim:");
        ocrHint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ocrHint.setForeground(new Color(97, 97, 97));
        obrazaciToolbar.add(ocrHint);
        obrazaciToolbar.add(btnPokreniOcr);
        obrazaciPanel.add(obrazaciToolbar, BorderLayout.SOUTH);

        // ── Stavke table ──────────────────────────────────────────────────────
        JPanel stavkePanel = new JPanel(new BorderLayout());
        stavkePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                " Stavke odabranog obrasca (OCR polja) "));
        jScrollPane2.setPreferredSize(new Dimension(1100, 160));
        stavkePanel.add(jScrollPane2, BorderLayout.CENTER);

        // ── Center split ──────────────────────────────────────────────────────
        JSplitPane splitCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, obrazaciPanel, stavkePanel);
        splitCenter.setDividerLocation(220);
        splitCenter.setResizeWeight(0.6);
        splitCenter.setBorder(new EmptyBorder(4, 4, 4, 4));

        // ── Assemble ──────────────────────────────────────────────────────────
        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(splitCenter, BorderLayout.CENTER);

        initCustomTableModels();
        FormeUtil.otvoriPunEkran(this);
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void initCustomTableModels() {
        String[] koloneObrasci = {"ID", "Datum", "Šk. godina", "Semestar", "Status",
                "Student (indeks)", "Zaposleni", "OCR", "Putanja fajla"};
        tableModelObrasci = new DefaultTableModel(koloneObrasci, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblObrasci.setModel(tableModelObrasci);
        tblObrasci.setRowHeight(24);
        tblObrasci.setGridColor(new Color(220, 230, 245));
        tblObrasci.getTableHeader().setBackground(new Color(232, 240, 254));
        tblObrasci.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblObrasci.setSelectionBackground(new Color(187, 222, 251));
        // Hide putanja column (index 8) — stored but not prominently displayed
        tblObrasci.getColumnModel().getColumn(8).setMinWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setMaxWidth(0);
        tblObrasci.getColumnModel().getColumn(8).setWidth(0);

        String[] koloneStavke = {"ID", "Polje", "OCR Vrednost", "Korigovana", "Podudarnost", "Uspešno"};
        tableModelStavke = new DefaultTableModel(koloneStavke, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStavke.setModel(tableModelStavke);
        tblStavke.setRowHeight(24);
        tblStavke.setGridColor(new Color(220, 230, 245));
        tblStavke.getTableHeader().setBackground(new Color(232, 240, 254));
        tblStavke.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblStavke.setSelectionBackground(new Color(187, 222, 251));
    }

    // ── Public API ─────────────────────────────────────────────────────────────

    public JTable getTblObrasci() { return tblObrasci; }
    public DefaultTableModel getTableModelObrasci() { return tableModelObrasci; }
    public JTable getTblStavke() { return tblStavke; }
    public DefaultTableModel getTableModelStavke() { return tableModelStavke; }
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
    public void addTabelaObrasciSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblObrasci.getSelectionModel().addListSelectionListener(l);
    }
}
