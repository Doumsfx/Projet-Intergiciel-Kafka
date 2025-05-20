package insa.uphf.fr.perTranslateClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Client simplifié pour l'API LibreTranslate
 */
@Component
public class LibreTranslateClient {
    private final HttpClient httpClient;
    private final String apiUrl;
    
    public LibreTranslateClient(@Value("${libretranslate.api-url:http://localhost:5000}") String apiUrl) {
        this.apiUrl = apiUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    /**
     * Détecte la langue source et traduit le texte en français
     * 
     * @param text Texte à traduire
     * @return Le texte traduit en français
     */
    public String translateToFrench(String text) {
        try {
            // 1. Détection de la langue source
            String sourceLang = detectLanguage(text);
            
            // Si c'est déjà du français, ne pas traduire
            if ("fr".equals(sourceLang)) {
                return text;
            }
            
            // 2. Traduction vers le français
            return translate(text, sourceLang, "fr");
        } catch (Exception e) {
            System.err.println("Erreur de traduction: " + e.getMessage());
            return "[Erreur] " + text;
        }
    }
    
    /**
     * Détecte la langue d'un texte
     */
    private String detectLanguage(String text) throws IOException, InterruptedException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", text);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/detect"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
                
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Erreur API: " + response.statusCode());
        }
        
        JSONArray jsonArray = new JSONArray(response.body());
        if (jsonArray.length() > 0) {
            return jsonArray.getJSONObject(0).getString("language");
        }
        
        // Si la détection échoue, on suppose que c'est de l'anglais
        return "en";
    }
    
    /**
     * Traduit un texte d'une langue à une autre
     */
    private String translate(String text, String source, String target) throws IOException, InterruptedException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("q", text);
        requestBody.put("source", source);
        requestBody.put("target", target);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/translate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
                
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Erreur API: " + response.statusCode());
        }
        
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse.getString("translatedText");
    }
}