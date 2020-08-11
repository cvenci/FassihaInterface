package com.example.fassiha;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView) findViewById(R.id.messageView);

        // here
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.1.6:8000/commands/";

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //JSONObject jsonObject = new JSONObject(response);
                    //JSONArray jsonArray = jsonObject.getJSONArray("core");
                    JSONArray jsonArray = new  JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        //here
                        messageView.setText("2");
                        messageView.setText(jo.getString("core"));

                    }
                } catch (JSONException e) {
                    messageView.setText(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                messageView.setText(error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    public void getSpeechInput(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-dz");

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 10);
        }else{
            Toast.makeText(this, "Your Device does not support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageView.setText(result.get(0));
                }
                break;
        }
    }

    // The code bellow is for testing volley lib

    // Instantiate the RequestQueue
    /* RequestQueue queue = Volley.newRequestQueue(this);

    String url = "https://www.google.com";

    // Request a string response from the provided URL
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>(){
        @Override
        public void onResponse(String response){
            messageView.setText("Response is :" + response.substring(0,500));
        }
            },
            new Response.ErrorListener(){
        @Override
         public void onErrorResponse(VolleyError error){
            messageView.setText("That didn't work");
        }
            });
*/
}
