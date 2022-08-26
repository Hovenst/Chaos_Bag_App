package com.example.chaosbagprobabilitycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Called when user taps "manual bag" button
    public void manualCreate(View view) {
        // Do something
        Intent intent = new Intent(this, CreateBagManual.class);
        // EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void scenarioCreate(View view) {
        // Do something
        Intent intent = new Intent(this, CreateBagScenario.class);
        // EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}