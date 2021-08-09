package com.example.figure.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import com.example.figure.R;
import com.example.figure.fragment.ProfileFragment;

public class NutritionDialog extends AppCompatDialogFragment {

    private EditText edit_Curr;
    private EditText edit_Goal;
    private NutritionDialog.SetListener listener;
    private String mode;



    public NutritionDialog(int i) {
        this.mode = i == 0 ? "Calories" : i == 1 ? "Fat" : i == 2 ? "Carbs" : i == 3 ? "Protein" : "";
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_goals_dialog, null);

        AlertDialog dialog = builder.setView(view)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Set", (dialog1, which) -> {
                    String currVal = edit_Curr.getText().toString();
                    String goalVal = edit_Goal.getText().toString();

                    if (currVal.replace(" ", "").equalsIgnoreCase("")) {
                        currVal = "0";
                    }
                    if (goalVal.replace(" ", "").equalsIgnoreCase("")) {
                        goalVal = "0";
                    }

                    listener.setValues(currVal, goalVal, mode);

//                        if (ingred.replace(" ", "").equalsIgnoreCase("")) {
//                            Toast.makeText(getContext(), "Cannot have blank ingredient", Toast.LENGTH_SHORT).show();
//                        } else {
//                            listener.addIngredient(ingred);
//                        }
                }).show();

        edit_Curr = view.findViewById(R.id.edit_curr);
        edit_Goal = view.findViewById(R.id.edit_goal);
        ProfileFragment parFrag = (ProfileFragment) getParentFragment();
        if (mode.equalsIgnoreCase("calories")) {
            edit_Curr.setTextColor(0xFFB20000);
            edit_Curr.setText(parFrag.getCalories());
            edit_Goal.setText(parFrag.getGoalCalories());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFB20000);
        } else if (mode.equalsIgnoreCase("fat")) {
            edit_Curr.setTextColor(0xFFF9CC92);
            edit_Curr.setText(parFrag.getFat());
            edit_Goal.setText(parFrag.getGoalFat());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFF9CC92);
        } else if (mode.equalsIgnoreCase("carbs")) {
            edit_Curr.setTextColor(0xFFCFFFDD);
            edit_Curr.setText(parFrag.getCarbs());
            edit_Goal.setText(parFrag.getGoalCarbs());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFCFFFDD);
        } else if (mode.equalsIgnoreCase("protein")) {
            edit_Curr.setTextColor(0xFFCFE1FF);
            edit_Curr.setText(parFrag.getProtein());
            edit_Goal.setText(parFrag.getGoalProtein());
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFCFE1FF);
        }


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));



        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (NutritionDialog.SetListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement NutritionDialog");
        }
    }

    public interface SetListener {
        void setValues(String currVal, String goalVal, String mode);
    }
}

