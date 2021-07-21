package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.figure.AddIngredientDialog;
import com.example.figure.BuildConfig;
import com.example.figure.GetUrlContent;
import com.example.figure.IResult;
import com.example.figure.adapter.IngredientRecyclerAdapter;
import com.example.figure.MainActivity;
import com.example.figure.R;
import com.example.figure.model.RecipeModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class IngredientFragment extends Fragment implements AddIngredientDialog.AddIngredientDialogListener {
    Context context;
    View _rootView;
    FloatingActionButton addIngredientButton;
    RecyclerView ingredRecyclerView;
    RecyclerView.LayoutManager mIngredLayoutManager;
    ArrayList<String> ingredData;
    IngredientRecyclerAdapter mIngredAdapter;
    ItemTouchHelper itemTouchHelper;
    RecipeModel recipeModel;
    String recipe_api_id = BuildConfig.EDAMAM_ID;
    String recipe_api_key = BuildConfig.EDAMAM_KEY;
    GetUrlContent mGetUrlContent;

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
            _rootView = inflater.inflate(R.layout.ingred_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (addIngredientButton == null) {
            addIngredientButton = view.findViewById(R.id.add_ingred_button);
            recipeModel = new ViewModelProvider(requireActivity()).get(RecipeModel.class);
            addIngredientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openIngredientDialog();
                }
            });

            initRecyclerView(view);

        ((ImageButton) view.findViewById(R.id.cook_pref_button)).bringToFront();
        ((ImageButton) view.findViewById(R.id.cook_pref_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CookFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ((MainFragment) getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.GONE);
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                scrim.setClickable(true);
//                scrim.setBackgroundColor(ContextCompat.getColor(context, R.color.main_pink_alpha));
            }
        });

        mGetUrlContent = new GetUrlContent(constrIResultCallback(), context);

        }
    }

    public IResult constrIResultCallback() {
        return new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("Recipe api response", response.toString());;
                //recipeModel = new ViewModelProvider(requireActivity()).get(RecipeModel.class);
                recipeModel.setResponse(response.toString());
                //pass ingreds to model?
                //test gson map
                recipeModel.setIngredients(ingredData.toArray(new String[ingredData.size()]));
                //recipeModel.sort();
                //pass to viewmodel here
                Bundle result = new Bundle();
                result.putBoolean("loaded", true);
                getParentFragmentManager().setFragmentResult("recipesLoaded", result);


            }

            @Override
            public void notifySuccess(String requestType, Bitmap response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Recipe callback error", "Error");
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response, String errand) {

            }
        };
    }

    public void initRecyclerView(View view) {
        ingredData = new ArrayList<String>();

        ingredRecyclerView = (RecyclerView) view.findViewById(R.id.ingred_recycler_view);
        mIngredLayoutManager = new LinearLayoutManager(context);
        ingredRecyclerView.setLayoutManager(mIngredLayoutManager);
        mIngredAdapter = new IngredientRecyclerAdapter(ingredData);
        ingredRecyclerView.setAdapter(mIngredAdapter);
        ingredRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(context, "on Moved ", Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(context, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                ingredData.remove(position);
                mIngredAdapter.notifyDataSetChanged();
                if (isMetIngredientMin()) {
                    getRecipeUrlContent(ingredData);
                } else {
                    Bundle result = new Bundle();
                    result.putBoolean("loaded", false);
                    getParentFragmentManager().setFragmentResult("recipesLoaded", result);
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.black))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.LEFT;
                final int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }


        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(ingredRecyclerView);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void openIngredientDialog() {
        AddIngredientDialog ingredientDialog= new AddIngredientDialog();
        ingredientDialog.show(getChildFragmentManager(), "ingredient dialog");
    }

    @Override
    public String toString () {
        return "ingredientFragment";
    }


    @Override
    public void addIngredient(String ingred) {

        if (ingredData.size() == 1) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((IngredientRecyclerAdapter.ViewHolder) ingredRecyclerView.findViewHolderForAdapterPosition(0)).getIngredTextView().getLayoutParams();
            params.topMargin = params.topMargin + 20;
            ((IngredientRecyclerAdapter.ViewHolder) ingredRecyclerView.findViewHolderForAdapterPosition(0)).getIngredTextView().setLayoutParams(params);
            mIngredAdapter.notifyDataSetChanged();
        }
        ingredData.add(ingred);
        mIngredAdapter.notifyDataSetChanged();
        if (isMetIngredientMin()) {
            CookPreferencesFragment frag = (CookPreferencesFragment) getParentFragmentManager().findFragmentById(R.id.cook_pref_container);
            if ((frag.mealTypeAdapter.getSelectedPos() == -1) && (frag.cuisineTypeAdapter.getSelectedPos() == -1)) {
                getRecipeUrlContent(ingredData);
            } else if ((frag.mealTypeAdapter.getSelectedPos() != -1) && (frag.cuisineTypeAdapter.getSelectedPos() != -1)) {
                getRecipeUrlContent(ingredData, frag.mealTypeAdapter.getSelectedText(), frag.cuisineTypeAdapter.getSelectedText());
            } else {
                if (frag.mealTypeAdapter.getSelectedPos() != -1) {
                    getRecipeUrlContent(ingredData, frag.mealTypeAdapter.getSelectedText(), 1);
                } else {
                    getRecipeUrlContent(ingredData, frag.cuisineTypeAdapter.getSelectedText(), 2);
                }
            }
        }
    }

    public void getRecipeUrlContent(ArrayList<String> ingredients) {
        String url = "https://api.edamam.com/api/recipes/v2?type=public";
        String app_id = "&app_id=" + recipe_api_id;
        String app_key = "&app_key=" + recipe_api_key;
        String ingrs = "&q=";
        String imgSize = "&imageSize=LARGE";
        int ingrMin = (int) Math.log(ingredients.size());
        int ingrMax = ingrMax(ingredients.size());
        String ingrRange = "&ingr=" + ingrMin + "-" + ingrMax;
        for (int i = 0; i <= ingredients.size() - 1; i++) {
            if (i == ingredients.size() - 1) {
                ingrs += ingredients.get(i);
                break;
            }
            ingrs += ingredients.get(i) + "%20";
        }
        url += ingrs + app_id
                 + app_key + ingrRange + imgSize;
        Log.d("request url", url);
        mGetUrlContent.getDataVolley("GETCALL", url);
    }
    public void getRecipeUrlContent(ArrayList<String> ingredients, String mealOrCuisineType, int t) { // 1 for meal , 2 for cuisine
        String url = "https://api.edamam.com/api/recipes/v2?type=public";
        String app_id = "&app_id=" + recipe_api_id;
        String app_key = "&app_key=" + recipe_api_key;
        String ingrs = "&q=";
        String imgSize = "&imageSize=LARGE";

        int ingrMin = (int) Math.log(ingredients.size());
        int ingrMax = ingrMax(ingredients.size());
        String ingrRange = "&ingr=" + ingrMin + "-" + ingrMax;
        for (int i = 0; i <= ingredients.size() - 1; i++) {
            if (i == ingredients.size() - 1) {
                ingrs += ingredients.get(i);
                break;
            }
            ingrs += ingredients.get(i) + "%20";
        }
        url += ingrs + app_id
                + app_key + ingrRange + imgSize;

        if (t == 1) {
            String mealType = "&mealType=" + mealOrCuisineType;
            url += mealType;
        } else {
            String cuisineType = "&cuisineType=" + mealOrCuisineType;
            url += cuisineType;
        }
        Log.d("request url", url);
        mGetUrlContent.getDataVolley("GETCALL", url);
    }
    public void getRecipeUrlContent(ArrayList<String> ingredients, String mealType, String cuisineType) {
        String url = "https://api.edamam.com/api/recipes/v2?type=public";
        String app_id = "&app_id=" + recipe_api_id;
        String app_key = "&app_key=" + recipe_api_key;
        String ingrs = "&q=";
        String imgSize = "&imageSize=LARGE";
        String mType = "&mealType=" + mealType;
        String cType = "&cuisineType=" + cuisineType;

        int ingrMin = (int) Math.log(ingredients.size());
        int ingrMax = ingrMax(ingredients.size());
        String ingrRange = "&ingr=" + ingrMin + "-" + ingrMax;
        for (int i = 0; i <= ingredients.size() - 1; i++) {
            if (i == ingredients.size() - 1) {
                ingrs += ingredients.get(i);
                break;
            }
            ingrs += ingredients.get(i) + "%20";
        }
        url += ingrs + app_id
                + app_key + ingrRange + imgSize + cType + mType;
        Log.d("request url", url);
        mGetUrlContent.getDataVolley("GETCALL", url);
    }

    public int ingrMax(int ingrNum) {

        if ((ingrNum >= 3) && (ingrNum <= 5)) {
            return 9;
        }

        int diff = (int) Math.log(ingrNum);
        return ingrNum + (ingrNum - diff);
    }

    public boolean isMetIngredientMin() {
        return ingredData.size() >= 3;
    }


}
