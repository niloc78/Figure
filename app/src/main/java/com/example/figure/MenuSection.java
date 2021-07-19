package com.example.figure;

public class MenuSection {
    private String section_name;
    private String description;
    private MenuItem[] menu_items;

    public MenuItem[] getMenu_items() {
        return menu_items;
    }

    public String getDescription() {
        return description;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMenu_items(MenuItem[] menu_items) {
        this.menu_items = menu_items;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }
}
