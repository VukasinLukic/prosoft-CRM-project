package forme;

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

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
}
