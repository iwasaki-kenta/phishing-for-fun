package com.dranithix.fishackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    @Bind(R.id.signIn)
    TextView signIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.backButton)
    public void backButton() {
        finish();
    }

    @OnClick(R.id.signIn)
    public void signIn() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
