package com.aibei.lixue.bouncingmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aibei.lixue.bouncingmenu.widget.BouceBallShoopShoop;

public class BouceBallShoopActivity extends AppCompatActivity {
    private BouceBallShoopShoop shoopshoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bouce_ball_shoop);
        shoopshoop = (BouceBallShoopShoop) findViewById(R.id.shoopshoop);
    }
}
