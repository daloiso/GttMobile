package com.example.pdaloiso.gttmobile;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by pdaloiso on 14/05/2015.
 */
public class SearchActivity extends FragmentActivity {

    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        displaySpeechRecognizer();
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
