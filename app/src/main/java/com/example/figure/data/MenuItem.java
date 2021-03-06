package com.example.figure.data;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.figure.R;

import com.example.figure.databinding.MenuItemLayoutBinding;
import com.example.figure.fragment.MenuFragment;
import com.example.figure.MenuItemFilterCallback;
import com.xwray.groupie.viewbinding.BindableItem;

public class MenuItem extends BindableItem<MenuItemLayoutBinding> {
    private String name;
    private String description;
    private double price;
    transient MenuItemFilterCallback menuItemFilterCallback;
    transient MenuItemLayoutBinding itemBinding;

    public MenuItem() {

    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    protected MenuItemLayoutBinding initializeViewBinding(@NonNull View view) {
        return MenuItemLayoutBinding.bind(view);

    }


    @Override
    public void bind(@NonNull MenuItemLayoutBinding viewBinding, int position) {
        viewBinding.menuItemName.setText(getName());
        viewBinding.menuItemName.setTypeface(MenuFragment.face);
        viewBinding.menuItemPrice.setText("$ " + getPrice());
        viewBinding.menuItemPrice.setTypeface(MenuFragment.face);
        viewBinding.menuItemNutrition.setText("Set menu Item Nutrition");
        viewBinding.menuItemNutrition.setTypeface(MenuFragment.face);
        this.itemBinding = viewBinding;
        //notifyChanged();

        Log.d("settexts onbind", "called");


    }

    @Override
    public int getLayout() {
        return R.layout.menu_item_layout;
    }

    public void callFilterCallBack(){
        menuItemFilterCallback.onFiltered(this);
    }

    public void setMenuItemFilterCallback(MenuItemFilterCallback menuItemFilterCallback) {
        this.menuItemFilterCallback = menuItemFilterCallback;
    }

    public MenuItemLayoutBinding getItemBinding() {
        return itemBinding;
    }



}
