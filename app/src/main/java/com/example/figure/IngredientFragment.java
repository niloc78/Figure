package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientFragment extends Fragment implements AddIngredientDialog.AddIngredientDialogListener {
    Context context;
    View _rootView;
    FloatingActionButton addIngredientButton;
//    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setRetainInstance(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.ingred_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (addIngredientButton == null) {
            addIngredientButton = view.findViewById(R.id.add_ingred_button);
            addIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openIngredientDialog();
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void openIngredientDialog() {
        AddIngredientDialog ingredientDialog= new AddIngredientDialog();
        ingredientDialog.show(getChildFragmentManager(), "ingredient dialog");
    }

    @Override
    public String toString () {
        return "ingredientFragment";
    }


    @Override
    public void addIngredient(String ingred) {

    }


}
