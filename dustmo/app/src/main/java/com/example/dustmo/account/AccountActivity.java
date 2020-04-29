package com.example.dustmo.account;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dustmo.R;

public class AccountActivity extends AppCompatActivity {
    ImageView header;//creates variables for controlling the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);//sets the layout to activity_account.xml
        header = findViewById(R.id.header_image);//links header to the header_image element ID
        header.setImageResource(R.drawable.account_logo_small);//Sets header image from drawable folder
        header.setContentDescription("Account Screen");//sets content description for image
    }
}
