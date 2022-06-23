package com.example.mykitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.example.mykitchen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeCardFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.recipe_card_imageView)
    ImageView imageView;

    String imagePath;

    public RecipeCardFragment() {
        // Required empty public constructor
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        fillImageView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_card, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (imagePath != null) {
            fillImageView();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillImageView() {
        Lifecycle.State state = getLifecycle().getCurrentState();
        if (state != Lifecycle.State.INITIALIZED && state != Lifecycle.State.DESTROYED) {
            Glide.with(getContext()).load(imagePath).into(imageView);
        }
    }
}