package com.example.asiklab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class VectorActivity extends AppCompatActivity {

    static VectorView vectorView;
    int x = 0, y = 0;

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
                if(x == 0) {
                    x = vectorView.getx();
                    y = vectorView.gety();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class)
                            .putExtra("x", x)
                            .putExtra("y", y)
                            .putExtra("x1", vectorView.getx())
                            .putExtra("y1", vectorView.gety()));
                }
            }
        });
        findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vectorView.getData().equals(""))
                    startActivityForResult(new Intent(getApplicationContext(), SetActivity.class).putExtra("data", vectorView.getData()), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        log("reg: " + requestCode + ", res: " + resultCode);
        if(data != null && resultCode == 1) {
            log("received: " + data.getStringExtra("data"));
            vectorView.setData(data.getStringExtra("data"));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        vectorView.draw(true);
    }
    public static void log(String msg) {
        Log.e("mylog", msg);
    }
}
