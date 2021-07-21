package com.example.figure;


import android.graphics.Bitmap;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    public void notifySuccess(String requestType, JSONObject response);
    public void notifySuccess(String requestType, Bitmap response);
    public void notifyError(String requestType, VolleyError error);
    public void notifySuccess(String requestType, JSONObject response, String errand);
}
