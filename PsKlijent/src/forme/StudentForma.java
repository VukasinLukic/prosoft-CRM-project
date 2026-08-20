package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class StudentForma extends JDialog {

    private JTextField txtJmbg;
    private JTextField txtIndeks;
    private JTextField txtPrezime;
    private JTextField txtIme;
    private JTextField txtMestoRodjenja;
    private JTextField txtAdresaStanovanja;
    private JTextField txtPretraga;
    private JComboBox cmbStudijskiProgram;
    private JComboBox cmbKriterijum;
    private JScrollPane jScrollPane1;
    private JTable tblStudenti;
    private JButton btnSacuvaj;
    private JButton btnDodaj;
    private JButton btnObrisi;
    private JButton btnOcisti;
    private JButton btnPretrazi;

    private DefaultTableModel tableModel;
    private domen.Student selektovani;

    public StudentForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public StudentForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setTitle("Upravljanje Studentima");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Components
        txtIndeks = new JTextField(); txtIndeks.setPreferredSize(new Dimension(130, 28));
        txtJmbg = new JTextField(); txtJmbg.setPreferredSize(new Dimension(180, 28));
        txtIme = new JTextField(); txtIme.setPreferredSize(new Dimension(200, 28));
        txtPrezime = new JTextField(); txtPrezime.setPreferredSize(new Dimension(200, 28));
        txtMestoRodjenja = new JTextField(); txtMestoRodjenja.setPreferredSize(new Dimension(200, 28));
        txtAdresaStanovanja = new JTextField(); txtAdresaStanovanja.setPreferredSize(new Dimension(280, 28));
        txtPretraga = new JTextField(); txtPretraga.setPreferredSize(new Dimension(400, 28));
        cmbStudijskiProgram = new JComboBox();
        cmbKriterijum = new JComboBox(new String[]{"Indeks", "Ime", "Prezime", "JMBG"});
        tblStudenti = new JTable();
        jScrollPane1 = new JScrollPane(tblStudenti);
        jScrollPane1.setPreferredSize(new Dimension(900, 300));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));
        btnPretrazi = makeButton("Pretraži", new Color(21, 101, 192));

        // ── Search panel ────────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Pretraga "));
        searchPanel.add(new JLabel("Kriterijum:"));
        searchPanel.add(cmbKriterijum);
        searchPanel.add(txtPretraga);
        searchPanel.add(btnPretrazi);

        // ── Form panel ───────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o studentu "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        addRow(formPanel, g, 0, 0, "Indeks:", txtIndeks, "JMBG:", txtJmbg);
        addRow(formPanel, g, 0, 2, "Ime:", txtIme, "Prezime:", txtPrezime);
        addRow(formPanel, g, 0, 4, "Mesto rodjenja:", txtMestoRodjenja, "Adresa stanovanja:", txtAdresaStanovanja);

        g.gridx = 0; g.gridy = 6; g.gridwidth = 1;
        formPanel.add(new JLabel("Studijski program:"), g);
        g.gridx = 1; g.gridwidth = 3; g.fill = GridBagConstraints.HORIZONTAL;
        cmbStudijskiProgram.setPreferredSize(new Dimension(400, 28));
        formPanel.add(cmbStudijskiProgram, g);

        // ── Table section ────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout(0, 4));
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista studenata "));
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        // ── Button panel ─────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 225, 245)));
        btnPanel.add(btnOcisti);
        btnPanel.add(btnObrisi);
        btnPanel.add(btnSacuvaj);
        btnPanel.add(btnDodaj);

        // ── Top group ────────────────────────────────────────────────────────
        JPanel topGroup = new JPanel(new BorderLayout(0, 4));
        topGroup.setBorder(new EmptyBorder(4, 4, 4, 4));
        topGroup.add(searchPanel, BorderLayout.NORTH);
        topGroup.add(formPanel, BorderLayout.CENTER);

        // ── Main layout ──────────────────────────────────────────────────────
        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        initCustomTableModel();
        FormeUtil.otvoriPunEkran(this);
    }

    private void addRow(JPanel panel, GridBagConstraints g, int startX, int startY,
            String lbl1, JComponent f1, String lbl2, JComponent f2) {
        g.fill = GridBagConstraints.NONE; g.gridwidth = 1;
        g.gridx = startX; g.gridy = startY;
        panel.add(new JLabel(lbl1), g);
        g.gridx = startX + 1; g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(f1, g);
        g.fill = GridBagConstraints.NONE;
        g.gridx = startX + 2;
        panel.add(new JLabel(lbl2), g);
        g.gridx = startX + 3; g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(f2, g);
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
        String[] kolone = {"Indeks", "JMBG", "Ime", "Prezime", "Mesto rodjenja", "Adresa", "Studijski program"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStudenti.setModel(tableModel);
        tblStudenti.setRowHeight(24);
        tblStudenti.setGridColor(new Color(220, 230, 245));
        tblStudenti.getTableHeader().setBackground(new Color(232, 240, 254));
        tblStudenti.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblStudenti.setSelectionBackground(new Color(187, 222, 251));
    }

    public JTable getTblStudenti() { return tblStudenti; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtIndeks() { return txtIndeks; }
    public JTextField getTxtJmbg() { return txtJmbg; }
    public JTextField getTxtIme() { return txtIme; }
    public JTextField getTxtPrezime() { return txtPrezime; }
    public JTextField getTxtMestoRodjenja() { return txtMestoRodjenja; }
    public JTextField getTxtAdresaStanovanja() { return txtAdresaStanovanja; }
    public JComboBox<domen.StudijskiProgram> getCmbStudijskiProgram() { return cmbStudijskiProgram; }
    public JTextField getTxtPretraga() { return txtPretraga; }
    public JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }
    public domen.Student getSelektovani() { return selektovani; }
    public void setSelektovani(domen.Student s) { this.selektovani = s; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblStudenti.getSelectionModel().addListSelectionListener(l);
    }
}
