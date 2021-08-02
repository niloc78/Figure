package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.figure.MainActivity;
import com.example.figure.adapter.NutritionAdapter;
import com.example.figure.R;
import com.example.figure.data.Recipe;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class RecipeFragment extends Fragment {

    Context context;
    View _rootView;
    ImageView recipeImage;
    TextView recipeName;
    Recipe recipe;
    RecyclerView nutritionRecyclerView;
    NutritionAdapter nutritionAdapter;
    RecyclerView.LayoutManager nutritionLayoutManager;
    LinkedHashMap<String,String> nutrition;
    WebView recipeWeb;
    ImageButton showRecipe;
    ScrollView recipeScrollView;
    ProgressBar loading;
    ImageView recipeNutritionDivider;
    //MaterialCardView recipeWebCard;
    //RelativeLayout recipeWebLayout;
    //    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
    public RecipeFragment(Recipe recipe) {
        super();
        this.recipe = recipe;
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.recipe_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initNutrition(view);
    }

    public ImageView getRecipeImage() {
        return recipeImage;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public ImageButton getShowRecipe() {
        return showRecipe;
    }

    public void initViews(View view) {
        recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
        recipeName = (TextView) view.findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getRecipeName());

        Typeface face = ResourcesCompat.getFont(context, R.font.josefinsans);
        recipeName.setTypeface(face);

        Bitmap bmp = null;
        Log.d("image url", recipe.getImageUrl());
        Picasso.with(getActivity()).load(Uri.parse(recipe.getImageUrl())).into(recipeImage);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        recipeScrollView = (ScrollView) view.findViewById(R.id.recipe_frag_scrollview);
        recipeImage.setMinimumHeight((displayMetrics.widthPixels/4)*3);
        //recipeWebCard = (MaterialCardView) view.findViewById(R.id.recipe_web_card);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        showRecipe = (ImageButton) view.findViewById(R.id.show_recipe);

        //recipeWebLayout = (RelativeLayout) view.findViewById(R.id.recipe_web_container);
        recipeWeb = (WebView) view.findViewById(R.id.recipe_web_view);
        recipeWeb.getSettings().setJavaScriptEnabled(true);
//        recipeWeb.getSettings().setLoadWithOverviewMode(true);
//        recipeWeb.getSettings().setUseWideViewPort(true);
        recipeWeb.getSettings().setDomStorageEnabled(true);
        recipeWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
                recipeScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        recipeScrollView.smoothScrollTo(0, recipeWeb.getTop());
                    }
                });
            }
        });
        //recipeWebCard.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //recipeWebLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        recipeWeb.setLayerType(View.LAYER_TYPE_NONE, null);

        showRecipe.setOnClickListener(v -> {
            showRecipe.setSelected(!showRecipe.isSelected());

            if (showRecipe.isSelected()) {
                recipeWeb.loadUrl(recipe.getUrl());
                //TransitionManager.beginDelayedTransition(recipeWeb, new AutoTransition());
                recipeWeb.setVisibility(View.VISIBLE);

            } else {
                recipeWeb.loadUrl("about:blank");
                //TransitionManager.beginDelayedTransition(recipeWeb, new AutoTransition());
                recipeWeb.setVisibility(View.GONE);
                //recipeScrollView.scrollTo(0, .getTop());
//                recipeWebLayout.removeAllViews();
//                recipeWebCard.removeAllViews();
//
//                recipeWeb.clearHistory();
//                recipeWeb.clearCache(true);
//                recipeWeb.onPause();
//                recipeWeb.removeAllViews();
//                recipeWeb.destroyDrawingCache();
//                recipeWeb.destroy();
//                recipeWeb = null;
            }

        });

//        recipeWeb.setWebViewClient(new WebViewClient());
//        recipeWeb.loadUrl(recipe.getUrl());
        recipeNutritionDivider = (ImageView) view.findViewById(R.id.recipe_nutrition_divider);

    }



    public void initNutrition(View view) {
        nutrition = new LinkedHashMap<>();
        JSONObject obj = new JSONObject(recipe.getTotalNutrients());
        //Log.d("test nutrients", obj.toString());
        try {
            nutrition.put("Calories", Integer.toString((int) recipe.getCalories()));
            nutrition.put("Protein", Long.toString(obj.getJSONObject("PROCNT").getLong("quantity")) + obj.getJSONObject("PROCNT").getString("unit"));
            nutrition.put("Cholesterol", (Long.toString(obj.getJSONObject("CHOLE").getLong("quantity")) + obj.getJSONObject("CHOLE").getString("unit")).replace("\n", ""));
            nutrition.put("Carbs", Long.toString(obj.getJSONObject("CHOCDF").getLong("quantity")) + obj.getJSONObject("CHOCDF").getString("unit"));
            nutrition.put("Sodium", Long.toString(obj.getJSONObject("NA").getLong("quantity")) + obj.getJSONObject("NA").getString("unit"));


//            Log.d("Test nutrients", "cal: " + recipe.getCalories() + "\n"
//                    + "protein: " + obj.getJSONObject("PROCNT").toString() + "\n"
//                    + "carbs: " + obj.getJSONObject("CHOCDF").toString() + "\n"
//                    + "cholesterol: " + obj.getJSONObject("CHOLE").toString() + "\n"
//                    + "sodium: " + obj.getJSONObject("NA").toString() + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nutritionAdapter = new NutritionAdapter(context, nutrition);
        nutritionLayoutManager = new GridLayoutManager(context, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        nutritionRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_nutrition_recycler_view);

        nutritionRecyclerView.setLayoutManager(nutritionLayoutManager);

        nutritionRecyclerView.setAdapter(nutritionAdapter);
//        nutritionRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                int position = parent.getChildAdapterPosition(view); // item position
//                int spanCount = 2;
//                int spacing = 150;//spacing between views in grid
//
//                if (position >= 0) {
//                    int column = position % spanCount; // item column
//                    Log.d("column", Integer.toString(column));
//                    outRect.left = ((spacing * (column)) + 10)/spanCount;
//
//                    //outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                    outRect.right = -1*(column - 1) * spacing/spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
////                    if (position < spanCount) { // top edge
////                        outRect.top = spacing;
////                    }
//                    //outRect.bottom = spacing; // item bottom
//                } else {
//                    outRect.left = 0;
//                    outRect.right = 0;
//                    outRect.top = 0;
//                    outRect.bottom = 0;
//                }
//            }
//        });
    }

    public ImageView getDivider() {
        return recipeNutritionDivider;
    }

    @Override
    public String toString () {
        return "recipeFragment";
    }

//    public void setImage() {
//
//    }
//    public void setRecipeName() {
//
//    }

}

