package com.example.mykitchen.utils.spoonacular;

import com.example.mykitchen.utils.spoonacular.jsonParseClasses.ComplexSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.Joke;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RandomSearch;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeCardPath;
import com.example.mykitchen.utils.spoonacular.jsonParseClasses.RecipeInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularInterface {

    static final String API_KEY = "266b48eccb9a411698faa98d94ec1167";
    static final String BASE_URL = "https://api.spoonacular.com/";

    @GET("recipes/complexSearch")
    Call<ComplexSearch> searchRecipeByName(
            @Query("query") String name,
            @Query("number") Integer number,
            @Query("apiKey") String apiKey);

    @GET("recipes/complexSearch")
    Call<ComplexSearch> searchRecipeByName(
            @Query("query") String name,
            @Query("number") Integer number,
            @Query("diet") String diet,
            @Query("apiKey") String apiKey);

    @GET("recipes/{id}/information?includeNutrition=false")
    Call<RecipeInformation> getRecipeInformation(
            @Path("id") long id,
            @Query("apiKey") String apiKey);

    @GET("recipes/{id}/card")
    Call<RecipeCardPath> getRecipeCard(
            @Path("id") long id,
            @Query("apiKey") String apiKey);

    @GET("food/jokes/random")
    Call<Joke> getJoke(@Query("apiKey") String apiKey);

    @GET("recipes/random?limitLicense=true")
    Call<RandomSearch> getRandomRecipes(
            @Query("number") Integer number,
            @Query("tags") String diet,
            @Query("apiKey") String apiKey);

    @GET("recipes/random?limitLicense=true")
    Call<RandomSearch> getRandomRecipes(
            @Query("number") Integer number,
            @Query("apiKey") String apiKey);
}
