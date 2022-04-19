package com.example.tp3webservices.modele;

public class Utilisateur {
    private String login;
    private String motDePasse;
    private int idUtilisateur;
    private static int IDS=0;

    public Utilisateur(String login, String motDePasse, int idUtilisateur) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.idUtilisateur = IDS++;
    }

    public String getLogin() {
        return login;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public boolean verifierMotDePasse(String motDePasse){
        return this.motDePasse.equals(motDePasse);
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }
}
