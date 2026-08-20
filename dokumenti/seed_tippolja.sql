-- =========================================================
-- Seed: TipPolja za SV-20 obrazac
-- Pokretanje: jednom, pre prvog koriscenja OCR funkcionalnosti
-- Nazivi polja MORAJU se poklapati sa "name" u sv20_template.json
--
-- Koordinate za OCR zive iskljucivo u
-- ocr-microservice/sv20-ocr-service/templates/sv20_template.json
-- (TipPolja vise nema pozicijaX/Y/sirina/visina kolone — vidi
-- dokumenti/PLAN_IZBACIVANJE_KOORDINATA_TIPPOLJA.md).
-- =========================================================

-- Ocisti prethodni seed ako postoji (opcionalno)
-- DELETE FROM tippolja WHERE nazivPolja IN (
--   'ime_prezime_studenta','jmbg','broj_indeksa','univerzitet',
--   'fakultet','studijski_program','vrsta_studija','opstina_ustanove',
--   'godina_prvog_upisa','espb_bodovi','nacin_finansiranja',
--   'srednja_skola_naziv','srednja_skola_vrsta','srednja_skola_opstina',
--   'srednja_skola_godina','pol','godina_rodjenja','mesto_rodjenja',
--   'prebivaliste_opstina','prebivaliste_naselje','prebivaliste_ulica',
--   'drzavljanstvo','nacionalna_pripadnost','bracni_status',
--   'mesto_stanovanja_studiranje','tip_smestaja','izvor_sredstava',
--   'broj_glasackog_lista','da_li_ste_zaposleni','izdrzavanje_drugih',
--   'roditelj_zaposlen','zanimanje_roditelja','skolska_sprema_oca',
--   'skolska_sprema_majke','potrebna_podrska','datum_popunjavanja',
--   'potpis_studenta'
-- );

-- ── Stranica 1 ──────────────────────────────────────────────────────────────

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('ime_prezime_studenta', 'TEXT', NULL, 1, 1, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('jmbg', 'NUMERIC', NULL, 1, 2, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('broj_indeksa', 'ALPHANUMERIC', '^[0-9]{4}/[0-9]{4}$', 1, 3, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('univerzitet', 'TEXT', NULL, 1, 4, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('fakultet', 'TEXT', NULL, 1, 5, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('studijski_program', 'TEXT', NULL, 1, 6, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('vrsta_studija', 'BOOLEAN', NULL, 1, 7, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('opstina_ustanove', 'TEXT', NULL, 1, 8, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_prvog_upisa', 'NUMERIC', NULL, 1, 9, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('espb_bodovi', 'NUMERIC', NULL, 1, 10, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('nacin_finansiranja', 'BOOLEAN', NULL, 1, 11, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_naziv', 'TEXT', NULL, 1, 12, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_vrsta', 'TEXT', NULL, 1, 13, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_opstina', 'TEXT', NULL, 1, 14, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('srednja_skola_godina', 'NUMERIC', NULL, 1, 15, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('pol', 'BOOLEAN', NULL, 1, 16, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('godina_rodjenja', 'NUMERIC', NULL, 1, 17, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('mesto_rodjenja', 'TEXT', NULL, 1, 18, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('prebivaliste_opstina', 'TEXT', NULL, 1, 19, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('prebivaliste_naselje', 'TEXT', NULL, 1, 20, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('prebivaliste_ulica', 'TEXT', NULL, 1, 21, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('drzavljanstvo', 'TEXT', NULL, 1, 22, true, true);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('nacionalna_pripadnost', 'TEXT', NULL, 1, 23, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('bracni_status', 'BOOLEAN', NULL, 1, 24, true, false);

-- ── Stranica 2 ──────────────────────────────────────────────────────────────

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('mesto_stanovanja_studiranje', 'TEXT', NULL, 2, 25, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('tip_smestaja', 'BOOLEAN', NULL, 2, 26, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('izvor_sredstava', 'TEXT', NULL, 2, 27, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('broj_glasackog_lista', 'TEXT', NULL, 2, 28, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('da_li_ste_zaposleni', 'BOOLEAN', NULL, 2, 29, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('izdrzavanje_drugih', 'BOOLEAN', NULL, 2, 30, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('roditelj_zaposlen', 'BOOLEAN', NULL, 2, 31, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('zanimanje_roditelja', 'TEXT', NULL, 2, 32, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('skolska_sprema_oca', 'BOOLEAN', NULL, 2, 33, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('skolska_sprema_majke', 'BOOLEAN', NULL, 2, 34, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('potrebna_podrska', 'TEXT', NULL, 2, 35, false, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('datum_popunjavanja', 'DATE', NULL, 2, 36, true, false);

INSERT INTO tippolja (nazivPolja, tipPodatka, regexValidacija, stranica, redosledObrade, podrzavaOCR, obaveznoPolje)
VALUES ('potpis_studenta', 'TEXT', NULL, 2, 37, false, false);
