package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class TerminDezurstvaForma extends JDialog {

    private JComboBox cmbTipTermina;
    private JTextField txtKancelarija;
    private JScrollPane jScrollPane1;
    private JTable tblTermini;
    private JButton btnOcisti;
    private JButton btnDodaj;
    private JButton btnSacuvaj;
    private JButton btnObrisi;

    private DefaultTableModel tableModel;
    private domen.TerminDezurstva selektovani;

    public TerminDezurstvaForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public TerminDezurstvaForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setTitle("Upravljanje Terminima Dežurstva");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cmbTipTermina = new JComboBox();
        cmbTipTermina.setPreferredSize(new Dimension(250, 28));
        txtKancelarija = new JTextField();
        txtKancelarija.setPreferredSize(new Dimension(250, 28));
        tblTermini = new JTable();
        jScrollPane1 = new JScrollPane(tblTermini);
        jScrollPane1.setPreferredSize(new Dimension(660, 280));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));

        // ── Form panel ───────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o terminu "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0;
        formPanel.add(new JLabel("Tip termina:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(cmbTipTermina, g);
        g.weightx = 0; g.fill = GridBagConstraints.NONE;

        g.gridx = 0; g.gridy = 1;
        formPanel.add(new JLabel("Kancelarija:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(txtKancelarija, g);

        // ── Table section ─────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista termina "));
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
        setSize(760, 560);
        setLocationRelativeTo(null);
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
        String[] kolone = {"ID", "Tip termina", "Kancelarija"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTermini.setModel(tableModel);
        tblTermini.setRowHeight(24);
        tblTermini.setGridColor(new Color(220, 230, 245));
        tblTermini.getTableHeader().setBackground(new Color(232, 240, 254));
        tblTermini.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblTermini.setSelectionBackground(new Color(187, 222, 251));
    }

    public JTable getTblTermini() { return tblTermini; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JComboBox<domen.tipTermina> getCmbTipTermina() { return cmbTipTermina; }
    public JTextField getTxtKancelarija() { return txtKancelarija; }
    public domen.TerminDezurstva getSelektovani() { return selektovani; }
    public void setSelektovani(domen.TerminDezurstva t) { this.selektovani = t; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblTermini.getSelectionModel().addListSelectionListener(l);
    }
}
