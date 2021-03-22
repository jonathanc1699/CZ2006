package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Clinic_admin_page extends AppCompatActivity {

    private int queue_count=0;
    TextView textview_queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);

        textview_queue = (TextView) findViewById(R.id.textView_queue);
        textview_queue.setText(String.valueOf(queue_count));


    }

    public void button_increment(View view) {
        queue_count+=1;
        textview_queue.setText(String.valueOf(queue_count));
    }
}