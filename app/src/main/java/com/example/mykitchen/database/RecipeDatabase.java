package com.example.mykitchen.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mykitchen.database.dao.RecipeDao;
import com.example.mykitchen.database.entity.Recipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    private static RecipeDatabase recipeDatabase;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract RecipeDao recipeDao();

    public static RecipeDatabase getInstance(Context context) {
        if (recipeDatabase == null) {
            synchronized (RecipeDatabase.class) {
                recipeDatabase = Room.databaseBuilder(
                        context.getApplicationContext(),
                        RecipeDatabase.class,
                        "Recipe.db")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return recipeDatabase;
    }
}
