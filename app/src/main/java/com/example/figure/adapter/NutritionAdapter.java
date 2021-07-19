package com.example.figure.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.R;

import java.util.LinkedHashMap;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {

    private LinkedHashMap<String, String> data;
    Typeface face;
    Context context;

    public NutritionAdapter(Context context, LinkedHashMap<String, String> data) {
        this.data = data;
        this.context = context;
        this.face = ResourcesCompat.getFont(context, R.font.josefinsans);
    }

    @NonNull
    @Override
    public NutritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_item_layout, parent, false);
        return new NutritionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionAdapter.ViewHolder holder, int position) {
        String[] keys = data.keySet().toArray(new String[data.size()]);
        TextView nutritionTextView = holder.getNutritionTextView();

        nutritionTextView.setText(keys[position] + " " + data.get(keys[position]));
        nutritionTextView.setTypeface(face);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) nutritionTextView.getLayoutParams();
        if (position % 2 == 0) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            nutritionTextView.setLayoutParams(lp);
        } else {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            //recipeImage.setMinimumHeight((displayMetrics.widthPixels/4)*3);
            lp.rightMargin = lp.rightMargin + (displayMetrics.widthPixels/16);
            nutritionTextView.setLayoutParams(lp);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nutritionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nutritionTextView = (TextView) itemView.findViewById(R.id.nutrition_text_view);
        }

        public TextView getNutritionTextView() {
            return nutritionTextView;
        }
    }
}
