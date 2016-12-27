package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qingsongchou.library.widget.dialog.CommonLoadingDialog;
import com.qingsongchou.library.widget.progress.AngleProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AngleProgressBar bar1 = (AngleProgressBar) findViewById(R.id.bar1);
        AngleProgressBar bar2 = (AngleProgressBar) findViewById(R.id.bar2);
        AngleProgressBar bar3 = (AngleProgressBar) findViewById(R.id.bar3);
        AngleProgressBar bar4 = (AngleProgressBar) findViewById(R.id.bar4);
        AngleProgressBar bar5 = (AngleProgressBar) findViewById(R.id.bar5);
        bar1.setProgress(0);
        bar2.setProgress(10f);
        bar3.setProgress(50.25);
        bar4.setProgress(80.456);
        bar5.setProgress(100);

        new CommonLoadingDialog(this).show();
        View viewById = findViewById(R.id.textView);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonLoadingDialog(MainActivity.this).show("加载你妹");
            }
        });
    }
}
