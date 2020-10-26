package com.example.fassiha.behaviours;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fassiha.R;
import com.example.fassiha.dataclass.FassihaResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkHandler {

    private Context context;
    private Activity activity;

    public  NetworkHandler(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void serverRequest(String url){

        final TextView showResponseTextView = (TextView) activity.findViewById(
                R.id.showResponseTextView);

        RequestQueue queue = Volley.newRequestQueue(context);
        final AppsHandler appsHandler = new AppsHandler(context, activity);
        // Request a string response from provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jso= new JSONObject(response);
                            int level = Integer.parseInt(jso.getString("level"));
                            int app_id = Integer.parseInt(jso.getString("app_id"));
                            String core = jso.getString("core");
                            String args = jso.getString("args");
                            String command = jso.getString("command");
                            FassihaResponse fassihaResponse = new FassihaResponse(level, app_id,
                                    core, args, command);
                            if (level != 2){
                                showResponseTextView.setText(fassihaResponse.core);
                                appsHandler.openApp(fassihaResponse.app_id);
                            }else{
                                if(fassihaResponse.core.length()<30) {
                                    showResponseTextView.setText(fassihaResponse.core);
                                    //appsHandler.googleSearch(fassihaResponse.command);
                                }else{
                                    showResponseTextView.setText("جاري البحث");
                                    appsHandler.googleSearch(fassihaResponse.command);
                                }
                                }

                        } catch (JSONException e) {
                            showResponseTextView.setText(e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showResponseTextView.setText(error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

        public void serverPost(String url, String command){

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            //String URL = "http://...";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id","1");
            jsonBody.put("core", command);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody.getBytes();
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
                requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void serverDelete(String url){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VolleyDelete", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyDelete", error.toString());
            }
        });
        queue.add(stringRequest);
    }

}
