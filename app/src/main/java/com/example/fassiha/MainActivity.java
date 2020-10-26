package com.example.fassiha;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fassiha.behaviours.NetworkHandler;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView showMessageTextView;
    private TextView showResponseTextView;

    private TextToSpeech mTTS;
    String text = "";

    String postUrl = "http://192.168.1.3:8000/commands/";
    String requestUrl = "http:/192.168.1.3:8000/responses/1/";
    NetworkHandler networkHandler = new NetworkHandler(this, MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMessageTextView = (TextView) findViewById(R.id.showMessageTextView);
        showResponseTextView = (TextView) findViewById(R.id.showResponseTextView);
        showResponseTextView.addTextChangedListener(textWatcher);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(Locale.forLanguageTag("ar"));

                    if(result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "Language not supported");
                    }
                }else {
                    Log.e("TTS", "Initialization failed");
                }
                Log.e("TTS", "INIT");
            }
        });
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

    public void speak(String text){

        text= showResponseTextView.getText().toString();
        Log.e("TTS", text);

        mTTS.setPitch((float)1);
        mTTS.setSpeechRate((float)1);

        mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
        Log.e("TTS", "DONE !!");
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            text = s.toString();
            speak(text);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void disconnect(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
