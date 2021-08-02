package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.figure.BuildConfig;
import com.example.figure.GetUrlContent;
import com.example.figure.IResult;
import com.example.figure.MainActivity;
import com.example.figure.adapter.PreferenceRecyclerAdapter;
import com.example.figure.R;
import com.example.figure.model.RestaurantModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.Slider;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class DineDeliveryPreferencesFragment extends Fragment {

    Context context;
    View _rootView;
    String mode;
    TextView radiusTextView;
    Slider radiusSlider;
    RecyclerView cuisineTypeRecyclerView;
    RecyclerView.LayoutManager cuisineTypeLayoutManager;
    LinkedHashMap<String, Integer> cuisineTypeData;
    ImageButton locationSearchBarButton;
    RadioGroup priceRadioGroup;
    PreferenceRecyclerAdapter cuisineTypeAdapter;
    MaterialCardView searchButton;
    public static final String places_key = BuildConfig.PLACES_API_KEY;
    private Place currLocation;
    private String price_level = "";
    GetUrlContent mGetUrlContent;
    RestaurantModel restaurantModel;
    public static final String documenu_key = BuildConfig.DOCUMENU_API_KEY;

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
            _rootView = inflater.inflate(R.layout.dine_delivery_preferences_layout, container, false);

            mode = getParentFragment() instanceof  DineFragment ? "Dine" : "Delivery";

        }
        return _rootView;
    }

    public float getRadius() {
        return radiusSlider.getValue();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (cuisineTypeRecyclerView == null) {
            ImageButton backButton = (ImageButton) view.findViewById(R.id.dine_delivery_pref_back_button);

            if (mode.equalsIgnoreCase("Dine")) {
                backButton.setOnClickListener(v -> {
                    ((DineFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    ((MainFragment) getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);
                });
            } else if (mode.equalsIgnoreCase("Delivery")) {
                backButton.setOnClickListener(v -> {
                    ((DeliveryFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    ((MainFragment) getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);
                });
            }

            mGetUrlContent = new GetUrlContent(constrIResultCallback(), context);
            restaurantModel = new ViewModelProvider(requireActivity()).get(RestaurantModel.class);
            initViewsAndButtons(view);
            initRecyclerViews(view);
            initPlacesAPI(view);

        }



    }

    private IResult constrIResultCallback() {
        return new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("restaurant response", response.toString());
                restaurantModel.setResponse(response.toString(), getPriceLevel());
                if (getMode().equalsIgnoreCase("Dine")) {
                    ((DineFragment) getParentFragment()).initMenuFragment();
                }
            }

            @Override
            public void notifySuccess(String requestType, Bitmap response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("no restaurant response", "none");
            }

            @Override
            public void notifySuccess(String requestType, JSONObject response, String errand) {

            }
        };
    }

    private String buildUrl() {
        String url = "https://api.documenu.com/v2/restaurants/search/geo?";
        LatLng latlng = getCurrLocation().getLatLng();
        String lat = "lat=" + Double.toString(latlng.latitude);
        String lon = "&lon=" + Double.toString(latlng.longitude);
        String distance = "&distance=" + Float.toString(getRadius());
        String fullmenu = "&fullmenu";
        String cuisine = "&cuisine=" + getCuisine();

        url += lat + lon + distance + fullmenu + cuisine + "&key=" + documenu_key;
        return url;
    }

    void initPlacesAPI(View view) {
        if (!Places.isInitialized()) {
            Places.initialize(context, places_key);
        }
        PlacesClient placesClient = Places.createClient(context);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.place_search_autocomplete);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setHint("Enter your location");

        autocompleteSupportFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
        final View root = autocompleteSupportFragment.getView();

        locationSearchBarButton = (ImageButton) view.findViewById(R.id.location_search_bar_button);
        locationSearchBarButton.setOnClickListener(v -> {
            root.post(() -> root.findViewById(R.id.places_autocomplete_search_input).performClick());
        });

        //autocompleteSupportFragment.setMenuVisibility(false);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                currLocation = place;
            }

            @Override
            public void onError(@NonNull Status status) {
                currLocation = null;
            }
        });

    }

    public Place getCurrLocation() {
        return currLocation;
    }

    public void initViewsAndButtons(View view) {
        Typeface face = ResourcesCompat.getFont(context, R.font.josefinsans);
        ((TextView) view.findViewById(R.id.searching_up_to)).setTypeface(face);
        ((TextView) view.findViewById(R.id.mile_num)).setTypeface(face);
        ((TextView) view.findViewById(R.id.miles_away)).setTypeface(face);
        ((TextView) view.findViewById(R.id.preferences_text)).setTypeface(face);

        ((TextView) view.findViewById(R.id.restaurant_prices)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad1)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad2)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad3)).setTypeface(face);
        ((TextView) view.findViewById(R.id.update_preferences)).setTypeface(face);
        priceRadioGroup = (RadioGroup) view.findViewById(R.id.price_radio_group);
        priceRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("checked id: ", Integer.toString(checkedId));

            price_level = checkedId == -1 ? "" : ((RadioButton) group.findViewById(checkedId)).getText().toString();

            Log.d("price level: ", price_level);
        });
        searchButton = (MaterialCardView) view.findViewById(R.id.update_preferences_card_view);
        searchButton.setOnClickListener(v -> {
            if (isValidPreferences()) {
                mGetUrlContent.getDataVolley("GETCALL", buildUrl());
               ((ImageButton) view.findViewById(R.id.dine_delivery_pref_back_button)).performClick(); // to close
            } else {
                Toast.makeText(context, "Location and cuisine type must be set", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public String getPriceLevel() {
        return price_level;
    }

    private String getCuisine() {
        return cuisineTypeAdapter.getSelectedText();
    }

    private boolean isValidPreferences() {
        return currLocation != null && getCuisine() != "";
    }

    public void initRecyclerViews(View view) {
        String[] cuisineTypeKeys = new String[]{
                "American", "British", "Chinese", "French", "Indian", "Italian", "Japanese",
                "Kosher", "Mediterranean", "Mexican", "Middle Eastern", "South American"};
        final int[] cuisineTypeImages = new int[]{
                R.drawable.cuisine_type_american, R.drawable.cuisine_type_british, R.drawable.cuisine_type_chinese,
                R.drawable.cuisine_type_french, R.drawable.cuisine_type_indian, R.drawable.cuisine_type_italian, R.drawable.cuisine_type_japanese,
                R.drawable.cuisine_type_kosher, R.drawable.cuisine_type_mediterranean, R.drawable.cuisine_type_mexican, R.drawable.cuisine_type_middle_eastern,
                R.drawable.cuisine_type_south_american
        };
        cuisineTypeData = new LinkedHashMap<String, Integer>();
        for (int i = 0; i <= cuisineTypeKeys.length - 1; i++) {
            cuisineTypeData.put(cuisineTypeKeys[i], cuisineTypeImages[i]);
        }
        cuisineTypeRecyclerView = (RecyclerView) view.findViewById(R.id.dine_delivery_pref_cuisine_type_recycler_view);

        cuisineTypeLayoutManager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        cuisineTypeRecyclerView.setLayoutManager(cuisineTypeLayoutManager);
        cuisineTypeAdapter = new PreferenceRecyclerAdapter(context,cuisineTypeData);
        cuisineTypeRecyclerView.setAdapter(cuisineTypeAdapter);

        radiusSlider = (Slider) view.findViewById(R.id.radius_slider);
        radiusTextView = (TextView) view.findViewById(R.id.mile_num);

        radiusSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                radiusTextView.setText(Float.toString(radiusSlider.getValue()));
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString () {
        return "dineDeliveryPreferencesFragment";
    }



}
