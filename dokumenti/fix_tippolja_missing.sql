-- =========================================================
-- Fix: dopunjava 5 redova iz seed_tippolja.sql koji su pukli
-- na CHECK constraint-u chk_tp_regex.
-- Pokreni OVO umesto seed_tippolja.sql ako si vec ubacio
-- ostalih 32 reda (izbegava duplikate).
-- =========================================================

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('jmbg', 'NUMERIC', NULL, 1, 2, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_prvog_upisa', 'NUMERIC', NULL, 1, 9, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('espb_bodovi', 'NUMERIC', NULL, 1, 10, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_godina', 'NUMERIC', NULL, 1, 15, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_rodjenja', 'NUMERIC', NULL, 1, 17, true, true);
