package com.example.mykitchen.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mykitchen.R;
import com.example.mykitchen.utils.RecipeRepository;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.Joke;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JokeActivity extends AppCompatActivity {

    private static final String TAG = "JokeActivity";

    TextView textView;

    ActionBar actionBar;

    RecipeRepository recipeRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        textView = findViewById(R.id.textViewJokeText);

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_activity_joke);

        recipeRepository = RecipeRepository.getInstance(getApplicationContext());

        recipeRepository.getFoodJoke(new Callback<Joke>() {
            @Override
            public void onResponse(Call<Joke> call, Response<Joke> response) {
                textView.setText(response.body().getText());
            }

            @Override
            public void onFailure(Call<Joke> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

}
