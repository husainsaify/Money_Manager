package com.hackerkernel.moneymanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fabAddMoney) FloatingActionButton mFabAddMoney;
    @Bind(R.id.fabSubtractMoney) FloatingActionButton mFabSubtractMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //set Toolbar
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.app_name);

        mFabAddMoney.setOnClickListener(this);
        mFabSubtractMoney.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mFabAddMoney){
            Toast.makeText(getApplicationContext(),"Add money bitch",Toast.LENGTH_LONG).show();
        }else if (v == mFabSubtractMoney){
            Toast.makeText(getApplicationContext(),"sub money bitch",Toast.LENGTH_LONG).show();
        }
    }
}
