package com.dranithix.fishackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroActivity extends AppCompatActivity {
    @Bind(R.id.signIn)
    TextView signIn;

    @Bind(R.id.get)
    LinearLayout get;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signIn)
    public void signIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.get)
    public void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
