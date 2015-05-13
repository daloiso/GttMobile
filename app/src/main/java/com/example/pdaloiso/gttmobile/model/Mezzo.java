package com.example.pdaloiso.gttmobile.model;

/**
 * Created by pdaloiso on 13/05/2015.
 */
public class Mezzo {


    private int id;
    private boolean avvisoFermate;
    private boolean pedane;
    private boolean displayFermate;
    //lista di percorsi

    public Mezzo() {
    }

    public boolean isAvvisoFermate() {
        return avvisoFermate;
    }

    public void setAvvisoFermate(boolean avvisoFermate) {
        this.avvisoFermate = avvisoFermate;
    }

    public boolean isPedane() {
        return pedane;
    }

    public void setPedane(boolean pedane) {
        this.pedane = pedane;
    }

    public boolean isDisplayFermate() {
        return displayFermate;
    }

    public void setDisplayFermate(boolean displayFermate) {
        this.displayFermate = displayFermate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Mezzo{" +
                "id=" + id +
                ", avvisoFermate=" + avvisoFermate +
                ", pedane=" + pedane +
                ", displayFermate=" + displayFermate +
                '}';
    }
}
