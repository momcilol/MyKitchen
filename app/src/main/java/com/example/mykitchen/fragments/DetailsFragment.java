package com.example.mykitchen.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.mykitchen.R;
import com.example.mykitchen.database.entity.Recipe;
import com.example.mykitchen.utils.RecipeRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailsFragment extends Fragment {

    private final String TAG = "Details Fragment";
    private Unbinder unbinder;

    @BindView(R.id.recipe_desc_image)
    ImageView imageView;

    @BindView(R.id.rating_text)
    TextView ratingText;

    @BindView(R.id.desc_title)
    TextView title;

    @BindView(R.id.summary)
    TextView summary;

    @BindView(R.id.choose_recipecard)
    Button btnRecipeCard;

    @BindView(R.id.visit_source)
    Button btnVisitSource;

    @BindView(R.id.favourites_Button)
    ImageButton btnFavourites;

    private boolean startingState;

    private Recipe recipe;
    private OnShowRecipeCard onShowRecipeCard;
    private OnFavouritesChanged onFavouritesChanged;

    private RecipeRepository recipeRepository;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public interface OnShowRecipeCard {
        void showRecipeCard(Recipe recipe);
    }

    public interface OnFavouritesChanged {
        void refreshFavourites();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeRepository = RecipeRepository.getInstance(getActivity().getApplicationContext());
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onShowRecipeCard = (OnShowRecipeCard) context;
        onFavouritesChanged = (OnFavouritesChanged) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(startingState != recipe.isFavourite()) {
            if (recipe.isFavourite()) {
                recipeRepository.addFavourite(recipe);
            } else {
                recipeRepository.removeFavourite(recipe);
            }
            onFavouritesChanged.refreshFavourites();
        }
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        fillUI();
    }

    private void fillUI() {
        Log.i(TAG, "fillUI");
        Lifecycle.State state = getLifecycle().getCurrentState();
        if (recipe != null && state != Lifecycle.State.INITIALIZED && state != Lifecycle.State.DESTROYED) {
            recipe.setFavourite(recipeRepository.isInFavourites(recipe));
            startingState = recipe.isFavourite();
            Glide.with(getContext()).load(recipe.getImagePath()).into(imageView);
            btnFavourites.setImageResource(recipe.isFavourite() ? R.drawable.icon_star : R.drawable.icon_star_border);
            title.setText(recipe.getTitle());
            ratingText.setText("Spoonacular score " + (int) recipe.getRating() + "%" );
            summary.setText(Html.fromHtml(recipe.getSummary()));
        }
    }

    @OnClick(R.id.choose_recipecard)
    public void recipeCard() {
        onShowRecipeCard.showRecipeCard(recipe);
    }

    @OnClick(R.id.visit_source)
    public void visitSource() {
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl()));
        Intent chooserIntent = Intent.createChooser(browser, "Load " + recipe.getSourceUrl() + " with:");
        try{
            startActivity(chooserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "We found no application.", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.favourites_Button)
    public void addOrRemoveFavourite() {
        if (recipe.isFavourite()) {
            recipe.setFavourite(false);
            btnFavourites.setImageResource(R.drawable.icon_star_border);
        } else {
            recipe.setFavourite(true);
            btnFavourites.setImageResource(R.drawable.icon_star);

        }
    }
}