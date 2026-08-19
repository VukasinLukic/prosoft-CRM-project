package ocr;

public class OcrPolje {
    private String nazivPolja;
    private String ocrVrednost;
    private double konfidens;
    private boolean uspesno;

    public String getNazivPolja() { return nazivPolja; }
    public void setNazivPolja(String n) { this.nazivPolja = n; }

    public String getOcrVrednost() { return ocrVrednost; }
    public void setOcrVrednost(String v) { this.ocrVrednost = v; }

    public double getKonfidens() { return konfidens; }
    public void setKonfidens(double k) { this.konfidens = k; }

    public boolean isUspesno() { return uspesno; }
    public void setUspesno(boolean u) { this.uspesno = u; }
}
