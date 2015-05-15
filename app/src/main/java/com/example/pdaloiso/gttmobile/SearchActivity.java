package com.example.pdaloiso.gttmobile;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by pdaloiso on 14/05/2015.
 */
public class SearchActivity extends FragmentActivity  implements
        TextToSpeech.OnInitListener {

    private static final int SPEECH_REQUEST_CODE = 0;

    private TextToSpeech tts;
    private Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        displaySpeechRecognizer();
        tts = new TextToSpeech(this, this);
        r = new Random();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ITALIAN);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        }catch(Exception e){
            Toast.makeText(this, "Error initializing speech to text engine."+ e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            final TextView editText =(TextView) findViewById(R.id.editText);
            editText.setText(spokenText);
            tts.speak(spokenText+" sicuro?", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(r.nextLong()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
