package com.example.leonardo.rumaae;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String msg = getIntent().getStringExtra("message");

    }

    public void startSecondActivity(View view) {
        Intent secondActivity = new Intent(this, secondActivity.class);
        startActivity(secondActivity);
    }
}
