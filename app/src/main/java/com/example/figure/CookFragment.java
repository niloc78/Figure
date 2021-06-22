package com.example.figure;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CookFragment extends Fragment {

    View _rootView;
    Context context;

//    public CookFragment() {
//        super(R.layout.cook_frag_layout);
//    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Toast.makeText(context, "rootview was null", Toast.LENGTH_SHORT).show();
            _rootView = inflater.inflate(R.layout.cook_frag_layout, container, false); // contains all the dash saved state/makes new if null
        }
        Toast.makeText(context, "child oncreateview called", Toast.LENGTH_SHORT).show();
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
        return "cookFragment";
    }



}
