import csv
import os

# Pfade definieren
CSV_FILE = "uebersetzungen.csv"
OUTPUT_DIR = "src/main/resources"

def generate_properties_files(csv_file, output_dir):
    # Sicherstellen, dass das Ausgabeverzeichnis existiert
    os.makedirs(output_dir, exist_ok=True)

    with open(csv_file, mode="r", encoding="utf-8") as file:
        reader = csv.reader(file, delimiter=";")
        header = next(reader)  # Erste Zeile lesen (propertyname, Sprachen)

        # Spaltenüberschriften analysieren
        if len(header) < 2 or header[0].strip().lower() != "propertyname":
            raise ValueError("Die CSV-Datei hat kein korrektes Format (erste Spalte muss 'propertyname' sein).")

        languages = header[1:]  # Sprachkürzel (z.B. en, de, nl)
        translations = {lang: [] for lang in languages}

        # CSV-Inhalt lesen und Übersetzungen sammeln
        for row in reader:
            if not row or len(row) == 0:
                continue  # Leere Zeile überspringen
            if row[0].startswith("#") or not row[0].strip():
                continue  # Kommentar oder leere Property

            property_name = row[0].strip()
            for i, lang in enumerate(languages):
                translations[lang].append(f"{property_name}={row[i + 1].strip()}")

        # Properties-Dateien generieren
        for lang, lines in translations.items():
            file_name = f"messages_{lang}.properties" if lang != languages[0] else "messages.properties"
            file_path = os.path.join(output_dir, file_name)

            with open(file_path, mode="w", encoding="utf-8") as prop_file:
                prop_file.write("\n".join(lines))
                print(f"Generiert: {file_path}")

if __name__ == "__main__":
    generate_properties_files(CSV_FILE, OUTPUT_DIR)