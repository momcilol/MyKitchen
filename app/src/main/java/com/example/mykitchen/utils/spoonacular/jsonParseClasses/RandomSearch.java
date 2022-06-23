package com.example.mykitchen.utils.spoonacular.jsonParseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomSearch {

    @SerializedName("recipes")
    @Expose
    private List<RecipeInfo> recipes = null;

    public RandomSearch() {
    }

    public RandomSearch(List<RecipeInfo> recipes) {
        super();
        this.recipes = recipes;
    }

    public List<RecipeInfo> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeInfo> recipes) {
        this.recipes = recipes;
    }

}





