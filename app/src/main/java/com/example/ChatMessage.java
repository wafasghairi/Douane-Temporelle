package com.example;

public class ChatMessage {
    private final String texte;
    private final boolean isUser;
    private final String heure;

    public ChatMessage(String texte, boolean isUser, String heure) {
        this.texte = texte;
        this.isUser = isUser;
        this.heure = heure;
    }

    public String getTexte() {
        return texte;
    }

    public boolean isUser() {
        return isUser;
    }

    public String getHeure() {
        return heure;
    }
}
