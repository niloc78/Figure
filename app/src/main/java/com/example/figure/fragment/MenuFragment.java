package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.figure.MainActivity;

public class MenuFragment extends Fragment {
    Context context;
    View _rootView;
    int id;
    LayoutInflater inflater;
    ViewGroup container;
    Bundle savedInstanceState;

    public MenuFragment(int id) {
        this.id = id;
    }

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

    void setId(int id) {
        this.id = id;
        onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            _rootView = inflater.inflate(id, container, false);
        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
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

}
