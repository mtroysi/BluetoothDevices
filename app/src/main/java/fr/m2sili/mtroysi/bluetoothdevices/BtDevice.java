package fr.m2sili.mtroysi.bluetoothdevices;

/**
 * Created by android on 11/30/16.
 */

public class BtDevice {
    private String nom;
    private int classe;
    private String adresse;
    private int state;

    public BtDevice(String nom, int classe, String adresse, int state) {
        this.nom = nom;
        this.classe = classe;
        this.adresse = adresse;
        this.state = state;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
