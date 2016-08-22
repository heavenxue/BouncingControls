package com.aibei.lixue.bouncingmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aibei.lixue.bouncingmenu.widget.BoucingMenu;

public class MainActivity extends AppCompatActivity {

    private View main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        main_layout = findViewById(R.id.relitive_main);
//        Toast.makeText(getBaseContext(),"",Toast.LENGTH_SHORT).show();
        new BoucingMenu(getBaseContext(),main_layout,R.layout.menu_boucing).show();
    }

}
