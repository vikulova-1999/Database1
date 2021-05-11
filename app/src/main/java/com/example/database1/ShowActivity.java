package com.example.database1;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvPass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        init();
        getIntentMain();
    }
    private void init()
    {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPass = findViewById(R.id.tvPass);
    }
    private void getIntentMain()
    {
        Intent i = getIntent();
        if(i != null)
        {
            tvName.setText(i.getStringExtra(Constant.USER_NAME));
            tvEmail.setText(i.getStringExtra(Constant.USER_EMAIL));
            tvPass.setText(i.getStringExtra(Constant.USER_PASS));
        }
    }
}