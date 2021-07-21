package com.example.figure.data;

import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.figure.ExpandCallback;
import com.example.figure.data.MenuItem;
import com.example.figure.databinding.MenuSectionLayoutBinding;
import com.example.figure.fragment.MenuFragment;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.example.figure.R;
import com.xwray.groupie.viewbinding.BindableItem;

public class MenuSection extends BindableItem<MenuSectionLayoutBinding> implements ExpandableItem {
    private String section_name;
    private String description;
    private MenuItem[] menu_items;
    ExpandCallback expandCallback;
    ExpandableGroup expandableGroup;

    boolean expanded;
    public MenuSection() {
    }

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

    @NonNull
    @Override
    protected MenuSectionLayoutBinding initializeViewBinding(@NonNull View view) {
        return MenuSectionLayoutBinding.bind(view);
    }



    public ExpandableGroup getExpandableGroup() {
        return expandableGroup;
    }



    public boolean isExpanded() {
        return expanded;
    }


    @Override
    public void bind(@NonNull MenuSectionLayoutBinding viewBinding, int position) {
        ChangeBounds changeBounds = new ChangeBounds();

        changeBounds.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (expanded == true) {
                    expandableGroup.onToggleExpanded();
                }
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                if (expanded == false) {
                    expandableGroup.onToggleExpanded();
                }
                if (expandCallback != null) {
                    expandCallback.onExpandChanged(expandableGroup);
                }
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });

        viewBinding.menuSectionName.setText(getSection_name());
        viewBinding.menuSectionName.setTypeface(MenuFragment.face);
        viewBinding.menuSectionName.setOnClickListener(v -> {


            TransitionManager.go(new Scene((ViewGroup) viewBinding.menuSectionName.getParent()), changeBounds);
//            TransitionManager.beginDelayedTransition((ViewGroup) viewBinding.menuSectionName.getParent());
            ConstraintLayout constraintLayout = (ConstraintLayout) viewBinding.menuSectionName.getParent();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            if (!expandableGroup.isExpanded()) {
                expanded = false;
                viewBinding.menuSectionName.setSelected(true);
                constraintSet.clear(R.id.menu_section_name, ConstraintSet.RIGHT);
                constraintSet.connect(R.id.menu_section_name, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                constraintSet.applyTo(constraintLayout);
            } else {
                expanded = true;
                Log.d("else called", "else called");
                viewBinding.menuSectionName.setSelected(false);
                constraintSet.clear(R.id.menu_section_name, ConstraintSet.LEFT);
                constraintSet.connect(R.id.menu_section_name, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                constraintSet.applyTo(constraintLayout);
            }

        });
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setExpandCallback(ExpandCallback expandCallback) {
        this.expandCallback = expandCallback;
    }

    @Override
    public int getLayout() {
        return R.layout.menu_section_layout;
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        expandableGroup = onToggleListener;
    }
}
