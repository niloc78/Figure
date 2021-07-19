package com.example.figure.data;

public class Menu {
    private String menu_name;
    private MenuSection[] menu_sections;

    public MenuSection[] getMenu_sections() {
        return menu_sections;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setMenu_sections(MenuSection[] menu_sections) {
        this.menu_sections = menu_sections;
    }
}
