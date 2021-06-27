package com.example.figure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddIngredientDialog extends AppCompatDialogFragment {
    private EditText edit_ingredient;
    private AddIngredientDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ingred_dialog, null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ingred = edit_ingredient.getText().toString();
                        listener.addIngredient(ingred);
                    }
                });
        edit_ingredient = view.findViewById(R.id.edit_ingredient);

        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (AddIngredientDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement AddIngredientDialogListener");
        }
    }

    public interface AddIngredientDialogListener {
        void addIngredient(String ingred);
    }

}
