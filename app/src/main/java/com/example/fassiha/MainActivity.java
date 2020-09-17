package com.example.fassiha;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView showMessageTextView;
    private TextView showResponseTextView;
    String postUrl = "http://192.168.1.4:8000/commands/";
    String requestUrl = "http:/192.168.1.4:8000/responses/1/";
    NetworkHandler networkHandler = new NetworkHandler(this, MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMessageTextView = (TextView) findViewById(R.id.showMessageTextView);
        showResponseTextView = (TextView) findViewById(R.id.showResponseTextView);
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
        if(requestCode == 10){
            if(resultCode == RESULT_OK && data != null){
                final ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                showMessageTextView.setText(result.get(0));

                networkHandler.serverPost(postUrl, result.get(0));
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){e.printStackTrace();}
                networkHandler.serverRequest(requestUrl);
                //networkHandler.serverDelete(requestUrl);

                // Use code bellow to delete responses
                /*String url;
                for(int i=4;i<=7;i++){
                    url = "http:/192.168.1.6:8000/responses/";
                    url = url+Integer.toString(i)+"/";
                    networkHandler.serverDelete(url);
                }*/
            }
        }
    }

    public void startRegistration(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


}
