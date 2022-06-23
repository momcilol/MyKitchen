package com.example.mykitchen.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = false)
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    @ColumnInfo(name = "rating")
    private double rating;
    @ColumnInfo(name = "source_url")
    private String sourceUrl;
    @ColumnInfo(name = "summary")
    private String summary;
    @ColumnInfo(name = "recipe_card_path")
    private String recipeCardPath;
    @ColumnInfo(name = "favourite")
    private boolean favourite = false;


    public Recipe() {}

    // Getter Methods

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public double getRating() { return rating; }

    public String getSourceUrl() { return sourceUrl; }

    public String getSummary() { return summary; }

    public String getRecipeCardPath() { return recipeCardPath; }

    public boolean isFavourite() { return favourite; }

    // Setter Methods

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setRating(double rating) { this.rating = rating; }

    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public void setSummary(String summary) { this.summary = summary; }

    public void setRecipeCardPath(String recipeCardPath) { this.recipeCardPath = recipeCardPath; }

    public void setFavourite(boolean favourite) { this.favourite = favourite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean hasDetails() {
        return summary != null;
    }
}
