package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
//    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
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

        }
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


    }


}
