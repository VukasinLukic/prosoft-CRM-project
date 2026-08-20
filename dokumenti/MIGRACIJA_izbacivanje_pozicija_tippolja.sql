-- =========================================================
-- Migracija: izbacivanje mrtvih pozicionih kolona iz `tippolja`
-- Prati:  dokumenti/PLAN_IZBACIVANJE_KOORDINATA_TIPPOLJA.md
--
-- Šta radi:
--   - briše pozicijaX, pozicijaY, sirina, visina (OCR ih nikad nije
--     čitao — jedini izvor koordinata je ocr-microservice/.../sv20_template.json)
--   - briše CHECK constraint chk_tp_pozicije (validirao je baš te kolone)
--
-- Šta NE dira:
--   - stranica, redosledObrade ostaju (koriste se za grupisanje/sortiranje
--     polja u novom OCR pregled ekranu)
--   - chk_tp_stranica, chk_tp_regex ostaju netaknuti
--   - podaci u ostalim tabelama (sv20obrazac, stavkeobrasca, student, ...)
--
-- VAŽNO: napravi backup baze pre pokretanja.
--   mysqldump -u <user> -p bazaocr > bazaocr_backup_pre_migracije.sql
--
-- Pokreni Java klijent/server tek POSLE ove migracije, sa kodom koji
-- više ne referencira pozicijaX/Y/sirina/visina (commit koji uklanja
-- te kolone iz domen/TipPolja.java, TipPoljaForma.java, TipPoljaController.java).
-- =========================================================

-- DROP CHECK zahteva MySQL 8.0.16+. Ako baza koristi stariju verziju,
-- zameni sa: ALTER TABLE tippolja DROP CONSTRAINT chk_tp_pozicije;
ALTER TABLE tippolja
    DROP CHECK chk_tp_pozicije;

ALTER TABLE tippolja
    DROP COLUMN pozicijaX,
    DROP COLUMN pozicijaY,
    DROP COLUMN sirina,
    DROP COLUMN visina;

-- Provera posle migracije — očekivane kolone: idPolja, nazivPolja, tipPodatka,
-- regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje
DESCRIBE tippolja;
