package com.example.figure.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.figure.R;
import com.example.figure.fragment.ProfileFragment;

public class ProfileStatDialog extends AppCompatDialogFragment {
    private EditText edit_Name;
    private NumberPicker feetPicker;
    private NumberPicker inchesPicker;
    private EditText edit_Weight;
    private EditText edit_Goal_Weight;
    private ProfileStatDialog.SetProfileStatsListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_profile_dialog, null);

        AlertDialog dialog = builder.setView(view)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Set", (dialog1, which) -> {

                    String name = edit_Name.getText().toString();
                    int feet = feetPicker.getValue();
                    int inches = inchesPicker.getValue();
                    String height = feet + "'" + inches;
                    String weight = edit_Weight.getText().toString();
                    String goalWeight = edit_Goal_Weight.getText().toString();

                    if (name.replace(" ", "").equalsIgnoreCase("")) {
                        name = "Name";
                    }
                    if (weight.replace(" ", "").equalsIgnoreCase("")) {
                        weight = "0";
                    }
                    if (goalWeight.replace(" ", "").equalsIgnoreCase("")) {
                        goalWeight = "0";
                    }

                    listener.setStats(name, height, weight, goalWeight);
                }).show();

        edit_Name = (EditText) view.findViewById(R.id.edit_profile_name);
        feetPicker = (NumberPicker) view.findViewById(R.id.feetpicker);
        inchesPicker = (NumberPicker) view.findViewById(R.id.inchpicker);
        edit_Weight = (EditText) view.findViewById(R.id.edit_weight);
        edit_Goal_Weight = (EditText) view.findViewById(R.id.edit_goal_weight);

        feetPicker.setMinValue(1);
        feetPicker.setMaxValue(10);
        inchesPicker.setMinValue(0);
        inchesPicker.setMaxValue(11);

        ProfileFragment parFrag = (ProfileFragment) getParentFragment();

        edit_Name.setText(parFrag.getName());
        feetPicker.setValue(parFrag.getFeet());
        inchesPicker.setValue(parFrag.getInch());
        edit_Weight.setText(parFrag.getWeight());
        edit_Goal_Weight.setText(parFrag.getGoalWeight());


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackground(ContextCompat.getDrawable(getContext(), R.color.white));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.black));



        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ProfileStatDialog.SetProfileStatsListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement ProfileStatDialog");
        }
    }

    public interface SetProfileStatsListener {
        void setStats(String name, String height, String weight, String goalWeight);
    }
}
