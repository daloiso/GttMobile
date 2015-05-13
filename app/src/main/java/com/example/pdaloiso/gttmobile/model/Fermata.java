package com.example.pdaloiso.gttmobile.model;

/**
 * Created by pdaloiso on 13/05/2015.
 */
public class Fermata extends Punto{

    private String indicazioneStradale;
    private String altreIndicazioni;
    private boolean banchina;
    private Integer idFermata;

    public String getIndicazioneStradale() {
        return indicazioneStradale;
    }

    public void setIndicazioneStradale(String indicazioneStradale) {
        this.indicazioneStradale = indicazioneStradale;
    }

    public String getAltreIndicazioni() {
        return altreIndicazioni;
    }

    public void setAltreIndicazioni(String altreIndicazioni) {
        this.altreIndicazioni = altreIndicazioni;
    }

    public boolean isBanchina() {
        return banchina;
    }

    public void setBanchina(boolean banchina) {
        this.banchina = banchina;
    }

    public Integer getIdFermata() {
        return idFermata;
    }

    public void setIdFermata(Integer idFermata) {
        this.idFermata = idFermata;
    }
}
