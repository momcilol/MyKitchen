package com.example.mykitchen.utils.spoonacular.jsonParseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinePairing {

    @SerializedName("pairedWines")
    @Expose
    private List<Object> pairedWines = null;
    @SerializedName("pairingText")
    @Expose
    private String pairingText;
    @SerializedName("productMatches")
    @Expose
    private List<Object> productMatches = null;


    public WinePairing() {
    }


    public WinePairing(List<Object> pairedWines, String pairingText, List<Object> productMatches) {
        super();
        this.pairedWines = pairedWines;
        this.pairingText = pairingText;
        this.productMatches = productMatches;
    }

    public List<Object> getPairedWines() {
        return pairedWines;
    }

    public void setPairedWines(List<Object> pairedWines) {
        this.pairedWines = pairedWines;
    }

    public String getPairingText() {
        return pairingText;
    }

    public void setPairingText(String pairingText) {
        this.pairingText = pairingText;
    }

    public List<Object> getProductMatches() {
        return productMatches;
    }

    public void setProductMatches(List<Object> productMatches) {
        this.productMatches = productMatches;
    }

}