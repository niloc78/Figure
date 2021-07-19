package com.example.figure.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.LinkedHashMap;

public class PreferenceRecyclerAdapter extends RecyclerView.Adapter<PreferenceRecyclerAdapter.ViewHolder> {
    LinkedHashMap<String, Integer> data;
    ColorMatrix matrix;
    ColorMatrixColorFilter filter;
    Typeface face;
    Context context;
    private int selectedposition = -1;
    private String selectedText = "";

    public PreferenceRecyclerAdapter(Context context ,LinkedHashMap<String, Integer> data) {
        this.data = data;
        this.matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
        this.context = context;
        face = ResourcesCompat.getFont(context, R.font.josefinsans);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pref_item_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] keys = data.keySet().toArray(new String[data.size()]);

        ShapeableImageView imageView = holder.getPrefItemImage();
        MaterialCardView prefItemCardView = holder.getPrefItemCardView();

        holder.getPrefItemName().setText(keys[position]);
        holder.getPrefItemName().setTypeface(face);

        imageView.setImageResource(data.get(keys[position]));
//      imageView.setColorFilter(filter);

        if (selectedposition == position) {
            holder.itemView.setSelected(true);
            imageView.clearColorFilter();
            prefItemCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardbackgray));

        } else {
            holder.itemView.setSelected(false);
            imageView.setColorFilter(filter);
            prefItemCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {

            if (holder.itemView.isSelected()) {
                notifyItemChanged(selectedposition);
                selectedposition = -1;
                selectedText = "";

            } else {

                if (selectedposition >= 0) {
                    notifyItemChanged(selectedposition);
                }

                selectedposition = holder.getAdapterPosition();
                selectedText = holder.getPrefItemName().getText().toString();
                notifyItemChanged(selectedposition);

            }

        });

        // grey out default
        //imageView.clearColorFilter(); // clear grey filter

    }

    public int getSelectedPos() {
        return selectedposition;
    }

    public String getSelectedText() {
        return selectedText;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView prefItemCardView;
        TextView prefItemName;
        ShapeableImageView prefItemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prefItemCardView = (MaterialCardView) itemView.findViewById(R.id.pref_item_card);
            prefItemName = (TextView) itemView.findViewById(R.id.pref_item_name);
            prefItemImage = (ShapeableImageView) itemView.findViewById(R.id.pref_item_image);
        }

        public MaterialCardView getPrefItemCardView() {
            return prefItemCardView;
        }

        public ShapeableImageView getPrefItemImage() {
            return prefItemImage;
        }

        public TextView getPrefItemName() {
            return prefItemName;
        }
    }

}
