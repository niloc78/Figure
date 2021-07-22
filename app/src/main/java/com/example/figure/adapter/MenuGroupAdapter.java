package com.example.figure.adapter;

import android.content.Context;
import android.text.Html;

import androidx.core.content.ContextCompat;

import com.example.figure.R;
import com.example.figure.data.Menu;
import com.example.figure.data.MenuItem;
import com.example.figure.data.MenuSection;
import com.example.figure.databinding.MenuItemLayoutBinding;
import com.example.figure.databinding.MenuSectionLayoutBinding;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;

public class MenuGroupAdapter extends GroupAdapter {

    Context context;
    int currFocused;
    ArrayList<Object> filteredList;

    public void findNext() {

    }

    public void filter(String text) {

        if (text.isEmpty()) {
            for (int i = 0; i<= getItemCount() - 1; i++) { // menusection type
                if (getItem(i) instanceof MenuSection) {
                    MenuSectionLayoutBinding viewBinding = ((MenuSection) getItem(i)).getViewBinding();
                    String newString = viewBinding.menuSectionName.getText().toString();
                    viewBinding.menuSectionName.setText(newString);
                } else if (getItem(i) instanceof MenuItem) {
                    MenuItemLayoutBinding iBinding = ((MenuItem) getItem(i)).getItemBinding();
                    String newString = iBinding.menuItemName.getText().toString();
                    iBinding.menuItemName.setText(newString);
                }

            }
        } else {
            for (int i = 0; i<= getItemCount() - 1; i++) { // menusection type
                if (getItem(i) instanceof MenuSection) {
                    MenuSection menuSection = (MenuSection) getItem(i);
                    MenuSectionLayoutBinding viewBinding = menuSection.getViewBinding();
                    String sectionText = viewBinding.menuSectionName.getText().toString();
                    if (sectionText.toLowerCase().contains(text.toLowerCase())) {
                        if (sectionText.equalsIgnoreCase(text)) {
                            menuSection.callExpandCallback();
                            if (!menuSection.getExpandableGroup().isExpanded()) {
                                viewBinding.menuSectionName.performClick();
                            }
                        }
                        String newString = sectionText.replaceAll("(?i)"+text, "<font color='#F5C4C4'>"+"$0"+"</font>");
                        viewBinding.menuSectionName.setText(Html.fromHtml(newString));
                    } else {
                        viewBinding.menuSectionName.setText(sectionText);
                    }
                } else if (getItem(i) instanceof MenuItem) {
                    MenuItem menuItem = (MenuItem) getItem(i);
                    MenuItemLayoutBinding iBinding = menuItem.getItemBinding();
                    String itemText = iBinding.menuItemName.getText().toString();
                    if (itemText.toLowerCase().contains(text.toLowerCase())) {
                        if (itemText.equalsIgnoreCase(text)) {
                            menuItem.callFilterCallBack();
                        }
                        String newString = itemText.replaceAll("(?i)"+text, "<font color='#F5C4C4'>"+"$0"+"</font>");
                        iBinding.menuItemName.setText(Html.fromHtml(newString));
                    } else { ;
                        iBinding.menuItemName.setText(itemText);
                    }

                }

            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
