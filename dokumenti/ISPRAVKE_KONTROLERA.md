> **REŠENO.** Sve ispravke opisane u ovom dokumentu su primenjene — sve forme sada postoje, svi kontroleri su usklađeni sa V2 nazivima metoda, i ceo projekat se čisto kompajlira (provereno sa `javac`). Ovaj fajl je ostavljen kao istorijski zapis, ne opisuje trenutno stanje koda.

# Izveštaj o greškama u V2App Klijentu (Paket `kontroleri`)

Analizom kompilacionih grešaka u modulu `PsKlijent`, utvrđeno je da greške nastaju zbog neusklađenosti između starih kontrolera (koji su netaknuti preneseni iz V1 verzije) i novog dizajna formi opisanog u dokumentu `DOKUMENTACIJA_IZRADE_FORMI_V2.md`. 

Kontroleri pokušavaju da pozovu metode za dobavljanje GUI komponenti pod starim nazivima, i očekuju stare tipove komponenti (npr. `JSpinner` umesto `JTextField`), dok su u V2 formama ti nazivi i tipovi potpuno promenjeni da bi pratili novi standard.

Da bi aplikacija mogla uspešno da se iskompajlira i proradi, potrebno je da se u fajlovima unutar paketa `kontroleri` izvrše sledeće ispravke:

## 1. `StavkeObrascaController.java`
- **Dugme za zatvaranje:** `forma.addZatvoriListener(...)` više ne postoji. U V2 formi nije definisano dugme "Zatvori". Treba ukloniti ovaj listener iz kontrolera, ili u V2 formu `StavkeObrascaForma` dodati `btnZatvori` i odgovarajuću metodu.
- **Tip polja:** Svuda gde piše `forma.getCmbTipPolja()`, promeniti u `forma.getCmbPolje()`.
- **Nivo podudarnosti:** Kontroler očekuje spinner (`getSpnNivoPodudarnosti().getValue()`), dok je po V2 dokumentaciji to tekstualno polje (`txtNivoPodudarnosti`). Potrebno je raditi parsiranje: `Double.parseDouble(forma.getTxtNivoPodudarnosti().getText())`. Shodno tome, postavljanje vrednosti ide preko `setText(String.valueOf(0.0))`.
- **Selektovana stavka:** Promeniti `forma.getSelektovana()` i `forma.setSelektovana(...)` u `forma.getSelektovani()` i `forma.setSelektovani(...)`.
- **Objekat obrasca:** Kontroler poziva `forma.getObrazac()`, međutim ta metoda ne postoji u V2 specifikaciji forme. U klasu `StavkeObrascaForma.java` treba ubaciti metodu `public SV20Obrazac getObrazac() { ... }` (i polje u koje se obrazac smešta).

## 2. `StudijskiProgramController.java`
- **Komboboks za stepen studija:** Promeniti poziv `forma.getCmbStepen()` u `forma.getCmbStepenStudija()`.
- **Tabela programa:** Promeniti poziv `forma.getTblStudijskiProgrami()` u `forma.getTblProgrami()`.

## 3. `SV20ObrazacController.java`
- **Dugme za uređivanje stavki:** Kontroler poziva `forma.addUrediStavkeListener(...)`, ali dugme `btnUrediStavke` ne postoji u V2 specifikaciji. Potrebno je ili u formu dodati `JButton btnUrediStavke` i prateću `add` metodu, ili obrisati taj listener i iskoristiti neko postojeće dugme.
- **Tabela obrazaca (listener):** Promeniti `forma.addTabelaSelectionListener(...)` u `forma.addTabelaObrasciSelectionListener(...)` kako stoji u V2 dokumentaciji za formu.
- **Selektovani obrazac:** Promeniti svaki poziv `forma.getSelektovani()` i `forma.setSelektovani(...)` u `forma.getSelektovaniObrazac()` i `forma.setSelektovaniObrazac(...)`.

## 4. `TerminDezurstvaController.java`
- **Dugme za dodavanje:** Promeniti `forma.addUbaciListener(...)` u `forma.addDodajListener(...)`.

## 5. `TipPoljaController.java`
- **Regex:** Promeniti `forma.getTxtRegex()` u `forma.getTxtRegexValidacija()`.
- **Pozicije i Dimenzije (X, Y, Širina, Visina):** U V1 su ovo bili spinneri (`getSpnPozicijaX()`, itd.), dok su po V2 specifikaciji oni sada tekstualna polja (`JTextField`). Shodno tome:
  - Prilikom očitavanja umesto `(int) forma.getSpnPozicijaX().getValue()` treba raditi parsiranje teksta: `Integer.parseInt(forma.getTxtPozicijaX().getText())`. Ista izmena važi i za X, Y, Širinu i Visinu.
  - Prilikom postavljanja vrednosti umesto `setValue(0)` treba raditi `setText("0")`.
- **Redosled obrade:** Promeniti poziv `forma.getSpnRedosled()` u `forma.getSpnRedosledObrade()`.
- **Tabela:** Promeniti sve pozive `forma.getTblTipPolja()` u `forma.getTblTipoviPolja()`.
