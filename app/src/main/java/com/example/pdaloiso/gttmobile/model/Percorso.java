package com.example.pdaloiso.gttmobile.model;

import java.util.List;

/**
 * Created by pdaloiso on 13/05/2015.
 */
public class Percorso {

    private List<Fermata> fermate;
    //lista di mezzi

    public List<Fermata> getFermate() {
        return fermate;
    }

    public void setFermate(List<Fermata> fermate) {
        this.fermate = fermate;
    }

}
