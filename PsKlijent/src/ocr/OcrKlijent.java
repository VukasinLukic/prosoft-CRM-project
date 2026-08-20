package ocr;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OcrKlijent {

    private static final String OCR_URL = "http://localhost:9001/api/ocr/process";
    private static final String HEALTH_URL = "http://localhost:9001/api/health";

    public static void proveriServis() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(HEALTH_URL))
                .GET()
                .timeout(Duration.ofSeconds(5))
                .build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new Exception("OCR servis nije dostupan (status: " + resp.statusCode() + ")");
        }
    }

    public static List<OcrPolje> obradiObrazac(String putanjaFajla, String obrazacId) throws Exception {
        java.nio.file.Path filePath = Paths.get(putanjaFajla);
        System.out.println("[OcrKlijent debug] primljena putanja : " + putanjaFajla);
        System.out.println("[OcrKlijent debug] Paths.get() daje  : " + filePath);
        System.out.println("[OcrKlijent debug] toAbsolutePath()  : " + filePath.toAbsolutePath());
        System.out.println("[OcrKlijent debug] Files.exists()    : " + Files.exists(filePath));
        if (!Files.exists(filePath)) {
            throw new Exception("Fajl nije pronađen: " + putanjaFajla);
        }

        byte[] fileBytes = Files.readAllBytes(filePath);
        String fileName = filePath.getFileName().toString();
        String mimeType = guessMimeType(fileName);
        String boundary = "----OcrBoundary" + System.currentTimeMillis();
        System.out.println("[OcrKlijent debug] fajl: " + fileName + " (" + fileBytes.length + " B, " + mimeType + "), obrazacId=" + obrazacId);

        byte[] body = buildMultipartBody(boundary, fileBytes, fileName, mimeType, obrazacId);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OCR_URL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .timeout(Duration.ofSeconds(120))
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

        System.out.println("[OcrKlijent debug] POST " + OCR_URL);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("[OcrKlijent debug] odgovor status=" + response.statusCode()
                + " telo=" + trimZaLog(response.body()));

        if (response.statusCode() != 200) {
            throw new Exception("OCR servis vratio grešku " + response.statusCode() + ": " + response.body());
        }

        return parseOcrResponse(response.body());
    }

    private static String trimZaLog(String s) {
        if (s == null) return "null";
        return s.length() > 500 ? s.substring(0, 500) + "... (" + s.length() + " karaktera ukupno)" : s;
    }

    private static byte[] buildMultipartBody(String boundary, byte[] fileBytes,
            String fileName, String mimeType, String obrazacId) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String fileHeader = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n"
                + "Content-Type: " + mimeType + "\r\n\r\n";
        out.write(fileHeader.getBytes("UTF-8"));
        out.write(fileBytes);
        out.write("\r\n".getBytes("UTF-8"));

        if (obrazacId != null && !obrazacId.isEmpty()) {
            String idPart = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"obrazac_id\"\r\n\r\n"
                    + obrazacId + "\r\n";
            out.write(idPart.getBytes("UTF-8"));
        }

        out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        return out.toByteArray();
    }

    private static String guessMimeType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".tif") || lower.endsWith(".tiff")) return "image/tiff";
        if (lower.endsWith(".bmp")) return "image/bmp";
        return "application/octet-stream";
    }

    private static List<OcrPolje> parseOcrResponse(String json) {
        List<OcrPolje> result = new ArrayList<>();

        int fieldsStart = json.indexOf("\"fields\"");
        if (fieldsStart < 0) return result;

        int arrayStart = json.indexOf('[', fieldsStart);
        if (arrayStart < 0) return result;

        int arrayEnd = findMatchingBracket(json, arrayStart);
        if (arrayEnd < 0) return result;

        String fieldsJson = json.substring(arrayStart + 1, arrayEnd);

        int pos = 0;
        while (pos < fieldsJson.length()) {
            int objStart = fieldsJson.indexOf('{', pos);
            if (objStart < 0) break;
            int objEnd = findMatchingBrace(fieldsJson, objStart);
            if (objEnd < 0) break;

            String fieldJson = fieldsJson.substring(objStart, objEnd + 1);
            OcrPolje polje = parseOcrPolje(fieldJson);
            if (polje != null && polje.getNazivPolja() != null) {
                result.add(polje);
            }
            pos = objEnd + 1;
        }

        return result;
    }

    private static int findMatchingBrace(String s, int openPos) {
        int depth = 0;
        boolean inStr = false;
        boolean esc = false;
        for (int i = openPos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) { esc = false; continue; }
            if (c == '\\' && inStr) { esc = true; continue; }
            if (c == '"') { inStr = !inStr; continue; }
            if (inStr) continue;
            if (c == '{') depth++;
            else if (c == '}') { depth--; if (depth == 0) return i; }
        }
        return -1;
    }

    private static int findMatchingBracket(String s, int openPos) {
        int depth = 0;
        boolean inStr = false;
        boolean esc = false;
        for (int i = openPos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (esc) { esc = false; continue; }
            if (c == '\\' && inStr) { esc = true; continue; }
            if (c == '"') { inStr = !inStr; continue; }
            if (inStr) continue;
            if (c == '[') depth++;
            else if (c == ']') { depth--; if (depth == 0) return i; }
        }
        return -1;
    }

    private static OcrPolje parseOcrPolje(String json) {
        OcrPolje p = new OcrPolje();
        p.setNazivPolja(extractString(json, "field_name"));

        // Preferiraj validated_value (post-processed) nad raw ocr_value
        String validated = extractString(json, "validated_value");
        String raw = extractString(json, "ocr_value");
        p.setOcrVrednost((validated != null && !validated.isEmpty()) ? validated : raw);

        try {
            String conf = extractNumber(json, "confidence");
            p.setKonfidens(conf.isEmpty() ? 0.0 : Double.parseDouble(conf));
        } catch (NumberFormatException ex) {
            p.setKonfidens(0.0);
        }
        p.setUspesno("true".equalsIgnoreCase(extractBoolean(json, "is_valid")));
        return p;
    }

    private static String extractString(String json, String key) {
        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos < 0) return null;
        int colonPos = json.indexOf(':', keyPos + search.length());
        if (colonPos < 0) return null;
        int valStart = colonPos + 1;
        while (valStart < json.length() && Character.isWhitespace(json.charAt(valStart))) valStart++;
        if (valStart >= json.length()) return null;
        char first = json.charAt(valStart);
        if (first == '"') {
            StringBuilder sb = new StringBuilder();
            boolean esc = false;
            for (int i = valStart + 1; i < json.length(); i++) {
                char c = json.charAt(i);
                if (esc) { sb.append(c); esc = false; continue; }
                if (c == '\\') { esc = true; continue; }
                if (c == '"') break;
                sb.append(c);
            }
            return sb.toString();
        }
        if (first == 'n') return null; // null
        return null;
    }

    private static String extractNumber(String json, String key) {
        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos < 0) return "0";
        int colonPos = json.indexOf(':', keyPos + search.length());
        if (colonPos < 0) return "0";
        int valStart = colonPos + 1;
        while (valStart < json.length() && Character.isWhitespace(json.charAt(valStart))) valStart++;
        int valEnd = valStart;
        while (valEnd < json.length() && (Character.isDigit(json.charAt(valEnd))
                || json.charAt(valEnd) == '.' || json.charAt(valEnd) == '-')) {
            valEnd++;
        }
        return valStart < valEnd ? json.substring(valStart, valEnd) : "0";
    }

    private static String extractBoolean(String json, String key) {
        String search = "\"" + key + "\"";
        int keyPos = json.indexOf(search);
        if (keyPos < 0) return "false";
        int colonPos = json.indexOf(':', keyPos + search.length());
        if (colonPos < 0) return "false";
        int valStart = colonPos + 1;
        while (valStart < json.length() && Character.isWhitespace(json.charAt(valStart))) valStart++;
        if (valStart + 4 <= json.length() && json.substring(valStart, valStart + 4).equals("true")) return "true";
        return "false";
    }
}
