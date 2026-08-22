package forme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Otvara prozor preko cele raspolozive povrsine ekrana (izuzev trake zadataka).
 * Radi i za JFrame (right pravo maksimiziranje preko setExtendedState) i za
 * JDialog (koji tu opciju nema, pa mu se granice rucno postave na ekran).
 */
final class FormeUtil {

    private FormeUtil() {
    }

    static void otvoriPunEkran(Window w) {
        if (w instanceof Frame) {
            ((Frame) w).setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        w.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
    }

    /**
     * Zajednički kompaktan izgled za sve tabele u aplikaciji — niži redovi, tanke linije,
     * centriran sadržaj, i eksplicitne širine kolona (bez ovoga sve kolone dobijaju istu
     * širinu pa kratke vrednosti kao "1" ostave ogroman prazan prostor dok se ime/adresa seku).
     * `sirine` daje preferiranu širinu za onoliko kolona koliko ih ima — ako je tabela ima
     * više kolona nego sto je širina zadato, ostatak dobija podrazumevanu širinu.
     */
    static void stilizujTabelu(JTable t, int... sirine) {
        t.setRowHeight(24);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(232, 236, 241));
        t.getTableHeader().setBackground(new Color(242, 244, 247));
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setSelectionBackground(new Color(214, 224, 235));

        DefaultTableCellRenderer centrirano = new DefaultTableCellRenderer();
        centrirano.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < t.getColumnModel().getColumnCount(); i++) {
            if (i < sirine.length) {
                t.getColumnModel().getColumn(i).setPreferredWidth(sirine[i]);
            }
            // Ne pregazi renderer koji je forma već posebno postavila (npr. obojene značke) —
            // samo centriraj obične tekstualne kolone.
            TableCellRenderer postojeci = t.getColumnModel().getColumn(i).getCellRenderer();
            if (postojeci == null) {
                t.getColumnModel().getColumn(i).setCellRenderer(centrirano);
            }
        }
    }
}
