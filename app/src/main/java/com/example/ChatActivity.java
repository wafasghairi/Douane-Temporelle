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

public class ChatActivity extends Activity {

    private static final String TAG = "GEMINI";

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

    private final List<ChatMessage> listeMessagesWs = new ArrayList<>();
    private ChatAdapter adaptateurChatWs;

    private final SimpleDateFormat formatHeure = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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

        adaptateurChatWs = new ChatAdapter(this, listeMessagesWs);
        lvMessagesWs.setAdapter(adaptateurChatWs);

        ajouterMessageBot(getString(R.string.chat_welcome));

        btnRetourChatWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEnvoyerWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texte = etMessageWs.getText().toString().trim();
                if (!texte.isEmpty()) {
                    traiterEnvoiMessageWs(texte);
                    etMessageWs.setText("");
                }
            }
        });

        chipFrWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterEnvoiMessageWs(getString(R.string.preset_greeting_fr));
            }
        });

        chipEnWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterEnvoiMessageWs(getString(R.string.preset_greeting_en));
            }
        });

        chipArWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterEnvoiMessageWs(getString(R.string.preset_greeting_ar));
            }
        });

        chipTnWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterEnvoiMessageWs(getString(R.string.preset_greeting_derja));
            }
        });
    }

    private void traiterEnvoiMessageWs(final String messageTexte) {
        String heureActuelle = formatHeure.format(new Date());
        listeMessagesWs.add(new ChatMessage(messageTexte, true, heureActuelle));
        adaptateurChatWs.notifyDataSetChanged();
        lvMessagesWs.smoothScrollToPosition(listeMessagesWs.size() - 1);

        btnEnvoyerWs.setEnabled(false);

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
                        ajouterMessageBot(reponseGemini);
                    }

                    @Override
                    public void onError(String messageErreur) {
                        btnEnvoyerWs.setEnabled(true);
                        Log.e(TAG, "Erreur de connexion IA: " + messageErreur);
                        Toast.makeText(ChatActivity.this, "Erreur IA: " + messageErreur, Toast.LENGTH_SHORT).show();
                        tvModeWs.setText("Erreur Connexion IA");
                        tvModeWs.setTextColor(getColor(R.color.couleur_refuse));
                        ajouterMessageBot("Service IA temporairement indisponible. Veuillez vérifier votre connexion.");
                    }
                }
        );
    }

    private void ajouterMessageBot(String texteReponse) {
        String heureActuelle = formatHeure.format(new Date());
        listeMessagesWs.add(new ChatMessage(texteReponse, false, heureActuelle));
        adaptateurChatWs.notifyDataSetChanged();
        lvMessagesWs.smoothScrollToPosition(listeMessagesWs.size() - 1);
    }
}
