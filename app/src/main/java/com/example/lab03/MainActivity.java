package com.example.lab03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "com.tradan.lab03.values";
    TextView br, tr, tl, bl;
    SeekBar seekBar;
    TextView[] views;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConstraintLayout layout;
    long startTime, clicks;
    float cPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        br = findViewById(R.id.bottomRight_text);
        tr = findViewById(R.id.topRight_text);
        bl = findViewById(R.id.bottomLeft_text);
        tl = findViewById(R.id.topLeft_text);
        seekBar = findViewById(R.id.seekBar);
        views = new TextView[]{br, bl, tr, tl};
        layout = findViewById(R.id.activity_main_layout);
        br.setOnClickListener(this);
        bl.setOnClickListener(this);
        tr.setOnClickListener(this);
        tl.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (TextView v : views) {
                    v.setTextSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastProgress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Snackbar snackbar = Snackbar.make(layout,
                        "Font Size Changed To " + seekBar.getProgress() + "sp",
                        Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                seekBar.setProgress(lastProgress);
                                for (TextView x: views) {x.setTextSize(lastProgress); }
                                Snackbar.make(layout, "Font Size Reverted Back To " + lastProgress
                                        + "sp", Snackbar.LENGTH_LONG);
                            }
                        });
                snackbar.setActionTextColor(Color.BLUE);
                View snackBarView = snackbar.getView();
                TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();

            }
        });
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editor.clear().apply();
                setInitialValues();
                return false;
            }
        });
        setInitialValues();
        startTime = System.currentTimeMillis();

    }

    private void setInitialValues() {
        for (TextView v : views) {
            v.setText(sharedPreferences.getString(v.getTag().toString(), "0"));
        }
        seekBar.setProgress(20);
    }

    @Override
    public void onClick(View v) {
        TextView x = (TextView) v;
        x.setText("" + (Integer.parseInt(x.getText().toString()) + 1));
        editor.putString(x.getTag().toString(), x.getText().toString());
        editor.apply();
        cPS = ++clicks/((System.currentTimeMillis() - startTime)/1000f);
        Toast.makeText(this, ""+cPS, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialValues();
    }
}