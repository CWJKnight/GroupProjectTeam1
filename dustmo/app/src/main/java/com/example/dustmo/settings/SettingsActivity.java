package com.example.dustmo.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.dustmo.R;

public class SettingsActivity extends AppCompatActivity {
    ImageView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        header = findViewById(R.id.header_image);
        header.setImageResource(R.drawable.settings_logo_small);
        header.setContentDescription("Settings Screen");
    }
}
