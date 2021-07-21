package com.example.figure;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;

import org.json.JSONObject;


public class GetUrlContent {
    IResult mResultCallback = null;
    Context mContext;

    public GetUrlContent(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void postDataVolley(final String requestType, String url, JSONObject sendObj) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null) {
                        mResultCallback.notifySuccess(requestType, response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null) {
                        mResultCallback.notifyError(requestType, error);
                    }
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }

    public void getDataVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null) {
                        mResultCallback.notifySuccess(requestType, response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestType, error);
                    }
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }

    public void getImageVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if (mResultCallback != null) {
                        mResultCallback.notifySuccess(requestType, response);
                    }
                    Log.d("image bitmap resp", "received");

                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestType, error);
                    }
                    Log.d("image bitmap err", "err");
                }
            });

//            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    if (mResultCallback != null) {
//                        mResultCallback.notifySuccess(requestType, response);
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (mResultCallback != null) {
//                        mResultCallback.notifyError(requestType, error);
//                    }
//                }
//            });

            queue.add(request);

        } catch (Exception e) {

        }
    }
    public void getDataVolley(final String requestType, String url, String errand) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null) {
                        mResultCallback.notifySuccess(requestType, response, errand);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestType, error);
                    }
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }
}
