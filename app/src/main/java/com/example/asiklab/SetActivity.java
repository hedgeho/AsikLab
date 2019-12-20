package com.example.asiklab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        final EditText et_name = findViewById(R.id.et_name), et_length = findViewById(R.id.et_length),
                et_angle = findViewById(R.id.et_angle);
        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("data"));
            et_name.setText(object.getString("name"));
            et_length.setText(object.getDouble("length") + "");
            et_angle.setText(object.getDouble("angle") + "");
        } catch (JSONException e) {}

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject()
                            .put("name", et_name.getText().toString())
                            .put("length", Double.parseDouble(et_length.getText().toString()))
                            .put("angle", Double.parseDouble(et_angle.getText().toString()));
                    setResult(1, new Intent().putExtra("data", obj.toString()));
                    finish();
                } catch (JSONException e) {}
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.e("mylog", "smth");
        super.onBackPressed();
    }
}
