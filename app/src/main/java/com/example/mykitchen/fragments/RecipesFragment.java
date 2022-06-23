package com.example.mykitchen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykitchen.R;
import com.example.mykitchen.database.entity.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipesFragment extends Fragment {

    private static final String TAG = "RecipesFragment";

    Unbinder unbinder;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecipeAdapter recipeAdapter;

    List<Recipe> recipeList;

    boolean notified;

    private RecipeFragmentInterface recipeFragmentInterface;

    public RecipesFragment() {
//        needed empty constructor
    }

    public interface RecipeFragmentInterface {
        void showDetailsFragment(Recipe recipe);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeAdapter = new RecipeAdapter(getContext());
        Log.i(TAG, "onCreate: Adapter created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        unbinder = ButterKnife.bind(this, view);

        // Set the adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipeAdapter);

        if (recipeList != null) {
            recipeAdapter.addData(recipeList);
        }
        Log.i(TAG, "onCreateView: ");

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        recipeFragmentInterface = (RecipeFragmentInterface) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        if (recipeAdapter != null) {
            recipeAdapter.addData(recipeList);
        }
    }
}