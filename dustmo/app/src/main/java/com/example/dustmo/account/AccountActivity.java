package com.example.dustmo.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.dustmo.R;

public class AccountActivity extends AppCompatActivity {
    ImageView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        header = findViewById(R.id.header_image);
        header.setImageResource(R.drawable.account_logo_small);
        header.setContentDescription("Account Screen");
    }
}
