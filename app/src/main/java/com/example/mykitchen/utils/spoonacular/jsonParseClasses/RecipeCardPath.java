package com.example.mykitchen.utils.spoonacular.jsonParseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeCardPath {

    @SerializedName("url")
    @Expose
    private String url;

    public RecipeCardPath() {
    }

    public RecipeCardPath(String url) {
        super();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}