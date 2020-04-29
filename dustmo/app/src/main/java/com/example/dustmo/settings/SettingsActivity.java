package com.example.dustmo.settings;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dustmo.R;

public class SettingsActivity extends AppCompatActivity {
    ImageView header;//creates variables for controlling the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);//sets the layout to activity_settings.xml
        header = findViewById(R.id.header_image);//links header to the header_image element ID
        header.setImageResource(R.drawable.settings_logo_small);//Sets header image from drawable folder
        header.setContentDescription("Settings Screen");//sets content description for image
    }
}
