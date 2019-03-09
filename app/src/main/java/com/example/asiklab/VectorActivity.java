package com.example.asiklab;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class VectorActivity extends AppCompatActivity {

    static VectorView vectorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector);

        vectorView = findViewById(R.id.vector1);
        vectorView.setViews((TextView) findViewById(R.id.tv_length),
                (TextView) findViewById(R.id.tv_angle),
                (TextView) findViewById(R.id.tv_length_b),
                (TextView) findViewById(R.id.tv_angle_b));
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectorView.delete();
            }
        });
        findViewById(R.id.btn_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vectorView.deleteAll();
            }
        });
        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .putExtra("x", vectorView.getx())
                        .putExtra("y", vectorView.gety()));
            }
        });
    }
    public static void log(String msg) {
        Log.e("mylog", msg);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        vectorView.draw();
    }
}
