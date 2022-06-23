package com.example.mykitchen.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.mykitchen.R;
import com.example.mykitchen.database.entity.Recipe;
import com.example.mykitchen.fragments.DetailsFragment;
import com.example.mykitchen.fragments.RecipeCardFragment;
import com.example.mykitchen.fragments.RecipesFragment;
import com.example.mykitchen.utils.RecipeRepository;
import com.example.mykitchen.utils.spoonacular.Transformator;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.ComplexSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RandomSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeCardPath;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeInformation;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipesFragment.RecipeFragmentInterface,
                                                               DetailsFragment.OnShowRecipeCard,
                                                               DetailsFragment.OnFavouritesChanged,
                                                               SensorEventListener {


    static final String TAG = "MainActivity";

    private RecipesFragment recipesFragment;
    private DetailsFragment detailsFragment;
    private RecipeCardFragment recipeCardFragment;

    ActionBar actionBar;
    SearchView searchView;

    private List<Recipe> recipeList;

    private RecipeRepository recipeRepository;
    private SharedPreferences sharedPreferences;

    private int number;
    private String diet;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean sensorAvailable, isFirstTime = true;

    private double currX, currY, currZ, prevX, prevY, prevZ;
    private double deltaX, deltaY, deltaZ;
    private static final double shakeThreshold = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Log.i(TAG, "Just binded.");

        recipeList = new ArrayList<>();

//        Set Action Bar

        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(R.string.search_recipes);


//        Create and set Fragments

        recipesFragment = new RecipesFragment();
        detailsFragment = new DetailsFragment();
        recipeCardFragment = new RecipeCardFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!isTwoPaneView()) {
            transaction.add(R.id.am_frame, recipesFragment);
        } else {
            transaction.add(R.id.am_left_frame, recipesFragment);
        }
        transaction.commit();
        Log.i(TAG, "onCreate: ");

//        Open repository

        recipeRepository = RecipeRepository.getInstance(getApplicationContext());


//        Set search preferences

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        number = sharedPreferences.getInt(getResources().getString(R.string.number_of_search_results_key), getResources().getInteger(R.integer.default_number_of_search_results));
        diet = sharedPreferences.getString(getResources().getString(R.string.diet_preference_key), "None");

//        Set shake Listener

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.i(TAG, "onCreate: Sensor created");
            Toast.makeText(this, "Accelerometer available", Toast.LENGTH_LONG).show();
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorAvailable = true;
        } else {
            Toast.makeText(this, "Accelerometer is not available", Toast.LENGTH_LONG).show();
            sensorAvailable = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorAvailable) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorAvailable) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setVisible(false);
        searchView = (SearchView) menu.getItem(4).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (diet == null || diet.equalsIgnoreCase("None")) {
                    recipeRepository.searchFood(s, number, searchFoodCallback());
                } else {
                    recipeRepository.searchFood(s, number, diet, searchFoodCallback());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_favourites_1: {
                Intent intent = new Intent(this, FavouritesActivity.class);
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

    public Callback<ComplexSearch> searchFoodCallback() {
        return  new Callback<ComplexSearch>() {
            @Override
            public void onResponse(@NonNull Call<ComplexSearch> call, @NonNull Response<ComplexSearch> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "Responded search food.");
                    List<Result> listaRezultata = Objects.requireNonNull(response.body()).getResults();
                    recipeList = Transformator.resultList2recipeList(listaRezultata);
                    for (Recipe recipe : recipeList) {
                        Log.i(TAG, "onResponse: " + recipe);
                    }
                    recipesFragment.setRecipeList(recipeList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ComplexSearch> call, @NonNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        };
    }

    @Override
    public void showDetailsFragment(Recipe recipe) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneView()) {
            transaction.replace(R.id.am_frame, detailsFragment);

        } else {
            transaction.replace(R.id.am_right_frame, detailsFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        getFragmentManager().executePendingTransactions();

        if (recipe.hasDetails()) {
            detailsFragment.setRecipe(recipe);
        } else {
            recipeRepository.findRecipeInformation(recipe, findRecipeInformationCallback(recipe));
        }
    }

    public void showRecipesFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneView()) {
            transaction.replace(R.id.am_frame, recipesFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    public Callback<RecipeInformation> findRecipeInformationCallback(Recipe recipe) {
        return new Callback<RecipeInformation>() {
            @Override
            public void onResponse(@NonNull Call<RecipeInformation> call, @NonNull Response<RecipeInformation> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "Responded recipe information.");
                    RecipeInformation recipeInformation = response.body();
                    Transformator.fillDetails(recipe, recipeInformation);
                    Log.i(TAG, recipe.getImagePath());
                    Log.i(TAG, recipe.getSummary());
                    Log.i(TAG, recipe.getSourceUrl());
                    detailsFragment.setRecipe(recipe);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeInformation> call, @NonNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        };
    }

    @Override
    public void showRecipeCard(Recipe recipe) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneView()) {
            transaction.replace(R.id.am_frame, recipeCardFragment);
        } else {
            transaction.replace(R.id.am_right_frame, recipeCardFragment);
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

    public Callback<RandomSearch> searchRandomCallback() {
        return new Callback<RandomSearch>() {
            @Override
            public void onResponse(@NonNull Call<RandomSearch> call, @NonNull Response<RandomSearch> response) {
                if (response.code() == 200) {
                    Log.i(TAG, "Responded search random callback.");
                    RandomSearch randomSearch = response.body();
                    recipeList = Transformator.randomSearch2recipeList(randomSearch);
                    if (recipeList == null || recipeList.size() == 0){
                        Log.i(TAG, "onResponse: EMPTY");
                    } else {
                        showRecipesFragment();
                        for (Recipe recipe : recipeList) {
                            Log.i(TAG, "onResponse: " + recipe);
                        }
                        recipesFragment.setRecipeList(recipeList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RandomSearch> call, @NonNull Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        };
    }

    private boolean isTwoPaneView(){
        return findViewById(R.id.am_frame) == null;
    }


    @Override
    public void refreshFavourites() {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currX = sensorEvent.values[0];
        currY = sensorEvent.values[1];
        currZ = sensorEvent.values[2];

        if(!isFirstTime) {
            deltaX = Math.abs(prevX - currX);
            deltaY = Math.abs(prevY - currY);
            deltaZ = Math.abs(prevZ - currZ);

            if (Math.sqrt(deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ) > shakeThreshold){

                Log.i(TAG, "onSensorChanged: SENSOR CHANGED");

                if (diet == null || diet.equalsIgnoreCase("None")) {
                    Log.i(TAG, "onSensorChanged: Searching random food WITH diet");
                    recipeRepository.searchRandomRecipes(number, searchRandomCallback());
                } else {
                    Log.i(TAG, "onSensorChanged: Searching random food WITHOUT diet");
                    recipeRepository.searchRandomRecipes(number, diet, searchRandomCallback());
                }
            }
        }

        prevX = currX;
        prevY = currY;
        prevZ = currZ;
        isFirstTime = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}