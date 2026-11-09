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

public final class GeminiClient {

    private static final String TAG = "GEMINI";

    public static final String MODEL_NAME = "gemini-3.5-flash-lite";

    public interface ReponseCallback {
        void onSuccess(String reponse);
        void onError(String messageErreur);
    }

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private GeminiClient() {}

    public static void envoyerMessage(
            final Context context,
            final List<ChatMessage> historique,
            final String messageActuel,
            final ReponseCallback callback) {

        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String cleApi = BuildConfig.GEMINI_API_KEY;
                    if (cleApi == null || cleApi.trim().isEmpty() || "MY_GEMINI_API_KEY".equals(cleApi.trim())) {
                        posterErreur(callback, "Clé API non configurée");
                        return;
                    }

                    String systemInstruction = context.getString(R.string.gemini_system_instruction);
                    JSONObject payload = construirePayload(systemInstruction, historique, messageActuel);

                    URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("x-goog-api-key", cleApi.trim());
                    connection.setConnectTimeout(25000);
                    connection.setReadTimeout(60000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setDoOutput(true);

                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(input, 0, input.length);
                    }

                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        String reponseJson = lireFlux(connection.getInputStream());
                        final String texte = extraireTexteReponse(reponseJson);
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

    private static JSONObject construirePayload(
            String systemInstruction,
            List<ChatMessage> historique,
            String messageActuel) throws Exception {

        JSONObject payload = new JSONObject();

        JSONObject sysObj = new JSONObject();
        JSONArray sysParts = new JSONArray();
        sysParts.put(new JSONObject().put("text", systemInstruction));
        sysObj.put("parts", sysParts);
        payload.put("system_instruction", sysObj);

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

        JSONObject genConfig = new JSONObject();
        genConfig.put("maxOutputTokens", 1024);
        genConfig.put("temperature", 0.3);
        payload.put("generationConfig", genConfig);

        return payload;
    }

    private static String extraireTexteReponse(String reponseJson) throws Exception {
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

    private static void posterErreur(final ReponseCallback callback, final String message) {
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(message);
            }
        });
    }

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
