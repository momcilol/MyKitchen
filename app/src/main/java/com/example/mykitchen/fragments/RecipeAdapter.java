package com.example.mykitchen.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mykitchen.R;
import com.example.mykitchen.database.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    List<Recipe> recipeList;
    public Context context;
    private static final String TAG = "RecipeAdapter";

    public RecipeAdapter(Context context) {
        Log.i(TAG, "Created adapter.");
        this.recipeList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_recipe, parent, false);
        RecipeHolder vh = new RecipeHolder(v, recipeList, context);
        Log.i(TAG, "onCreateViewHolder: ");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeHolder holder, int position) {
        holder.recipe = recipeList.get(position);
        holder.title.setText(holder.recipe.getTitle());
        Glide.with(context).load(holder.recipe.getImagePath()).into(holder.image);
        Log.i(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {

        Recipe recipe;

        RecipesFragment.RecipeFragmentInterface recipeFragmentInterface;

        @BindView(R.id.recipeTitle)
        public TextView title;

        @BindView(R.id.imageView)
        public ImageView image;

        public RecipeHolder(@NonNull View itemView, final List<Recipe> recipeList, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recipeFragmentInterface = (RecipesFragment.RecipeFragmentInterface) context;
        }

        @OnClick({R.id.imageView, R.id.recipeTitle})
        public void seeDetails(View view) {
            recipeFragmentInterface.showDetailsFragment(recipe);
        }
    }

    public void clear() {
        recipeList.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Recipe> list) {
        recipeList = list;
        notifyDataSetChanged();
    }

    public void update() {
        notifyDataSetChanged();
    }

}
