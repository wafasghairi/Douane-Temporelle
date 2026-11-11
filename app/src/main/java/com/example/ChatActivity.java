package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


// Classe ChatActivity: L'activité principale mta3 el Chatbot IA
// Tgéri el interface graphique, el envoi w el isti9bel mta3 les messages m3a API Gemini

public class ChatActivity extends Activity {

    private static final String TAG = "GEMINI";

    // Déclarina les composants graphique mta3 l'interface XML
    private Button btnRetourChatWs;
    private TextView tvTitreChatWs;
    private TextView tvModeWs;
    private Button chipFrWs;
    private Button chipEnWs;
    private Button chipArWs;
    private Button chipTnWs;
    private ListView lvMessagesWs;
    private EditText etMessageWs;
    private Button btnEnvoyerWs;

    // Liste mta3 les messages mta3 el conversation w el adapter mte3ha
    private final List<ChatMessage> listeMessagesWs = new ArrayList<>();
    private ChatAdapter adaptateurChatWs;

    // Formatter bech na5dhou l'heure actuelle mta3 irsal el message (HH:mm)
    private final SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nrbtou l'activité bel fichier XML mte3ha "activity_chat"
        setContentView(R.layout.activity_chat);

        // Norbtou kol composant Java bel ID mte3ou eli fel XML
        btnRetourChatWs = findViewById(R.id.btn_retour_chat_ws);
        tvTitreChatWs = findViewById(R.id.tv_titre_chat_ws);
        tvModeWs = findViewById(R.id.tv_mode_ws);
        chipFrWs = findViewById(R.id.chip_fr_ws);
        chipEnWs = findViewById(R.id.chip_en_ws);
        chipArWs = findViewById(R.id.chip_ar_ws);
        chipTnWs = findViewById(R.id.chip_tn_ws);
        lvMessagesWs = findViewById(R.id.lv_messages_ws);
        etMessageWs = findViewById(R.id.et_message_ws);
        btnEnvoyerWs = findViewById(R.id.btn_envoyer_ws);

        // Initialisation mta3 el ChatAdapter w rabto bel ListView
        adaptateurChatWs = new ChatAdapter(this, listeMessagesWs);
        lvMessagesWs.setAdapter(adaptateurChatWs);

        // Naffichiw el message de bienvenue initial mta3 l'officier de douane IA
        ajouterMessageBot(getString(R.string.chat_welcome));

        // Bouton retour bech nsakrou el chat w narja3ou lel écran précèdent
        btnRetourChatWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Bouton envoyer bech nb3thou el message el sahraou l'API Gemini IA
        btnEnvoyerWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texte = etMessageWs.getText().toString().trim();
                if (!texte.isEmpty()) {
                    traiterEnvoiMessageWs(texte);
                    etMessageWs.setText(""); // N'vidiw el EditText ba3d el irsal
                }
            }
        });

        // Les chips rapides (Français, English, Arabe, Derja Tunisienne) bech n'testiw el langues b sehl
        chipFrWs.setOnClickListener(v -> traiterEnvoiMessageWs(getString(R.string.preset_greeting_fr)));
        chipEnWs.setOnClickListener(v -> traiterEnvoiMessageWs(getString(R.string.preset_greeting_en)));
        chipArWs.setOnClickListener(v -> traiterEnvoiMessageWs(getString(R.string.preset_greeting_ar)));
        chipTnWs.setOnClickListener(v -> traiterEnvoiMessageWs(getString(R.string.preset_greeting_derja)));
    }

    // Fonction bech ttraiti l'envoie mt3 el message w t3ayt l'GeminiClient
    private void traiterEnvoiMessageWs(final String messageTexte) {
        String heureActuelle = formatHeure.format(new Date());

        // 1. Nzidou message mta3 l'utilisateur lel list w n'refreshiw el adapter
        listeMessagesWs.add(new ChatMessage(messageTexte, true, heureActuelle));
        adaptateurChatWs.notifyDataSetChanged();
        lvMessagesWs.smoothScrollToPosition(listeMessagesWs.size() - 1);

        // Nblokiw el bouton envoyer lin terja3 el réponse mta3 l'IA
        btnEnvoyerWs.setEnabled(false);

        // 2. Nab3thou el message l'GeminiClient (Requête HTTP asynchrone fel background)
        GeminiClient.envoyerMessage(
                getApplicationContext(),
                listeMessagesWs,
                messageTexte,
                new GeminiClient.ReponseCallback() {
                    @Override
                    public void onSuccess(String reponseGemini) {
                        btnEnvoyerWs.setEnabled(true);
                        tvModeWs.setText(getString(R.string.chat_mode_online));
                        tvModeWs.setTextColor(getColor(R.color.couleur_approuve_clair));
                        // Nzidou réponse mta3 el Bot IA lel ListView
                        ajouterMessageBot(reponseGemini);
                    }

                    @Override
                    public void onError(String messageErreur) {
                        btnEnvoyerWs.setEnabled(true);
                        Log.e(TAG, "Erreur connexion Gemini IA: " + messageErreur);
                        Toast.makeText(ChatActivity.this, "Erreur IA: " + messageErreur, Toast.LENGTH_SHORT).show();
                        tvModeWs.setText("Erreur Connexion IA");
                        tvModeWs.setTextColor(getColor(R.color.couleur_refuse));
                        ajouterMessageBot("Service IA temporairement indisponible. Veuillez vérifier votre connexion.");
                    }
                }
        );
    }

    // Fonction bech nzidou message mta3 el bot lel ListView w nscroliw lel e5er message
    private void ajouterMessageBot(String texteReponse) {
        String heureActuelle = formatHeure.format(new Date());
        listeMessagesWs.add(new ChatMessage(texteReponse, false, heureActuelle));
        adaptateurChatWs.notifyDataSetChanged();
        lvMessagesWs.smoothScrollToPosition(listeMessagesWs.size() - 1);
    }
}
