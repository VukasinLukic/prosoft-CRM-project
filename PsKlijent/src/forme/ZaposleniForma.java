package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class ZaposleniForma extends JDialog {

    private JTextField txtIme;
    private JTextField txtPretraga;
    private JTextField txtKorisnickoIme;
    private JTextField txtEmail;
    private JTextField txtPrezime;
    private JPasswordField txtPotvrdaSifre;
    private JPasswordField txtSifra;
    private JComboBox<String> cmbKriterijum;
    private JScrollPane jScrollPane1;
    private JTable tblZaposleni;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;
    private JButton btnOcisti;
    private JButton btnPretrazi;

    private DefaultTableModel tableModel;
    private domen.ZaposleniFakulteta selektovani;

    public ZaposleniForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ZaposleniForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setTitle("Upravljanje Zaposlenima");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        txtIme = new JTextField(); txtIme.setPreferredSize(new Dimension(200, 28));
        txtPrezime = new JTextField(); txtPrezime.setPreferredSize(new Dimension(200, 28));
        txtKorisnickoIme = new JTextField(); txtKorisnickoIme.setPreferredSize(new Dimension(200, 28));
        txtEmail = new JTextField(); txtEmail.setPreferredSize(new Dimension(200, 28));
        txtSifra = new JPasswordField(); txtSifra.setPreferredSize(new Dimension(200, 28));
        txtPotvrdaSifre = new JPasswordField(); txtPotvrdaSifre.setPreferredSize(new Dimension(200, 28));
        txtPretraga = new JTextField(); txtPretraga.setPreferredSize(new Dimension(380, 28));
        cmbKriterijum = new JComboBox<>(new String[]{"Ime", "Prezime", "Korisničko ime", "Email"});
        tblZaposleni = new JTable();
        jScrollPane1 = new JScrollPane(tblZaposleni);
        jScrollPane1.setPreferredSize(new Dimension(880, 280));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));
        btnPretrazi = makeButton("Pretraži", new Color(21, 101, 192));

        // ── Search panel ─────────────────────────────────────────────────────
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
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o zaposlenom "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        addLabeledRow(formPanel, g, 0, "Ime:", txtIme, "Prezime:", txtPrezime);
        addLabeledRow(formPanel, g, 2, "Korisničko ime:", txtKorisnickoIme, "Email:", txtEmail);
        addLabeledRow(formPanel, g, 4, "Lozinka:", txtSifra, "Potvrda lozinke:", txtPotvrdaSifre);

        // ── Table section ─────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista zaposlenih "));
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
        topGroup.add(searchPanel, BorderLayout.NORTH);
        topGroup.add(formPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        initCustomTableModel();
        FormeUtil.otvoriPunEkran(this);
    }

    private void addLabeledRow(JPanel p, GridBagConstraints g, int row,
            String l1, JComponent f1, String l2, JComponent f2) {
        g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.gridx = 0; g.gridy = row; p.add(new JLabel(l1), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; p.add(f1, g);
        g.fill = GridBagConstraints.NONE;
        g.gridx = 2; p.add(new JLabel(l2), g);
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
        String[] kolone = {"ID", "Ime", "Prezime", "Korisničko ime", "Email"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblZaposleni.setModel(tableModel);
        tblZaposleni.setRowHeight(24);
        tblZaposleni.setGridColor(new Color(220, 230, 245));
        tblZaposleni.getTableHeader().setBackground(new Color(232, 240, 254));
        tblZaposleni.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblZaposleni.setSelectionBackground(new Color(187, 222, 251));
    }

    public JTable getTblZaposleni() { return tblZaposleni; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtIme() { return txtIme; }
    public JTextField getTxtPrezime() { return txtPrezime; }
    public JTextField getTxtKorisnickoIme() { return txtKorisnickoIme; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JPasswordField getTxtSifra() { return txtSifra; }
    public JPasswordField getTxtPotvrdaSifre() { return txtPotvrdaSifre; }
    public JTextField getTxtPretraga() { return txtPretraga; }
    public JComboBox<String> getCmbKriterijum() { return cmbKriterijum; }
    public domen.ZaposleniFakulteta getSelektovani() { return selektovani; }
    public void setSelektovani(domen.ZaposleniFakulteta z) { this.selektovani = z; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addPretraziListener(java.awt.event.ActionListener l) { btnPretrazi.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblZaposleni.getSelectionModel().addListSelectionListener(l);
    }
}
