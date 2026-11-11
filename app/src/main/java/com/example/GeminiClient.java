package com.example;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;


// Classe GeminiClient: Client HTTP natif bech nconnectiw el application b l'API Gemini IA
// Ma tsta3mel 7atta bibliothèque externe (pas de Retrofit, pas de Volley). Tsta3mel HttpURLConnection w Executors

public final class GeminiClient {

    private static final String TAG = "GEMINI";

    // Interface Callback bech nraj3ou biha el résultat (onSuccess wala onError) lel Activity
    public interface ReponseCallback {
        void onSuccess(String reponse);
        void onError(String messageErreur);
    }

    // Thread pool bech nexecutiw el requêtes HTTP fel background thread w ma na3mlouhc ANR 3al Main Thread
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    // Handler bech nraj3ou el résultat lel Main UI Thread (bech nfar8ou wala n3mlou update el UI)
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private GeminiClient() {}

    // Fonction principale envoyerMessage: Ta5ou el historique w el message w tab3athhom b POST request l'API Gemini
    public static void envoyerMessage(
            final Context context,
            final List<ChatMessage> historique,
            final String messageActuel,
            final ReponseCallback callback) {

        // Exécution fel background thread
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    // Recuperation lel clé API mta3 Gemini injectée dynamiquement mel BuildConfig
                    String cleApi = BuildConfig.GEMINI_API_KEY;
                    if (cleApi == null || cleApi.trim().isEmpty() || "MY_GEMINI_API_KEY".equals(cleApi.trim())) {
                        posterErreur(callback, "Clé API non configurée");
                        return;
                    }

                    // Recuperation lel System Instruction (System Prompt) mta3 el Douanier IA mel strings.xml
                    String systemInstruction = context.getString(R.string.gemini_system_instruction);

                    // Construction mta3 el payload JSON eli bech neb3thouh l'API
                    JSONObject payload = construirePayloadGemini(systemInstruction, historique, messageActuel);

                    // URL mta3 el endpoint officiel ta3 Gemini (gemini-1.5-flash)
                    URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    // Passiw el clé API fel header HTTP x-goog-api-key (Sécurité maximale)
                    connection.setRequestProperty("x-goog-api-key", cleApi.trim());
                    connection.setConnectTimeout(25000);
                    connection.setReadTimeout(60000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setDoOutput(true);

                    // Écriture mta3 el payload JSON fel OutputStream mta3 el connection
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(input, 0, input.length);
                    }

                    // Vérification mta3 el code HTTP (200 OK veut dire succès)
                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        String reponseJson = lireFlux(connection.getInputStream());
                        // Extraire el text mta3 el réponse mel JSON mta3 Gemini
                        final String texte = extraireTexteGemini(reponseJson);
                        // Nraj3ou el text lel Main Thread bech naffichiweh fel chat
                        MAIN_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(texte);
                            }
                        });
                    } else {
                        String erreur = connection.getErrorStream() != null ? lireFlux(connection.getErrorStream()) : ("Code " + statusCode);
                        Log.w(TAG, "Erreur serveur " + statusCode + ": " + erreur);
                        posterErreur(callback, "Erreur " + statusCode);
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Avertissement réseau: " + e.getMessage());
                    posterErreur(callback, "Erreur réseau: " + e.getMessage());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    // Fonction bech na3mlou construction lel payload JSON (System instruction + Historique des 10 derniers messages + GenerationConfig)
    private static JSONObject construirePayloadGemini(
            String systemInstruction,
            List<ChatMessage> historique,
            String messageActuel) throws Exception {

        JSONObject payload = new JSONObject();

        // 1. System Instruction (El Consigne mta3 el Douanier IA w Protocol 1964)
        JSONObject sysObj = new JSONObject();
        JSONArray sysParts = new JSONArray();
        sysParts.put(new JSONObject().put("text", systemInstruction));
        sysObj.put("parts", sysParts);
        payload.put("system_instruction", sysObj);

        // 2. Contents (Historique mta3 el conversation b roles user w model)
        JSONArray contents = new JSONArray();
        List<ChatMessage> listeFiltree = preparerHistorique(historique, messageActuel);
        for (ChatMessage msg : listeFiltree) {
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", msg.isUser() ? "user" : "model");
            JSONArray parts = new JSONArray();
            parts.put(new JSONObject().put("text", msg.getTexte()));
            msgObj.put("parts", parts);
            contents.put(msgObj);
        }
        payload.put("contents", contents);

        // 3. GenerationConfig (Temperature 0.3 bech el réponses ykounou formelles w académiques)
        JSONObject genConfig = new JSONObject();
        genConfig.put("maxOutputTokens", 1024);
        genConfig.put("temperature", 0.3);
        payload.put("generationConfig", genConfig);

        return payload;
    }

    // Fonction bech na3mlou extraction lel text mta3 el réponse mel JSON complex mta3 Gemini
    private static String extraireTexteGemini(String reponseJson) throws Exception {
        JSONObject root = new JSONObject(reponseJson);
        JSONArray candidates = root.optJSONArray("candidates");
        if (candidates != null && candidates.length() > 0) {
            JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
            if (content != null) {
                JSONArray parts = content.optJSONArray("parts");
                if (parts != null && parts.length() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length(); i++) {
                        JSONObject p = parts.optJSONObject(i);
                        if (p != null && p.has("text")) {
                            sb.append(p.optString("text"));
                        }
                    }
                    String texte = sb.toString().trim();
                    if (!texte.isEmpty()) {
                        return texte;
                    }
                }
            }
        }
        throw new Exception("Réponse vide");
    }

    // Fonction bech na3mlou préparation lel historique (na5dhou a5er 10 messages w nt2akdou ennou el list tabda b user)
    private static List<ChatMessage> preparerHistorique(List<ChatMessage> source, String messageActuel) {
        List<ChatMessage> resultat = new ArrayList<>();
        if (source != null) {
            int debut = Math.max(0, source.size() - 10);
            for (int i = debut; i < source.size(); i++) {
                resultat.add(source.get(i));
            }
        }
        while (!resultat.isEmpty() && !resultat.get(0).isUser()) {
            resultat.remove(0);
        }
        if (resultat.isEmpty() || !resultat.get(resultat.size() - 1).getTexte().equals(messageActuel)) {
            resultat.add(new ChatMessage(messageActuel, true, ""));
        }
        return resultat;
    }

    // Fonction bech na3mlou posting lel erreur lel Main Thread
    private static void posterErreur(final ReponseCallback callback, final String message) {
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(message);
            }
        });
    }

    // Fonction bech na9raw el flux (InputStream) w n7awlouh l String
    private static String lireFlux(InputStream is) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                sb.append(ligne).append("\n");
            }
            return sb.toString();
        }
    }
}
