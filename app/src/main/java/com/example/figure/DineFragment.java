package com.example.figure;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class DineFragment extends Fragment {
    Context context;
    View _rootView;
    Button prefButton;
    Integer colorFrom;
    Integer colorTo;
    ValueAnimator colorAnim;

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
            _rootView = inflater.inflate(R.layout.dine_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (prefButton == null) {
            initPrefButton(view);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "dineFragment";
    }

    public void initPrefButton(View view) {
        prefButton = view.findViewById(R.id.preferences_button);
        colorFrom = ContextCompat.getColor(context, R.color.black);
        colorTo = ContextCompat.getColor(context, R.color.main_pink);
        colorAnim = ValueAnimator.ofArgb(colorFrom, colorTo);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    prefButton.setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
                }
            }
        });
        colorAnim.setDuration(900);
        colorAnim.setStartDelay(0);
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

    }
}
