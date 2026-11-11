package com.example;

// Déclarina classe publique esmha Keys mta3 les clés Intent
public final class Keys {

    // Matricules mta3 les membres du groupe (Wafa Sghairi & Emna Benwhat Ali)
    public static final String MATRICULE_WAFA = "1964";
    public static final String MATRICULE_EMNA = "3499";
    public static final String MATRICULE = MATRICULE_WAFA; // Matricule par défaut pour générer les clés

    private Keys() {}

    //fonction bech ngénériw clé mta3 ism l'voyageur
    public static String cleNom(String matricule) {
        return matricule + "_NOM_VOYAGEUR";
    }

    //fonction bech ngénériw clé mta3 année de naissance ta3 l'voyageur
    public static String cleAnneeNaissance(String matricule) {
        return matricule + "_ANNEE_NAISSANCE";
    }

    //fonction bech ngénériw clé mta3 année destination ta3 l'voyageur
    public static String cleAnneeDestination(String matricule) {
        return matricule + "_ANNEE_DESTINATION";
    }

    //fonction bech ngénériw clé mta3 motif de voyage
    public static String cleMotif(String matricule) {
        return matricule + "_MOTIF_VOYAGE";
    }

    //fonction bech ngénériw clé mta3 verdict final (accepté wala refusé)
    public static String cleVerdict(String matricule) {
        return matricule + "_VERDICT";
    }

    //fonction bech ngénériw clé mta3 l'âge temporel calculé
    public static String cleAgeTemporel(String matricule) {
        return matricule + "_AGE_TEMPOREL";
    }
}
