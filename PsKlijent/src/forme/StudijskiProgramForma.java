package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class StudijskiProgramForma extends JPanel {

    private JTextField txtNaziv;
    private JTextField txtOznaka;
    private JComboBox cmbStepenStudija;
    private JScrollPane jScrollPane1;
    private JTable tblProgrami;
    private JButton btnSacuvaj;
    private JButton btnDodaj;
    private JButton btnOcisti;
    private JButton btnObrisi;

    private DefaultTableModel tableModel;
    private domen.StudijskiProgram selektovani;

    public StudijskiProgramForma() {
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setBackground(Color.WHITE);

        txtNaziv = new JTextField(); txtNaziv.setPreferredSize(new Dimension(320, 28));
        txtOznaka = new JTextField(); txtOznaka.setPreferredSize(new Dimension(160, 28));
        cmbStepenStudija = new JComboBox();
        cmbStepenStudija.setPreferredSize(new Dimension(220, 28));
        cmbStepenStudija.addActionListener(this::cmbStepenStudijaActionPerformed);
        tblProgrami = new JTable();
        jScrollPane1 = new JScrollPane(tblProgrami);
        jScrollPane1.setPreferredSize(new Dimension(720, 280));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));

        // ── Form panel ──────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o studijskom programu "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 10, 7, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.gridwidth = 1;
        formPanel.add(new JLabel("Naziv programa:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(txtNaziv, g);
        g.weightx = 0; g.fill = GridBagConstraints.NONE;

        g.gridx = 0; g.gridy = 1;
        formPanel.add(new JLabel("Oznaka:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(txtOznaka, g);
        g.weightx = 0; g.fill = GridBagConstraints.NONE;

        g.gridx = 0; g.gridy = 2;
        formPanel.add(new JLabel("Stepen studija:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(cmbStepenStudija, g);

        // ── Table section ────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista studijskih programa "));
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        // ── Button panel ─────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 225, 245)));
        btnPanel.add(btnOcisti);
        btnPanel.add(btnObrisi);
        btnPanel.add(btnSacuvaj);
        btnPanel.add(btnDodaj);

        JPanel topGroup = new JPanel(new BorderLayout(0, 4));
        topGroup.setBorder(new EmptyBorder(4, 4, 4, 4));
        topGroup.add(naslovPanel("Upravljanje studijskim programima"), BorderLayout.NORTH);
        topGroup.add(formPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        initCustomTableModel();
    }

    private JComponent naslovPanel(String tekst) {
        JLabel naslov = new JLabel(tekst);
        naslov.setFont(new Font("SansSerif", Font.BOLD, 18));
        naslov.setForeground(new Color(38, 70, 110));
        naslov.setBorder(new EmptyBorder(2, 2, 10, 2));
        return naslov;
    }

    private void cmbStepenStudijaActionPerformed(java.awt.event.ActionEvent evt) {}

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
        String[] kolone = {"ID", "Naziv", "Oznaka", "Stepen studija"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProgrami.setModel(tableModel);
        tblProgrami.setRowHeight(24);
        tblProgrami.setGridColor(new Color(220, 230, 245));
        tblProgrami.getTableHeader().setBackground(new Color(232, 240, 254));
        tblProgrami.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblProgrami.setSelectionBackground(new Color(187, 222, 251));
    }

    public JTable getTblProgrami() { return tblProgrami; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtNaziv() { return txtNaziv; }
    public JTextField getTxtOznaka() { return txtOznaka; }
    public JComboBox<domen.stepenStudija> getCmbStepenStudija() { return cmbStepenStudija; }
    public domen.StudijskiProgram getSelektovani() { return selektovani; }
    public void setSelektovani(domen.StudijskiProgram sp) { this.selektovani = sp; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblProgrami.getSelectionModel().addListSelectionListener(l);
    }
}
