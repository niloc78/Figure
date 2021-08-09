package com.example.figure.fragment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.VolleyError;
import com.example.figure.BuildConfig;
import com.example.figure.GetUrlContent;
import com.example.figure.IResult;
import com.example.figure.MainActivity;
import com.example.figure.R;
import com.example.figure.data.Restaurant;
import com.example.figure.model.RestaurantModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderFragment extends Fragment {
    Context context;
    View _rootView;
    Button prefButton;
    Integer colorFrom;
    Integer colorTo;
    ValueAnimator colorAnim;
    MaterialCardView sampleMenuCard;
    public static final String places_key = BuildConfig.PLACES_API_KEY;
    public BottomSheetBehavior sheetBehavior;

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
            _rootView = inflater.inflate(R.layout.dine_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (prefButton == null) {
            initPrefButton(view);
            initDinePref(view);
//            ((Button) view.findViewById(R.id.test_dine_button)).setOnClickListener(v -> {
//                RestaurantModel restaurantModel = new ViewModelProvider(requireActivity()).get(RestaurantModel.class);
//                Restaurant restaurant = restaurantModel.chooseRestaurant();
//                Log.d("Test restaurant name", restaurant.getRestaurant_name());
//            });
            sampleMenuCard = (MaterialCardView) view.findViewById(R.id.sample_menu_card);
        }
    }

    public void initMenuFragment() {
        sampleMenuCard.setVisibility(View.GONE);
        RestaurantModel restaurantModel = new ViewModelProvider(requireActivity()).get(RestaurantModel.class);
        Restaurant restaurant = restaurantModel.chooseRestaurant();

        String latlng = restaurant.getGeo().get("lat") + "," + restaurant.getGeo().get("lon");
        String fields = "&fields=photos,formatted_address,name,rating,opening_hours,geometry";
        String name = restaurant.getRestaurant_name().replace(" ", "+") + "+";
        String address = restaurant.getAddress().get("street").replace(" ", "+");

        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=restaurant+" + name
                + address + "&inputtype=textquery"
                + "&locationbias=point:" + latlng
                + fields
                + "&key=" + places_key;

        Log.d("req url", url);

        GetUrlContent mGetUrlContent = new GetUrlContent(placeCallBack(), context);
        mGetUrlContent.getDataVolley("GETCALL", url);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.menu_container, new MenuFragment(restaurant), "menuFrag");
        transaction.commit();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "dineFragment";
    }

    public void initPrefButton(View view) {
        prefButton = view.findViewById(R.id.preferences_button);
        colorFrom = ContextCompat.getColor(context, R.color.black);
        colorTo = ContextCompat.getColor(context, R.color.main_pink);
        colorAnim = ValueAnimator.ofArgb(colorFrom, colorTo);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    prefButton.setBackgroundTintList(ColorStateList.valueOf((int)animation.getAnimatedValue()));
                }
            }
        });
        colorAnim.setDuration(900);
        colorAnim.setStartDelay(0);
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

        prefButton.setOnClickListener(v -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            ((MainFragment) getParentFragment()).mainSideBarIcon.setVisibility(View.GONE);
        });

    }

    public Button getPrefButton() {
        return prefButton;
    }

    private IResult placeCallBack() {
        return new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("restaurant image resp", response.toString());
                try {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int maxwidth = displayMetrics.widthPixels;
                    int maxheight = displayMetrics.heightPixels/3;
                    String maxdimens = "maxwidth=" + maxwidth + "&maxheight=" + maxheight;

                    String photo_ref = response.getJSONArray("candidates").getJSONObject(0).getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                    Log.d("photo ref", photo_ref);

                    String url = "https://maps.googleapis.com/maps/api/place/photo?" + maxdimens + "&photoreference=" + photo_ref + "&key=" + places_key;

                    GetUrlContent mGetUrlContent = new GetUrlContent(imageCallBack(), context);
                    mGetUrlContent.getImageVolley("GETCALL", url);

                } catch (JSONException e) {
                    ((MenuFragment)getChildFragmentManager().findFragmentById(R.id.menu_container)).endLoading();
                    Toast.makeText(context, "No restaurant image found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void notifySuccess(String requestType, Bitmap response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }

            @Override
            public void notifySuccess(String requestType, JSONObject response, String errand) {

            }
        };
    }
    private IResult imageCallBack() {
        return new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
            }

            @Override
            public void notifySuccess(String requestType, Bitmap response) {
                ((MenuFragment)getChildFragmentManager().findFragmentById(R.id.menu_container)).setImage(response);
                Log.d("image bitmap call", "backed got image");
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                ((MenuFragment)getChildFragmentManager().findFragmentById(R.id.menu_container)).endLoading();
                Log.d("no image resp", "no img resp");
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response, String errand) {

            }
        };
    }

    public void initDinePref(View view) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.add(R.id.dine_pref_container, new OrderPreferencesFragment(), "dinePreferencesFragment");
        trans.commit();

        sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.dine_pref_view));

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sheetBehavior.setDraggable(false);



//
//        ((ImageButton) view.findViewById(R.id.cook_pref_button)).bringToFront();
//        ((ImageButton) view.findViewById(R.id.cook_pref_button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                scrim.setClickable(true);
//                scrim.setBackgroundColor(ContextCompat.getColor(context, R.color.main_pink_alpha));
//            }
//        });
    }

}
