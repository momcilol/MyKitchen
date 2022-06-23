package com.example.mykitchen.utils;

import static com.example.mykitchen.utils.spoonacular.SpoonacularInterface.API_KEY;
import static com.example.mykitchen.utils.spoonacular.SpoonacularInterface.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.example.mykitchen.database.RecipeDatabase;
import com.example.mykitchen.database.dao.RecipeDao;
import com.example.mykitchen.database.entity.Recipe;
import com.example.mykitchen.utils.spoonacular.SpoonacularInterface;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.ComplexSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.Joke;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RandomSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeCardPath;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeInformation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private final SpoonacularInterface spoonacularInterface;
    private final RecipeDao recipeDao;
    private List<Recipe> favouriteRecipes;
    private static RecipeRepository recipeRepository;

    private RecipeRepository(Context context) {
        RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(context);
        this.recipeDao = recipeDatabase.recipeDao();
        this.favouriteRecipes = new ArrayList<>();
        readFavourites();

        Log.i(TAG, "RecipeRepository: Created.");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spoonacularInterface = retrofit.create(SpoonacularInterface.class);
        Log.i(TAG, "RecipeRepository: Spoonacular Created");
    }

    public static RecipeRepository getInstance(Context context) {
        if (recipeRepository == null) {
            recipeRepository = new RecipeRepository(context);
        }
        Log.i(TAG, "getInstance: Instance returned");
        return recipeRepository;
    }

    public void readFavourites() {
        RecipeDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> list = recipeDao.getAll();
            if (list == null) {
                Log.i(TAG, "readFavourites: Empty List");
            } else {
                favouriteRecipes.addAll(list);
            }
        });
        Log.i(TAG, "readFavourites: ");
    }

    public void addFavourite(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> recipeDao.insert(recipe));
        favouriteRecipes.add(recipe);
        Log.i(TAG, "addFavourite: Added");
    }

    public void removeFavourite(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> recipeDao.delete(recipe));
        favouriteRecipes.remove(recipe);
        Log.i(TAG, "removeFavourite: Removed");
    }

    public List<Recipe> getFavouriteRecipes()
    {
        Log.i(TAG, "getFavouriteRecipes: ");
        return favouriteRecipes;
    }

    public boolean isInFavourites(Recipe recipe) {
        for (Recipe listRecipe : favouriteRecipes) {
            if(recipe.equals(listRecipe)) {
                return true;
            }
        }
        return false;
    }

    public void searchFood(String name, int number, Callback<ComplexSearch> callback) {
        spoonacularInterface.searchRecipeByName(name, number, API_KEY).enqueue(callback);
    }

    public void searchFood(String name, int number, String diet, Callback<ComplexSearch> callback) {
        spoonacularInterface.searchRecipeByName(name, number, diet, API_KEY).enqueue(callback);
    }
    public void findRecipeInformation(Recipe recipe, Callback<RecipeInformation> callback) {
        spoonacularInterface.getRecipeInformation(recipe.getId(), API_KEY).enqueue(callback);
    }

    public void findRecipeCard(Recipe recipe, Callback<RecipeCardPath> callback) {
        spoonacularInterface.getRecipeCard(recipe.getId(), API_KEY).enqueue(callback);
    }

    public void getFoodJoke(Callback<Joke> callback) {
        spoonacularInterface.getJoke(API_KEY).enqueue(callback);
    }

    public void searchRandomRecipes(int number, Callback<RandomSearch> callback) {
        Log.i(TAG, "searchRandomRecipes: ");
        spoonacularInterface.getRandomRecipes(number, API_KEY).enqueue(callback);
    }

    public void searchRandomRecipes(int number, String diet, Callback<RandomSearch> callback) {
        Log.i(TAG, "searchRandomRecipes: ");
        spoonacularInterface.getRandomRecipes(number, diet, API_KEY).enqueue(callback);
    }


}
