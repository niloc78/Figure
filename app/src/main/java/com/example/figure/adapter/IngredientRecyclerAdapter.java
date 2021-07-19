package com.example.figure.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.R;

import java.util.ArrayList;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder> {

    private ArrayList<String> data;

    public IngredientRecyclerAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingred_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        if (position == 0) {
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.getIngredTextView().getLayoutParams();
//            params.topMargin = params.topMargin + 15; // left top right bottom
//            holder.getIngredTextView().setLayoutParams(params);
//        }

        holder.getIngredTextView().setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ingredTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredTextView = (TextView) itemView.findViewById(R.id.ingred_item_text_view);
        }

        public TextView getIngredTextView() {
            return ingredTextView;
        }
    }

}
