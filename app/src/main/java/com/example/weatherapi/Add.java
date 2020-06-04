package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add extends AppCompatActivity {

    EditText etloc;
    Button btnsrc;
    private String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etloc = findViewById(R.id.etLoc);
        btnsrc = findViewById(R.id.btnSearch);

        btnsrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location = etloc.getText().toString().trim();
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("value", Location);
        setResult(RESULT_OK,intent);
        finish();
    }
}
