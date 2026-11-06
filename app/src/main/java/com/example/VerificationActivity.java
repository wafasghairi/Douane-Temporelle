package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VerificationActivity extends Activity {

    // Declarina les composants graphique ta3 l'écran de vérification du passeport temporel
    private ImageView ivPhotoPasseportWs;
    private TextView tvTitrePasseportWs;
    private TextView tvValeurNomWs;
    private TextView tvValeurTrajetWs;
    private TextView tvValeurAgeTemporelWs;
    private TextView tvValeurMotifWs;
    private TextView tvAvertissementParadoxeWs;
    private Button btnApprouverWs;
    private Button btnRefuserWs;

    // Les variables global eli bech nest7a9ouhom fel calcul w fel verification fel activity hedhi
    private String nomVoyageur;
    private int anneeNaissance;
    private int anneeDestination;
    private String motifVoyage;
    private int ageTemporel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Norbtou l'Activite hedhi bel fichier XML mte3ha "activity_verification"
        setContentView(R.layout.activity_verification);

        // Norbtou kol composant fel code Java b el ID mte3ou eli fel XML
        ivPhotoPasseportWs = findViewById(R.id.iv_photo_passeport_ws);
        tvTitrePasseportWs = findViewById(R.id.tv_titre_passeport_ws);
        tvValeurNomWs = findViewById(R.id.tv_valeur_nom_ws);
        tvValeurTrajetWs = findViewById(R.id.tv_valeur_trajet_ws);
        tvValeurAgeTemporelWs = findViewById(R.id.tv_valeur_age_temporel_ws);
        tvValeurMotifWs = findViewById(R.id.tv_valeur_motif_ws);
        tvAvertissementParadoxeWs = findViewById(R.id.tv_avertissement_paradoxe_ws);
        btnApprouverWs = findViewById(R.id.btn_approuver_ws);
        btnRefuserWs = findViewById(R.id.btn_refuser_ws);

        // Hne nakhdhou l'Intent eli b3aththou el MainActivity fih les données ta3 el voyageur
        Intent intentRecu = getIntent();
        if (intentRecu != null) {
            nomVoyageur = intentRecu.getStringExtra(Keys.cleNom(Keys.MATRICULE));
            anneeNaissance = intentRecu.getIntExtra(Keys.cleAnneeNaissance(Keys.MATRICULE), 0);
            anneeDestination = intentRecu.getIntExtra(Keys.cleAnneeDestination(Keys.MATRICULE), 0);
            motifVoyage = intentRecu.getStringExtra(Keys.cleMotif(Keys.MATRICULE));
        }

        // Verification safety: idha le nom wala motif null, n7otouh string vide bech maysirch crash
        if (nomVoyageur == null) {
            nomVoyageur = "";
        }
        if (motifVoyage == null) {
            motifVoyage = "";
        }
        // 1. Calcul ta3 l'âge temporel: el far9 bin annee destination w annee naissance
        ageTemporel = anneeDestination - anneeNaissance;

        // Affichage ta3 les informations fel UI (passeport)
        tvValeurNomWs.setText(nomVoyageur);
        tvValeurTrajetWs.setText(String.format(getString(R.string.format_trajet_temporel), anneeNaissance, anneeDestination));
        tvValeurAgeTemporelWs.setText(String.format(getString(R.string.format_age_temporel), ageTemporel));
        tvValeurMotifWs.setText(motifVoyage);

        // 2. Test ta3 el Paradoxe Temporel:
        // El condition t9oul: idha el motif houwa "Modification du passé" W el age temporel houwa nombre premier  -> fama paradoxe
        boolean isModificationPasse = "Modification du passé".equals(motifVoyage);
        boolean isNombrePremier = isPrime(ageTemporel);

        if (isModificationPasse && isNombrePremier) {
            // Ken fama paradoxe: nblokiw el bouton "Approuver" (maynajamch y9oulou oui) w naffichiw l'alerte
            btnApprouverWs.setEnabled(false);
            tvAvertissementParadoxeWs.setText(String.format(getString(R.string.alerte_paradoxe_premier), ageTemporel));
            tvAvertissementParadoxeWs.setVisibility(View.VISIBLE);
        } else {
            // Sinon (ma famech paradoxe): el bouton approuver y5dem 3adi w l'alerte tet5aba
            btnApprouverWs.setEnabled(true);
            tvAvertissementParadoxeWs.setVisibility(View.GONE);
        }

        // Écouteur d'événement 3al bouton "Approuver": ki yenzel 3lih, n3adiw verdict = true lel ResultActivity
        btnApprouverWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerVerdictWs(true);
            }
        });

        // Écouteur d'événement 3al bouton "Refuser": ki yenzel 3lih, n3adiw verdict = false lel ResultActivity
        btnRefuserWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerVerdictWs(false);
            }
        });
    }

    // Fonction privé bech tparati l'Intent w tab3ath el verdict, esm el voyageur w l'age lel ResultActivity
    private void lancerVerdictWs(boolean verdict) {
        Intent intent = new Intent(VerificationActivity.this, ResultActivity.class);
        intent.putExtra(Keys.cleVerdict(Keys.MATRICULE), verdict);
        intent.putExtra(Keys.cleNom(Keys.MATRICULE), nomVoyageur);
        intent.putExtra(Keys.cleAgeTemporel(Keys.MATRICULE), ageTemporel);
        startActivity(intent);
    }

    // Fonction statique bech tverifi ken un nombre houwa nombre premier walla la (sta3maltha lel paradoxe)
    // El nombre premier houwa adad akbar min 1 w y9bel el9isma ella 3la rou7ou w 3la 1 barka.
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false; // 0 w 1 mouch nombres premiers
        }
        if (n == 2) {
            return true;  // 2 nombre premier zeda
        }
        if (n % 2 == 0) {
            return false; // Les nombres pairs (excepté 2) mouch nombres premiers
        }
        // ntestiw el9isma 3la les nombres impairs mel 3 (i * i <= n)
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false; // 9bel el9isma 3la 3adad e5er, donc mouch premier
            }
        }
        return true; // Ken t3ada el boucle lkol w ma 9belch el9isma, donc nombre premier
    }
}
