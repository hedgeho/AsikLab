package com.example.asiklab;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static int costyl = 0;
    private int i = 0, j = 0;
    FloatingActionButton fab;
    ConstraintLayout layout, root;
    ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(Intent.ACTION_POWER_USAGE_SUMMARY));
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final MainView view = findViewById(R.id.main_view);
        view.init(getIntent().getIntExtra("x", 0), getIntent().getIntExtra("y", 0),
                getIntent().getIntExtra("x1", 0), getIntent().getIntExtra("y1", 0));

        fab = findViewById(R.id.fab_play);
        EditText et_v = findViewById(R.id.et_v), et_t = findViewById(R.id.et_t), et_x = findViewById(R.id.et_x),
                et_y = findViewById(R.id.et_y);
        view.setViews(et_v, et_t, et_x, et_y);

        root = findViewById(R.id.root);
        layout = findViewById(R.id.layout);
        btn = findViewById(R.id.btn_down);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j == 0) {
                    root.removeView(layout);
                    ConstraintSet set = new ConstraintSet();
                    root.addView(layout, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, 1200));
                    set.clone(root);
                    set.connect(layout.getId(), ConstraintSet.LEFT, root.getId(), ConstraintSet.LEFT, 8);
                    set.connect(layout.getId(), ConstraintSet.RIGHT, root.getId(), ConstraintSet.RIGHT, 8);
                    set.connect(layout.getId(), ConstraintSet.TOP, root.getId(), ConstraintSet.TOP,  root.getHeight()-120);
                    //set.connect(view.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.TOP);
                    set.applyTo(root);
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    j = 1;
                } else {
                    root.removeView(layout);
                    ConstraintSet set = new ConstraintSet();
                    root.addView(layout, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, 1200));
                    set.clone(root);
                    set.connect(layout.getId(), ConstraintSet.LEFT, root.getId(), ConstraintSet.LEFT, 8);
                    set.connect(layout.getId(), ConstraintSet.RIGHT, root.getId(), ConstraintSet.RIGHT, 8);
                    set.connect(layout.getId(), ConstraintSet.TOP, root.getId(), ConstraintSet.TOP,  root.getHeight()-1200);
                    set.applyTo(root);
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    j = 0;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0) {
                    view.play();
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    i++;
                } else if(i==1) {
                    view.pause();
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    i++;
                } else {
                    view.pause();
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    i--;
                }
            }
        });

        et_t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(costyl > 0)
                    costyl--;
                else if(!s.toString().isEmpty())
                    view.setT(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_x.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(costyl > 0)
                    costyl--;
                else if(!s.toString().isEmpty())
                    view.setX(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_y.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(costyl > 0)
                    costyl--;
                else if(!s.toString().isEmpty())
                    view.setY(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(costyl > 0)
                    costyl--;
                else if(!s.toString().isEmpty())
                    view.setV(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
