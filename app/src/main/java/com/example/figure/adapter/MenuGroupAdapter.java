package com.example.figure.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.example.figure.data.MenuItem;
import com.example.figure.data.MenuSection;
import com.example.figure.databinding.MenuItemLayoutBinding;
import com.example.figure.databinding.MenuSectionLayoutBinding;
import com.xwray.groupie.GroupAdapter;

import java.util.ArrayList;

public class MenuGroupAdapter extends GroupAdapter {

    Context context;
    int currFocused = 0;
    ArrayList<Object> filteredList;
    String t = "";

    public void findNext() {
        if (filteredList.size() > 0) {
            if (currFocused < filteredList.size()) {
                if (filteredList.get(currFocused) instanceof MenuSection) {
                    MenuSection menuSection = (MenuSection) filteredList.get(currFocused);
                    MenuSectionLayoutBinding viewBinding = menuSection.getViewBinding();
                    menuSection.callExpandCallback();
                    Log.d("section expandcallback", "called");
                    if (!menuSection.getExpandableGroup().isExpanded()) {
                        viewBinding.menuSectionName.performClick();
                        Log.d("clicked", "section clicked");
                    }
                } else if (filteredList.get(currFocused) instanceof MenuItem) {
                    MenuItem menuItem = (MenuItem) filteredList.get(currFocused);
                    //MenuItemLayoutBinding iBinding = menuItem.getItemBinding();
                    menuItem.callFilterCallBack();
                }
                Log.d("focused before", Integer.toString(currFocused));
                currFocused++;
                Log.d("focused after", Integer.toString(currFocused));
            } else {
                Log.d("focused before zerod", Integer.toString(currFocused));
                currFocused = 0;
                Log.d("focused after zerod", Integer.toString(currFocused));
                if (filteredList.get(currFocused) instanceof MenuSection) {
                    MenuSection menuSection = (MenuSection) filteredList.get(currFocused);
                    MenuSectionLayoutBinding viewBinding = menuSection.getViewBinding();
                    menuSection.callExpandCallback();
                    Log.d("section expandcallback", "called");
                    if (!menuSection.getExpandableGroup().isExpanded()) {
                        viewBinding.menuSectionName.performClick();
                        Log.d("clicked", "section clicked");

                    }
                } else if (filteredList.get(currFocused) instanceof MenuItem) {
                    MenuItem menuItem = (MenuItem) filteredList.get(currFocused);
                    //MenuItemLayoutBinding iBinding = menuItem.getItemBinding();
                    menuItem.callFilterCallBack();
                }
                currFocused++;
            }

        }


    }

    public void filter(String text) {
        this.t = text;
        filteredList.clear();
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
                        if (!filteredList.contains(menuSection)) {
                            filteredList.add(menuSection);
                        }
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

                        if (!filteredList.contains(menuItem)) {
                            filteredList.add(menuItem);
                        }

                        if (itemText.equalsIgnoreCase(text)) {
                            menuItem.callFilterCallBack();
                        }

                        String newString = itemText.replaceAll("(?i)"+text, "<font color='#F5C4C4'>"+"$0"+"</font>");
                        iBinding.menuItemName.setText(Html.fromHtml(newString));
                        Log.d("if block call", "called " + newString);
                    } else {
                        iBinding.menuItemName.setText(itemText);
                        Log.d("else block call", "called " + itemText);
                    }

                }

            }
        }
    }

    public void initializeFilteredList() {
        this.filteredList = new ArrayList<Object>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getT() {
        return t;
    }
}
