package com.example.mykitchen.utils.spoonacular;

import android.util.Log;

import com.example.mykitchen.database.entity.Recipe;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RandomSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeCardPath;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeInfo;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeInformation;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.Result;

import java.util.ArrayList;
import java.util.List;

public class Transformator {

    private static final String TAG = "Transformator";

    public static Recipe result2recipe(Result result) {
        Recipe recipe = new Recipe();
        recipe.setId(result.getId());
        recipe.setTitle(result.getTitle());
        recipe.setImagePath(result.getImage());
        return recipe;
    }

    public static Recipe random2recipe(RecipeInfo recipeInfo) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeInfo.getId());
        recipe.setTitle(recipeInfo.getTitle());
        recipe.setImagePath(recipeInfo.getImage());
        recipe.setRating(recipeInfo.getSpoonacularScore());
        recipe.setSourceUrl(recipeInfo.getSourceUrl());
        recipe.setSummary(recipeInfo.getSummary());
        recipe.setFavourite(false);
        Log.i(TAG, "random2recipe: ");
        return recipe;
    }

    public static List<Recipe> randomSearch2recipeList(RandomSearch randomSearch) {
        Log.i(TAG, "randomSearch2recipeList: ");
        List<Recipe> recipeList = new ArrayList<>();
        List<RecipeInfo> recipeInfoList = randomSearch.getRecipes();
        if (recipeInfoList == null || recipeInfoList.size() == 0) {
            Log.i(TAG, "randomSearch2recipeList: EMPTY LIST");
        }
        for (RecipeInfo recipeInfo: recipeInfoList) {
            Log.i(TAG, "randomSearch2recipeList: " + recipeInfo.getTitle());
            recipeList.add(random2recipe(recipeInfo));
        }
        return recipeList;
    }

    public static List<Recipe> resultList2recipeList(List<Result> resultList) {
        List<Recipe> recipeList = new ArrayList<>();
        for (Result result: resultList) {
            recipeList.add(result2recipe(result));
        }
        return recipeList;
    }

    public static void fillDetails(Recipe recipe, RecipeInformation recipeInformation) {
        recipe.setRating(recipeInformation.getSpoonacularScore());
        recipe.setSummary(recipeInformation.getSummary());
        recipe.setSourceUrl(recipeInformation.getSourceUrl());
    }

    public static void fillDetails(Recipe recipe, RecipeCardPath recipeCardPath) {
        recipe.setRecipeCardPath(recipeCardPath.getUrl());
    }
}
