package forme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class StavkeObrascaForma extends JDialog {

    private JComboBox cmbPolje;
    private JTextField txtNivoPodudarnosti;
    private JTextField txtOcrVrednost;
    private JTextField txtKorigovanaVrednost;
    private JCheckBox chkOcrUspesno;
    private JScrollPane jScrollPane1;
    private JTable tblStavke;
    private JButton btnSacuvaj;
    private JButton btnDodaj;
    private JButton btnOcisti;
    private JButton btnObrisi;

    private DefaultTableModel tableModel;
    private domen.StavkeObrasca selektovani;

    public StavkeObrascaForma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public StavkeObrascaForma(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        initCustomTableModel();
    }

    private void initComponents() {
        setTitle("Stavke Obrasca");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cmbPolje = new JComboBox();
        cmbPolje.setPreferredSize(new Dimension(300, 28));
        txtOcrVrednost = new JTextField();
        txtOcrVrednost.setPreferredSize(new Dimension(300, 28));
        txtKorigovanaVrednost = new JTextField();
        txtKorigovanaVrednost.setPreferredSize(new Dimension(300, 28));
        txtNivoPodudarnosti = new JTextField("0.0");
        txtNivoPodudarnosti.setPreferredSize(new Dimension(120, 28));
        chkOcrUspesno = new JCheckBox("OCR uspešno");
        chkOcrUspesno.setFont(new Font("SansSerif", Font.PLAIN, 12));

        tblStavke = new JTable();
        jScrollPane1 = new JScrollPane(tblStavke);
        jScrollPane1.setPreferredSize(new Dimension(960, 300));

        btnDodaj = makeButton("Dodaj", new Color(21, 101, 192));
        btnSacuvaj = makeButton("Sačuvaj", new Color(46, 125, 50));
        btnObrisi = makeButton("Obriši", new Color(183, 28, 28));
        btnOcisti = makeButton("Očisti", new Color(97, 97, 97));

        // ── Info label ────────────────────────────────────────────────────────
        JLabel infoLabel = new JLabel(
                "<html><i>Dvostruki klik na red u tabeli obrasca otvara ovu formu. " +
                "Ovde možete ručno dodati ili izmeniti stavke OCR analize.</i></html>");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(100, 100, 120));
        infoLabel.setBorder(new EmptyBorder(4, 8, 4, 8));

        // ── Form panel ────────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Podaci o stavci "));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 10, 7, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Polje obrasca:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(cmbPolje, g);
        g.weightx = 0;

        g.gridx = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nivo podudarnosti (0-100):"), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtNivoPodudarnosti, g);

        g.gridx = 0; g.gridy = 1; g.fill = GridBagConstraints.NONE; g.weightx = 0;
        formPanel.add(new JLabel("OCR vrednost:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        formPanel.add(txtOcrVrednost, g);
        g.weightx = 0;

        g.gridx = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Korigovana vrednost:"), g);
        g.gridx = 3; g.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtKorigovanaVrednost, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
        formPanel.add(chkOcrUspesno, g);
        g.gridwidth = 1;

        // ── Table section ─────────────────────────────────────────────────────
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 240)), " Lista stavki obrasca "));
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        // ── Button panel ──────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 225, 245)));
        btnPanel.add(btnOcisti);
        btnPanel.add(btnObrisi);
        btnPanel.add(btnSacuvaj);
        btnPanel.add(btnDodaj);

        JPanel topGroup = new JPanel(new BorderLayout(0, 4));
        topGroup.setBorder(new EmptyBorder(4, 4, 0, 4));
        topGroup.add(infoLabel, BorderLayout.NORTH);
        topGroup.add(formPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout(4, 4));
        add(topGroup, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        initCustomTableModel();
        FormeUtil.otvoriPunEkran(this);
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
        String[] kolone = {"ID", "Polje", "OCR vrednost", "Korigovana vrednost", "Podudarnost", "OCR uspešno"};
        tableModel = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStavke.setModel(tableModel);
        tblStavke.setRowHeight(24);
        tblStavke.setGridColor(new Color(220, 230, 245));
        tblStavke.getTableHeader().setBackground(new Color(232, 240, 254));
        tblStavke.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblStavke.setSelectionBackground(new Color(187, 222, 251));
        // Wider columns for value fields
        if (tblStavke.getColumnModel().getColumnCount() > 2) {
            tblStavke.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblStavke.getColumnModel().getColumn(3).setPreferredWidth(200);
        }
    }

    public JTable getTblStavke() { return tblStavke; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JComboBox<domen.TipPolja> getCmbPolje() { return cmbPolje; }
    public JTextField getTxtOcrVrednost() { return txtOcrVrednost; }
    public JTextField getTxtKorigovanaVrednost() { return txtKorigovanaVrednost; }
    public JTextField getTxtNivoPodudarnosti() { return txtNivoPodudarnosti; }
    public JCheckBox getChkOcrUspesno() { return chkOcrUspesno; }
    public domen.StavkeObrasca getSelektovani() { return selektovani; }
    public void setSelektovani(domen.StavkeObrasca s) { this.selektovani = s; }

    public void addDodajListener(java.awt.event.ActionListener l) { btnDodaj.addActionListener(l); }
    public void addSacuvajListener(java.awt.event.ActionListener l) { btnSacuvaj.addActionListener(l); }
    public void addObrisiListener(java.awt.event.ActionListener l) { btnObrisi.addActionListener(l); }
    public void addOcistiListener(java.awt.event.ActionListener l) { btnOcisti.addActionListener(l); }
    public void addTabelaSelectionListener(javax.swing.event.ListSelectionListener l) {
        tblStavke.getSelectionModel().addListSelectionListener(l);
    }
}
