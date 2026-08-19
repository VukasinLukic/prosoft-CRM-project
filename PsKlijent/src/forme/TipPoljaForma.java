package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class TipPoljaForma extends JDialog {

    private JTextField txtNazivPolja;
    private JTextField txtRegexValidacija;
    private JTextField txtVisina;
    private JTextField txtSirina;
    private JTextField txtPozicijaX;
    private JTextField txtPozicijaY;
    private JComboBox cmbTipPodatka;
    private JSpinner spnStranica;
    private JSpinner spnRedosledObrade;
    private JCheckBox chkPodrzavaOCR;
    private JCheckBox chkObaveznoPolje;
    private JScrollPane jScrollPane1;
    private JTable tblTipoviPolja;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;
    private JButton btnOcisti;

    private DefaultTableModel tableModel;
    private domen.TipPolja selektovani;

    public TipPoljaForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public TipPoljaForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setTitle("Upravljanje Tipovima Polja");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtNazivPolja = new JTextField(); txtNazivPolja.setPreferredSize(new Dimension(200, 28));
        txtRegexValidacija = new JTextField(); txtRegexValidacija.setPreferredSize(new Dimension(200, 28));
        txtVisina = new JTextField(); txtVisina.setPreferredSize(new Dimension(90, 28));
        txtSirina = new JTextField(); txtSirina.setPreferredSize(new Dimension(90, 28));
        txtPozicijaX = new JTextField(); txtPozicijaX.setPreferredSize(new Dimension(90, 28));
        txtPozicijaY = new JTextField(); txtPozicijaY.setPreferredSize(new Dimension(90, 28));
        cmbTipPodatka = new JComboBox();
        cmbTipPodatka.setPreferredSize(new Dimension(200, 28));
        spnStranica = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spnStranica.setPreferredSize(new Dimension(80, 28));
        spnRedosledObrade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnRedosledObrade.setPreferredSize(new Dimension(80, 28));
        chkPodrzavaOCR = new JCheckBox("Podržava OCR");
        chkObaveznoPolje = new JCheckBox("Obavezno polje");
        tblTipoviPolja = new JTable();
        jScrollPane1 = new JScrollPane(tblTipoviPolja);
        jScrollPane1.setPreferredSize(new Dimension(920, 280));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));

        // ── Form panel ───────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o tipu polja "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        // Row 0: naziv + tip podatka
        addRow2(formPanel, g, 0, "Naziv polja:", txtNazivPolja, "Tip podatka:", cmbTipPodatka);
        // Row 2: regex + pozicija X
        addRow2(formPanel, g, 2, "Regex validacija:", txtRegexValidacija, "Pozicija X:", txtPozicijaX);
        // Row 4: pozicija Y + širina
        addRow2(formPanel, g, 4, "Pozicija Y:", txtPozicijaY, "Širina:", txtSirina);
        // Row 6: visina + stranica
        addRow2(formPanel, g, 6, "Visina:", txtVisina, "Stranica:", spnStranica);
        // Row 8: redosled + checkboxes
        g.gridx = 0; g.gridy = 8; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Redosled obrade:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(spnRedosledObrade, g);
        g.fill = GridBagConstraints.NONE;
        g.gridx = 2;
        formPanel.add(chkPodrzavaOCR, g);
        g.gridx = 3;
        formPanel.add(chkObaveznoPolje, g);

        // ── Table section ─────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista tipova polja "));
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 225, 245)));
        btnPanel.add(btnOcisti);
        btnPanel.add(btnObrisi);
        btnPanel.add(btnSacuvaj);
        btnPanel.add(btnDodaj);

        JPanel topGroup = new JPanel(new BorderLayout(0, 4));
        topGroup.setBorder(new EmptyBorder(4, 4, 4, 4));
        topGroup.add(formPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        initCustomTableModel();
        setSize(1020, 680);
        setLocationRelativeTo(null);
    }

    private void addRow2(JPanel p, GridBagConstraints g, int row,
            String l1, JComponent f1, String l2, JComponent f2) {
        g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.gridx = 0; g.gridy = row; p.add(new JLabel(l1), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; p.add(f1, g);
        g.fill = GridBagConstraints.NONE;
        g.gridx = 2; g.gridy = row; p.add(new JLabel(l2), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL; p.add(f2, g);
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void initCustomTableModel() {
        String[] kolone = {"ID", "Naziv", "Tip", "Stranica", "Redosled", "OCR", "Obavezno"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTipoviPolja.setModel(tableModel);
        tblTipoviPolja.setRowHeight(24);
        tblTipoviPolja.setGridColor(new Color(220, 230, 245));
        tblTipoviPolja.getTableHeader().setBackground(new Color(232, 240, 254));
        tblTipoviPolja.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblTipoviPolja.setSelectionBackground(new Color(187, 222, 251));
    }

    public JTable getTblTipoviPolja() { return tblTipoviPolja; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtNazivPolja() { return txtNazivPolja; }
    public JComboBox<domen.tipPodatka> getCmbTipPodatka() { return cmbTipPodatka; }
    public JTextField getTxtRegexValidacija() { return txtRegexValidacija; }
    public JTextField getTxtPozicijaX() { return txtPozicijaX; }
    public JTextField getTxtPozicijaY() { return txtPozicijaY; }
    public JTextField getTxtSirina() { return txtSirina; }
    public JTextField getTxtVisina() { return txtVisina; }
    public JSpinner getSpnStranica() { return spnStranica; }
    public JSpinner getSpnRedosledObrade() { return spnRedosledObrade; }
    public JCheckBox getChkPodrzavaOCR() { return chkPodrzavaOCR; }
    public JCheckBox getChkObaveznoPolje() { return chkObaveznoPolje; }
    public domen.TipPolja getSelektovani() { return selektovani; }
    public void setSelektovani(domen.TipPolja tp) { this.selektovani = tp; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblTipoviPolja.getSelectionModel().addListSelectionListener(l);
    }
}
