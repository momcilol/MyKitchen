package com.example.mykitchen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mykitchen.R;
import com.example.mykitchen.database.entity.Recipe;
import com.example.mykitchen.fragments.DetailsFragment;
import com.example.mykitchen.fragments.RecipeCardFragment;
import com.example.mykitchen.fragments.RecipesFragment;
import com.example.mykitchen.utils.RecipeRepository;
import com.example.mykitchen.utils.spoonacular.Transformator;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeCardPath;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesActivity extends AppCompatActivity implements RecipesFragment.RecipeFragmentInterface,
                                                                     DetailsFragment.OnShowRecipeCard,
                                                                     DetailsFragment.OnFavouritesChanged {

    private static final String TAG = "FavouritesActivity";

    private RecipesFragment recipesFragment;
    private DetailsFragment detailsFragment;
    private RecipeCardFragment recipeCardFragment;

    ActionBar actionBar;

    RecipeRepository recipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ButterKnife.bind(this);
        Log.i(TAG, "onCreate: Binded");

        recipeRepository = RecipeRepository.getInstance(getApplicationContext());

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.favourite_recipes);

        recipesFragment = new RecipesFragment();
        detailsFragment = new DetailsFragment();
        recipeCardFragment = new RecipeCardFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!isTwoPaneView()) {
            transaction.add(R.id.af_frame, recipesFragment);
        } else {
            transaction.add(R.id.af_left, recipesFragment);
        }
        transaction.commit();

        Log.i(TAG, "onCreate: FavouriteRecipes");
        recipesFragment.setRecipeList(recipeRepository.getFavouriteRecipes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.getItem(1).setVisible(false);
        menu.getItem(4).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search_recipes_0: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.app_bar_settings_3: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.app_bar_map_2: {
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;
            }
            default: return false;
        }
    }

    @Override
    public void showRecipeCard(Recipe recipe) {
        Log.i(TAG, "showRecipeCard: ");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneView()) {
            transaction.replace(R.id.af_frame, recipeCardFragment);
        } else {
            transaction.replace(R.id.af_right, recipeCardFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
        if (recipe.getRecipeCardPath() == null) {
            recipeRepository.findRecipeCard(recipe, findRecipeCardCallback(recipe));
        } else {
            recipeCardFragment.setImagePath(recipe.getRecipeCardPath());
        }
    }

    public Callback<RecipeCardPath> findRecipeCardCallback(Recipe recipe) {
        return new Callback<RecipeCardPath>() {
            @Override
            public void onResponse(@NonNull Call<RecipeCardPath> call, @NonNull Response<RecipeCardPath> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "Responded recipe information.");
                    RecipeCardPath recipeCardPath = response.body();
                    Transformator.fillDetails(recipe, recipeCardPath);
                    Log.i(TAG, recipe.getRecipeCardPath());
                    recipeCardFragment.setImagePath(recipe.getRecipeCardPath());
                }
            }

            @Override
            public void onFailure(Call<RecipeCardPath> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        };
    }

    @Override
    public void showDetailsFragment(Recipe recipe) {
        Log.i(TAG, "showDetailsFragment: ");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneView()) {
            transaction.replace(R.id.af_frame, detailsFragment);
        } else {
            transaction.replace(R.id.af_right, detailsFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
        detailsFragment.setRecipe(recipe);
    }

    @Override
    public void refreshFavourites() {
        Log.i(TAG, "refreshFavourites: ");
        recipesFragment.setRecipeList(recipeRepository.getFavouriteRecipes());
    }

    private boolean isTwoPaneView(){
        return findViewById(R.id.af_frame) == null;
    }

}