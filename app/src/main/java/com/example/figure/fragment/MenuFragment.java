package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.R;

import com.example.figure.MainActivity;
import com.example.figure.data.MenuItem;
import com.example.figure.data.MenuSection;
import com.example.figure.data.Restaurant;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Section;

import org.w3c.dom.Text;

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
    GroupAdapter groupAdapter;
    NestedScrollView nestedScrollView;
    public static Typeface face;
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
            this.context = (MainActivity) context;
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
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "menuFragment";
    }
    public void initViews(View view) {
        restaurantImage = (ImageView) view.findViewById(R.id.restaurant_image_view);

        ((TextView) view.findViewById(R.id.restaurant_name)).setText(restaurant.getRestaurant_name());
        ((TextView) view.findViewById(R.id.restaurant_name)).setTypeface((Typeface)ResourcesCompat.getFont(context, R.font.josefinsans));
        loading = (ProgressBar) view.findViewById(R.id.loading);
    }

    public void initMenu(View view) {
        nestedScrollView = view.findViewById(R.id.menu_nested_scroll_view);
        menuRecyclerView = view.findViewById(R.id.menu_recycler_view);
        layoutManager = new LinearLayoutManager(context);
        groupAdapter = new GroupAdapter();
        menuRecyclerView.setAdapter(groupAdapter);
        menuRecyclerView.setLayoutManager(layoutManager);
        groupAdapter.addAll(generateMenuSections());
        menuRecyclerView.setItemViewCacheSize(groupAdapter.getItemCount());
        size += groupAdapter.getItemCount();
        menuRecyclerView.setItemViewCacheSize(size);
        menuRecyclerView.setNestedScrollingEnabled(false);



    }

    public ArrayList<ExpandableGroup> generateMenuSections() {
        ArrayList<ExpandableGroup> expandableGroups = new ArrayList<ExpandableGroup>();
        for (MenuSection menuSection : restaurant.getMenus()[0].getMenu_sections()) {
            ExpandableGroup expandableGroup = new ExpandableGroup(menuSection, false);
            menuSection.setExpandCallback(eG -> {
                if (eG.isExpanded()) {
                    Log.d("expandedd called", "callback called");
                    //nestedScrollView.smoothScrollTo(0, 700);
                    menuRecyclerView.postDelayed(() -> {
                        float y = menuRecyclerView.getY() + menuRecyclerView.getChildAt(groupAdapter.getAdapterPosition(eG)).getY();
                        //menuRecyclerView.scrollToPosition(groupAdapter.getAdapterPosition(eG));
                        nestedScrollView.smoothScrollTo(0, (int) y);
                    }, 0);

                    //menuRecyclerView.scrollToPosition(groupAdapter.getAdapterPosition(eG));
                }
            });

            Section itemsSection = new Section(Arrays.asList(menuSection.getMenu_items()));
            size += itemsSection.getItemCount();
            expandableGroup.add(itemsSection);
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
}
