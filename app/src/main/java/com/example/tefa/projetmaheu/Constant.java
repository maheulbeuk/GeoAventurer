package com.example.tefa.projetmaheu;

/**
 * Created by Tefa on 11/10/2016.
 */

public class Constant {

    private String identifiant;
    private int xp;
    private int xpLevel;
    private int level;
    private int qtLevel;
    private int niveau[][];
    private int qFini;
    private int totQuest;

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void setXpLevel(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getQtLevel() {
        return qtLevel;
    }

    public void setQtLevel(int qtLevel) {
        this.qtLevel = qtLevel;
    }

    public int[][] getNiveau() {
        return niveau;
    }

    public void setNiveau(int[][] niveau) {
        this.niveau = niveau;
    }

    public int getqFini() {
        return qFini;
    }

    public void setqFini(int qFini) {
        this.qFini = qFini;
    }

    public int getTotQuest() {
        return totQuest;
    }

    public void setTotQuest(int totQuest) {
        this.totQuest = totQuest;
    }
}
