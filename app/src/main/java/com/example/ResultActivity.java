package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

public class ResultActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "TTS";

    private View rootLayout;
    private TextView tvStampVerdict;
    private TextView tvResultat;
    private TextView tvDetailsVerdict;
    private Button btnNouveauVoyageur;

    private TextToSpeech tts;
    private CountDownTimer timerSaut;
    private boolean verdictApprouve = false;
    private String nomVoyageur = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        rootLayout = findViewById(R.id.ll_root_resultat_ws);
        tvStampVerdict = findViewById(R.id.tv_stamp_verdict_ws);
        tvResultat = findViewById(R.id.tv_resultat_ws);
        tvDetailsVerdict = findViewById(R.id.tv_details_verdict_ws);
        btnNouveauVoyageur = findViewById(R.id.btn_nouveau_voyageur_ws);

        Intent intent = getIntent();
        int ageTemporel = 0;

        if (intent != null) {
            verdictApprouve = intent.getBooleanExtra(Keys.cleVerdict(Keys.MATRICULE), false);
            nomVoyageur = intent.getStringExtra(Keys.cleNom(Keys.MATRICULE));
            ageTemporel = intent.getIntExtra(Keys.cleAgeTemporel(Keys.MATRICULE), 0);
        }

        if (nomVoyageur == null) {
            nomVoyageur = "";
        }

        tts = new TextToSpeech(this, this);

        if (verdictApprouve) {
            rootLayout.setBackgroundColor(getColor(R.color.couleur_approuve));
            tvStampVerdict.setText(R.string.stamp_approuve);
            tvStampVerdict.setBackgroundResource(R.drawable.bg_stamp_approved);

            // Option 1: CountDownTimer mta3 3 secondes pour le décollage temporel
            timerSaut = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secondes = (millisUntilFinished / 1000) + 1;
                    tvResultat.setText("Décollage temporel dans " + secondes + "s...\nBon voyage " + nomVoyageur);
                }

                @Override
                public void onFinish() {
                    tvResultat.setText(String.format(getString(R.string.message_bon_voyage), nomVoyageur));
                }
            };
            timerSaut.start();

        } else {
            rootLayout.setBackgroundColor(getColor(R.color.couleur_refuse));
            tvResultat.setText(String.format(getString(R.string.message_arrestation), nomVoyageur));
            tvStampVerdict.setText(R.string.stamp_refuse);
            tvStampVerdict.setBackgroundResource(R.drawable.bg_stamp_rejected);
        }

        tvDetailsVerdict.setText(String.format(getString(R.string.details_age_verdict), ageTemporel));

        btnNouveauVoyageur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetour = new Intent(ResultActivity.this, MainActivity.class);
                intentRetour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentRetour);
                finish();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.FRENCH);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                String messageVocal;
                if (verdictApprouve) {
                    messageVocal = "Bon voyage " + nomVoyageur;
                } else {
                    messageVocal = "Attention ! " + nomVoyageur + " est en état d'arrestation temporelle !";
                }
                tts.speak(messageVocal, TextToSpeech.QUEUE_FLUSH, null, "VERDICT_TTS");
            }
        } else {
            Log.e(TAG, "Erreur initialisation TextToSpeech");
        }
    }

    @Override
    protected void onDestroy() {
        if (timerSaut != null) {
            timerSaut.cancel();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
