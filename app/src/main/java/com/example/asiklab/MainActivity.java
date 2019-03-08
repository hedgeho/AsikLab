package com.example.asiklab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainView view = findViewById(R.id.main_view);
        view.init(getIntent().getIntExtra("x", 0), getIntent().getIntExtra("y", 0), 1.0f, 1.0f);
    }
}
