package com.example.figure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.figure.data.DashModel;
import com.example.figure.R;

import java.util.List;

public class DashPagerAdapter extends PagerAdapter {
    private List<DashModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public DashPagerAdapter(List<DashModel> models, Context context) {

        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dash_image_layout, container, false);

        ImageView imageView;
        imageView = (ImageView) view.findViewById(R.id.dash_image);

        imageView.setImageResource(models.get(position).getImage());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
