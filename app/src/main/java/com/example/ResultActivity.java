package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

    // Hne déclaréna les composants mte3 l'interface (les vues eli fel XML)
    // rootLayout: l'arrière-plan ili bech nbadlou couleur mte3ou (vert wala rouge)
    private View rootLayout;
    // tvStampVerdict: el tampon (stamp) ili yetektib fih "MAQBOUL" wala "MARFOUD"
    private TextView tvStampVerdict;
    // tvResultat: el message elli yaffichi esm el voyageur w résultat ta3 l'inspection
    private TextView tvResultat;
    // tvDetailsVerdict: el message eli yaffichi l'age temporel w les détails
    private TextView tvDetailsVerdict;
    // btnNouveauVoyageur: el bouton eli yrod l'utilisateur lel page d'accueil (MainActivity)
    private Button btnNouveauVoyageur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nrbtou l'Activity hedhi bel fichier XML mte3ha "activity_result"
        setContentView(R.layout.activity_result);

        // Animation d'apparition smooth (fade in w fade out) ki t7el l'Activity
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Norbtou kol variable fel Java bel ID mte3ha fel XML
        rootLayout = findViewById(R.id.ll_root_resultat_ws);
        tvStampVerdict = findViewById(R.id.tv_stamp_verdict_ws);
        tvResultat = findViewById(R.id.tv_resultat_ws);
        tvDetailsVerdict = findViewById(R.id.tv_details_verdict_ws);
        btnNouveauVoyageur = findViewById(R.id.btn_nouveau_voyageur_ws);

        // Hne ne5dhou l'Intent eli ba3thetou l'Activity elli 9abalha (VerificationActivity)
        // W  nrecuperiw el valeur ta3 el verdict (true/false), esm el voyageur, w l'age temporel
        Intent intent = getIntent();
        boolean verdictApprouve = intent != null && intent.getBooleanExtra(Keys.cleVerdict(Keys.MATRICULE), false);
        String nomVoyageur = intent != null ? intent.getStringExtra(Keys.cleNom(Keys.MATRICULE)) : "";
        int ageTemporel = intent != null ? intent.getIntExtra(Keys.cleAgeTemporel(Keys.MATRICULE), 0) : 0;

        // Security check: idha esm el voyageur feragh (null), n7ottouha string vide bech ma tsirch crash (NullPointerException)
        if (nomVoyageur == null) {
            nomVoyageur = "";
        }

        // Test 3al verdict: idha el voyage maqboul (true) walla marefoud (false)
        if (verdictApprouve) {
            // --- CAS 1: VOYAGE APPROUVÉ (MAQBOUL) ---
            // 1. Nbadlou couleur el background b el a5dher (couleur_approuve)
            rootLayout.setBackgroundColor(getColor(R.color.couleur_approuve));
            // 2. N7ottou message bon voyage fel TextView mte3 el résultat
            tvResultat.setText(String.format(getString(R.string.message_bon_voyage), nomVoyageur));
            // 3. Nbadlou texte w background ta3 el tampon (stamp) b "MAQBOUL" w style approuvé
            tvStampVerdict.setText(R.string.stamp_approuve);
            tvStampVerdict.setBackgroundResource(R.drawable.bg_stamp_approved);
        } else {
            // --- CAS 2: VOYAGE REFUSÉ / ARRESTATION ---
            // 1. Nbadlou couleur l'background b el a7mer (couleur_refuse)
            rootLayout.setBackgroundColor(getColor(R.color.couleur_refuse));
            // 2. N7ottou message mte3 arrestation w rafadh fel TextView
            tvResultat.setText(String.format(getString(R.string.message_arrestation), nomVoyageur));
            // 3. Nbadlou el stamp b "MAREFOUD" w style mte3 el rafedh
            tvStampVerdict.setText(R.string.stamp_refuse);
            tvStampVerdict.setBackgroundResource(R.drawable.bg_stamp_rejected);
        }

        // Naffichiw l'age temporel el 7sebna fel details (exemple: Far9 el zamen)
        tvDetailsVerdict.setText(String.format(getString(R.string.details_age_verdict), ageTemporel));

        // Listener 3al bouton "Nouveau Voyageur" bech ki l'utilisateur yenzel 3lih:
        // Nraj3ouh lel MainActivity, w nfas5ou l'historique kol (FLAG_ACTIVITY_CLEAR_TOP) bech ma yarja3ch lel résultat lakdima.
        btnNouveauVoyageur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRetour = new Intent(ResultActivity.this, MainActivity.class);
                intentRetour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentRetour);
                finish(); // Nsakrou ResultActivity
            }
        });
    }
}
