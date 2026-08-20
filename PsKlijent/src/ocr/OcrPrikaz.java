package ocr;

import java.awt.image.BufferedImage;

public class OcrPrikaz {

    private final BufferedImage slika;
    private final boolean pdf;
    private final int brojStrana;

    public OcrPrikaz(BufferedImage slika, boolean pdf, int brojStrana) {
        this.slika = slika;
        this.pdf = pdf;
        this.brojStrana = brojStrana;
    }

    public BufferedImage getSlika() { return slika; }
    public boolean isPdf() { return pdf; }
    public int getBrojStrana() { return brojStrana; }
}
