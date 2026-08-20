-- =========================================================
-- Fix: dopunjava 5 redova iz seed_tippolja.sql koji su pukli
-- na CHECK constraint-ima (chk_tp_regex / chk_tp_pozicije).
-- Pokreni OVO umesto seed_tippolja.sql ako si vec ubacio
-- ostalih 32 reda (izbegava duplikate).
-- =========================================================

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('jmbg', 'NUMERIC', NULL, 98, 390, 258, 33, 1, 2, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_prvog_upisa', 'NUMERIC', NULL, 600, 1020, 120, 33, 1, 9, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('espb_bodovi', 'NUMERIC', NULL, NULL, NULL, NULL, NULL, 1, 10, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_godina', 'NUMERIC', NULL, 880, 1280, 100, 33, 1, 15, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, pozicijaX, pozicijaY, sirina, visina, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_rodjenja', 'NUMERIC', NULL, 450, 1370, 100, 33, 1, 17, true, true);
