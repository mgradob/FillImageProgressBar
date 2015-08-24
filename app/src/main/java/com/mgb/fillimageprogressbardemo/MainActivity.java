package com.mgb.fillimageprogressbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mgb.fillimageprogressbar.FillImageProgressBar;

public class MainActivity extends AppCompatActivity {

    FillImageProgressBar progressBar;
    Button detBtn, indBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (FillImageProgressBar) findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);

        detBtn = (Button) findViewById(R.id.detButton);
        detBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setIndeterminate(false);
                progressBar.resetToFixed(R.mipmap.ic_launcher);
            }
        });

        indBtn = (Button) findViewById(R.id.indButton);
        indBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setIndeterminate(true);
                progressBar.resetToProgress(R.mipmap.icon_lock_installing);
            }
        });
    }
}
