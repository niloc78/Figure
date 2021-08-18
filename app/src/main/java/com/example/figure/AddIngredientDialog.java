package com.example.figure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddIngredientDialog extends AppCompatDialogFragment {
    private EditText edit_ingredient;
    private AddIngredientDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_ingred_dialog, null);

        AlertDialog dialog = builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ingred = edit_ingredient.getText().toString();
                        if (ingred.replace(" ", "").equalsIgnoreCase("")) {
                            Toast.makeText(getContext(), "Cannot have blank ingredient", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.addIngredient(ingred);
                        }
                    }
                }).show();

        edit_ingredient = view.findViewById(R.id.edit_ingredient);



        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));

        return dialog;
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
