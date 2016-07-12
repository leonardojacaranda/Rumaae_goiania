package com.example.leonardo.rumaae;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import android.view.View;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String msg = getIntent().getStringExtra("message");

    }

    public void startSecondActivity(View view) {
        Intent secondActivity = new Intent(this, Cadastro.class);
        startActivity(secondActivity);
    }
    public void Solic(View view) {
        Intent secondActivity = new Intent(this, Solicitacoes.class);
        startActivity(secondActivity);
    }
}