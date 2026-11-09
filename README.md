# Mini Projet (DS 1) : Douane Temporelle (Time Travel Customs)

Projet réalisé dans le cadre de la matière **Développement Mobile** (3ème BI / FSEGT — Enseignant : M. Nidhal Jelassi).

---

## Membres du Groupe

- **Wafa Sghairi** (Email : `wafa.sghairi@etudiant-fsegt.utm.tn` | GitHub : [@wafasghairi](https://github.com/wafasghairi))
- **Emna Ben Ali** (Email : `emna.benali@etudiant-fsegt.utm.tn` | GitHub : [@BENALIEMNA](https://github.com/BENALIEMNA))

---

## Description du Projet

L'application simule un poste de contrôle douanier pour les voyageurs temporels. Elle vérifie la conformité des sauts temporels et applique les règles de sécurité du Protocole 1964.

---

## Structure de l'Application

1. **MainActivity (Formulaire d'enregistrement) :**
   - Saisie du nom du voyageur, de son année de naissance et de l'année de destination.
   - Sélection du motif de voyage (Tourisme, Modification du passé, Fuite).
   - Validation des champs obligatoires et passage à l'activité suivante via Intent.

2. **VerificationActivity (Scanner de paradoxe) :**
   - Disposition en `LinearLayout` horizontal (poids 0.3 pour la photo et 0.7 pour le passeport).
   - **Règle du Protocole 1964** : Si le motif est 'Modification du passé' ET que l'écart temporel est un nombre premier, le bouton 'Approuver' est automatiquement désactivé dès le chargement.

3. **ResultActivity (Verdict) :**
   - Fond vert si le saut est approuvé, fond rouge si le saut est refusé.
   - Message officiel et bouton 'Nouveau voyageur' pour revenir à l'accueil.

4. **ChatActivity (Fonctionnalité Bonus - Chatbot IA) :**
   - Assistant virtuel en ligne connecté à l'API Gemini.
   - Support du français, de l'anglais, de l'arabe et de la derja tunisienne (en arabizi et alphabet arabe).

---

## Instructions Système du Chatbot (Prompt Engineering)

> "Vous êtes l'agent IA officiel de la Douane Temporelle, un officier strict, poli et professionnel chargé de contrôler les autorisations de saut temporel selon le protocole 1964.
> Répondez de manière courte et concise dans la même langue et le même alphabet que le dernier message de l'utilisateur (français, anglais, arabe ou derja tunisienne).
> Si le motif est 'Modification du passé' et que l'écart temporel est un nombre premier, refusez strictement le saut."

---

## Contraintes du Cahier des Charges Respectées

- Code écrit en Java natif sans bibliothèques externes.
- Identifiants de vues XML suffixés par `_ws`.
- Commentaires dans le code en dialecte tunisien avec chiffres 0-9.
- Clés Intent générées via la classe `Keys` préfixées par le matricule étudiant (`1964` / `3499`).

---

## 🔑 Configuration de la Clé API Gemini & Exécution

Afin de pouvoir utiliser le Chatbot IA en ligne, veuillez suivre ces étapes simples :

1. Générer une clé API gratuite sur [Google AI Studio](https://aistudio.google.com/).
2. Créer un fichier nommé `.env` à la racine du projet.
3. Ajouter votre clé dans le fichier `.env` :
   ```properties
   GEMINI_API_KEY=votre_cle_gemini_ici
   ```
4. Ouvrir le projet dans **Android Studio** et lancer l'application sur un émulateur ou téléphone.

> **Note de Sécurité :** Le fichier `.env` est exclu du dépôt Git (`.gitignore`) pour protéger votre clé API personnelle.
