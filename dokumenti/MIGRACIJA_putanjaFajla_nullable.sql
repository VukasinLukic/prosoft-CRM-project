-- =========================================================
-- Migracija: dozvoli SV-20 obrazac bez skena (putanjaFajla NULL)
--
-- Zašto: radnik treba da može da registruje obrazac (student + zaposleni +
-- školska godina/semestar) PRE nego što fizički skenira papir — sken se
-- prilaže naknadno preko "Odaberi fajl..." + "Sačuvaj izmene". Kolona je
-- do sada bila NOT NULL, što je to sprečavalo.
--
-- Šta NE dira: OCR dugmad i dalje traže da putanjaFajla postoji pre nego
-- što se OCR pokrene — to je ostalo u Java kodu (SV20ObrazacController),
-- ovde se menja samo da li PRAZAN zapis sme da postoji u bazi.
--
-- VAŽNO: napravi backup baze pre pokretanja.
--   mysqldump -u <user> -p bazaocr > bazaocr_backup_pre_migracije.sql
-- =========================================================

ALTER TABLE sv20obrazac
    MODIFY putanjaFajla VARCHAR(500) NULL;

-- Provera posle migracije
DESCRIBE sv20obrazac;
