package com.example.figure.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

public class CustomRadioGroup  extends AppCompatRadioButton {
    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void toggle() {
        if (isChecked()) {
            if (getParent() != null && getParent() instanceof RadioGroup) {
                ((RadioGroup) getParent()).clearCheck();
            }
        } else {
            super.toggle();
        }
    }

}
