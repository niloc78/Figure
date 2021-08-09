package com.example.figure.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.R;

import com.example.figure.adapter.MenuGroupAdapter;
import com.example.figure.data.MenuItem;
import com.example.figure.data.MenuSection;
import com.example.figure.data.Restaurant;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.Section;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuFragment extends Fragment {
    Context context;
    View _rootView;
    ImageView restaurantImage;
    Restaurant restaurant;
    ProgressBar loading;
    RecyclerView menuRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    MenuGroupAdapter groupAdapter;
    NestedScrollView nestedScrollView;
    ImageButton searchBarIcon;
    public static Typeface face;
    View clayout;
    View dialogCard;
    View dialogView;
    Dialog dialog;
    ImageButton infoButton;
    SearchView searchView;
    int cx;
    int cy;
    int endRadius = 0;
    Animator revealAnimator;
    Animator exitAnimator;
    int size = 0;
    //    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }

    public MenuFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setRetainInstance(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = context;
             face = ResourcesCompat.getFont(context, R.font.josefinsans);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {

            _rootView = inflater.inflate(R.layout.menu_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (restaurantImage == null) {
            initViews(view);
            beginLoading();
            initMenu(view);
            initInfoDialog();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void initInfoDialog() {
        dialogView = View.inflate(context, R.layout.info_dialog, null);
        dialog = new Dialog(context, R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        dialogView.findViewById(R.id.dialogback).setOnClickListener( v-> {
            revealShow(false);
        });

        ((TextView) dialogView.findViewById(R.id.rest_name)).setText(restaurant.getRestaurant_name());
        ((TextView) dialogView.findViewById(R.id.rest_name)).setTypeface(face);

        ((TextView) dialogView.findViewById(R.id.address)).setText(restaurant.getAddress().get("street"));
        ((TextView) dialogView.findViewById(R.id.address)).setTypeface(face);

        ((TextView) dialogView.findViewById(R.id.phone)).setText(restaurant.getRestaurant_phone());
        ((TextView) dialogView.findViewById(R.id.phone)).setTypeface(face);

        ((TextView) dialogView.findViewById(R.id.website)).setText(restaurant.getRestaurant_website());
        ((TextView) dialogView.findViewById(R.id.website)).setTypeface(face);
        dialog.setOnShowListener(dialogInterface -> {
            dialogView.post(revealAnimationRunnable);
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCard = dialogView.findViewById(R.id.dialogcard);

//        int[] location = new int[2];
//        infoButton.getLocationOnScreen(location);
//        int y = location[1];
//
//        dialogCard.setY(y - infoButton.getHeight());
//
//        int w = dialogCard.getWidth();
//        int h = dialogCard.getHeight();
//
//        endRadius = (int) Math.hypot(w, h);
//
//        cx = (int) (infoButton.getX() + (infoButton.getWidth()/2));
//        cy = (int) 0;

//        revealAnimator = ViewAnimationUtils.createCircularReveal(dialogCard, cx, cy, 0, endRadius);
//        revealAnimator.setDuration(350);
//        exitAnimator = ViewAnimationUtils.createCircularReveal(dialogCard, cx, cy, endRadius, 0);

    }

    public void showDialog() {
        dialog.show();
    }



    private final Runnable revealAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            revealShow(true);
        }
    };

    public void revealShow(boolean b) {
        if (endRadius == 0) {
            int[] location = new int[2];
            infoButton.getLocationOnScreen(location);
            int y = location[1];
//              int y = (int) infoButton.getY();

              //int diff = ((int)clayout.getY() - (clayout.getHeight()/2)) - ((int)clayout.getY() - (dialogCard.getHeight()/2));


            dialogCard.setY(y - (infoButton.getHeight()));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                dialogCard.setY(y - (infoButton.getHeight()*2) - 5);
            }

            int w = dialogCard.getWidth();
            int h = dialogCard.getHeight();

            endRadius = (int) Math.hypot(w, h);

            cx = (int) (infoButton.getX() + (infoButton.getWidth()/2));
            cy = (int) 0;
        }

        if (b) {
            revealAnimator = ViewAnimationUtils.createCircularReveal(dialogCard, cx, cy, 0, endRadius);
            revealAnimator.setDuration(350);
            revealAnimator.setInterpolator(new AccelerateInterpolator());
            dialogCard.setVisibility(View.VISIBLE);
            revealAnimator.start();
        } else {

            exitAnimator = ViewAnimationUtils.createCircularReveal(dialogCard, cx, cy, endRadius, 0);
            exitAnimator.setDuration(350);
            exitAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    dialogCard.setVisibility(View.INVISIBLE);
                }
            });

            exitAnimator.start();
        }
    }

    @Override
    public String toString () {
        return "menuFragment";
    }
    public void initViews(View view) {
        restaurantImage = (ImageView) view.findViewById(R.id.restaurant_image_view);

        infoButton = (ImageButton) view.findViewById(R.id.info_button);
        infoButton.setOnClickListener( v-> {
            showDialog();
        });
        clayout = view.findViewById(R.id.clayout);

        ((TextView) view.findViewById(R.id.restaurant_name)).setText(restaurant.getRestaurant_name());
        ((TextView) view.findViewById(R.id.restaurant_name)).setTypeface((Typeface)ResourcesCompat.getFont(context, R.font.josefinsans));
        loading = (ProgressBar) view.findViewById(R.id.loading);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    groupAdapter.findNext();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("onquerytextchange", "called");
                groupAdapter.filter(newText);
                return true;
            }
        });
        searchBarIcon = (ImageButton) view.findViewById(R.id.search_bar_icon);

        searchBarIcon.setOnClickListener(v-> {
            searchView.setQuery(searchView.getQuery(), true);
        });

        ((Button) view.findViewById(R.id.preferences_button)).setOnClickListener( v-> {
            ((OrderFragment) getParentFragment()).getPrefButton().performClick();
        });

    }

    public void initMenu(View view) {
        nestedScrollView = view.findViewById(R.id.menu_nested_scroll_view);
        menuRecyclerView = view.findViewById(R.id.menu_recycler_view);
        layoutManager = new LinearLayoutManager(context);
        groupAdapter = new MenuGroupAdapter();
        groupAdapter.initializeFilteredList();
        groupAdapter.setContext(context);
        menuRecyclerView.setAdapter(groupAdapter);
        menuRecyclerView.setLayoutManager(layoutManager);
        groupAdapter.addAll(generateMenuSections());
//        groupAdapter.setFilterCallBack(text -> {
//            groupAdapter.filter(text);
//            Log.d("filter", "happened");
//        });
        menuRecyclerView.setItemViewCacheSize(groupAdapter.getItemCount());
        size += groupAdapter.getItemCount();
        menuRecyclerView.setItemViewCacheSize(size);
        menuRecyclerView.setNestedScrollingEnabled(false);

        //groupAdapter.filter("a");

    }

    public ArrayList<ExpandableGroup> generateMenuSections() {
        ArrayList<ExpandableGroup> expandableGroups = new ArrayList<ExpandableGroup>();
        for (MenuSection menuSection : restaurant.getMenus()[0].getMenu_sections()) {
            ExpandableGroup expandableGroup = new ExpandableGroup(menuSection, false);

            menuSection.setAnimationFinishedCallback( () -> {
                changeAdapterData();
            });

            menuSection.setExpandCallback(eG -> {
                if (eG.isExpanded()) {
                    //searchView.setQuery(searchView.getQuery(), false);
                    Log.d("expandedd called", "callback called");
                    //nestedScrollView.smoothScrollTo(0, 700);
                    menuRecyclerView.postDelayed(() -> {
                        float y = menuRecyclerView.getY() + menuRecyclerView.getChildAt(groupAdapter.getAdapterPosition(eG)).getY();

                        Log.d("chld count", Integer.toString(menuRecyclerView.getChildCount()));

                        //menuRecyclerView.scrollToPosition(groupAdapter.getAdapterPosition(eG));
                        nestedScrollView.smoothScrollTo(0, (int) y);
                    }, 0);

                    //menuRecyclerView.scrollToPosition(groupAdapter.getAdapterPosition(eG));
                }
            });
//            menuSection.setFilterCallBack(() -> {
//                if (!groupAdapter.getT().isEmpty() && (groupAdapter.getT() != null)) {
//                    groupAdapter.filter(groupAdapter.getT());
//                }
//            });

            for (MenuItem menuItem : menuSection.getMenu_items()) {
                menuItem.setMenuItemFilterCallback(mItem -> {
                    if (menuRecyclerView.getChildAt(groupAdapter.getAdapterPosition(mItem)) == null) {
                        groupAdapter.filter(searchView.getQuery().toString());
                    } else {
                        menuRecyclerView.post(() -> {
                            float y = menuRecyclerView.getY() + menuRecyclerView.getChildAt(groupAdapter.getAdapterPosition(mItem)).getY();
                            nestedScrollView.smoothScrollTo(0, (int) y);
                        });

                    }
                });

//                menuItem.setFilterCallBack(() -> {
//                    if (!groupAdapter.getT() .isEmpty() && (groupAdapter.getT() != null)) {
//                        groupAdapter.filter(groupAdapter.getT());
//                    }
//                });
            }
            Section itemsSection = new Section(Arrays.asList(menuSection.getMenu_items()));
            size += itemsSection.getItemCount();
            expandableGroup.add(itemsSection);
            //Log.d("expand child size", Integer.toString(expandableGroup.getGroup(0).getItem(0).getItemCount()));
            //Log.d("expand item size", Integer.toString(expandableGroup.getItemCount()));
            expandableGroups.add(expandableGroup);

        }
        return expandableGroups;
    }


    public void setImage(Bitmap bitmap) {
        //loading.setVisibility(View.VISIBLE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int minwidth = displayMetrics.widthPixels;
        int minheight = displayMetrics.heightPixels/3;
        float scaleWidth = ((float) minwidth)/width;
        float scaleHeight = ((float) minheight)/height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();

        restaurantImage.setMinimumHeight(minheight);

        Log.d("phone minwidth", Integer.toString(minwidth));
        Log.d("phone minheight", Integer.toString(minheight));

        Log.d("bitmap width", Integer.toString(resizedBitmap.getWidth()));
        Log.d("bitmap height", Integer.toString(resizedBitmap.getHeight()));
        Bitmap output = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = ContextCompat.getColor(context, R.color.white);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();
        float[] radii = new float[]{
                150, 150,
                150, 150,
                0, 0,
                0, 0
        };

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resizedBitmap, rect, rect, paint);
        restaurantImage.setImageBitmap(output);
        endLoading();

    }


    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void beginLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    public void endLoading() {
        loading.setVisibility(View.GONE);
    }

    void changeAdapterData() {
        new Handler().post(waitForAnimationsToFinishRunnable);
    }
    private Runnable waitForAnimationsToFinishRunnable = () -> waitForAnimationsToFinish();
    private void waitForAnimationsToFinish() {
        if (menuRecyclerView.isAnimating()) {
            menuRecyclerView.getItemAnimator().isRunning(animationFinishedListener);
            return;
        }
        onRecyclerViewAnimationsFinished();
    }
    private RecyclerView.ItemAnimator.ItemAnimatorFinishedListener animationFinishedListener = () -> new Handler().post(waitForAnimationsToFinishRunnable);
    private void onRecyclerViewAnimationsFinished() {
        if (!groupAdapter.getT().isEmpty() && (groupAdapter.getT() != null)) {
        groupAdapter.filter(groupAdapter.getT());
        }
    }
}
