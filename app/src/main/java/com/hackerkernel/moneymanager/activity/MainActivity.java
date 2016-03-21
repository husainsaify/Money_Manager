package com.hackerkernel.moneymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.hackerkernel.moneymanager.R;
import com.hackerkernel.moneymanager.adapter.TransactionAdapter;
import com.hackerkernel.moneymanager.pojo.TransactionPojo;
import com.hackerkernel.moneymanager.storage.Database;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.include_toolbar) Toolbar mToolbar;
    @Bind(R.id.fabAddMoney) FloatingActionButton mFabAddMoney;
    @Bind(R.id.fabSubtractMoney) FloatingActionButton mFabSubtractMoney;

    @Bind(R.id.mainlayout_empty) RelativeLayout mLayoutEmpty;
    @Bind(R.id.mainlayout_with_listview) RelativeLayout mLayoutMain;
    @Bind(R.id.total_wallet_amount) TextView mWalletAmount;
    @Bind(R.id.total_bank_amount) TextView mBankAmount;
    @Bind(R.id.listview) ListView mlistview;

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

        Database db = new Database(this);

         /*
        * Check transaction count
        * if its 0 (show empty layout)
        * else show main layout
        * */
        if (db.getTransactionCount() == 0){
            mLayoutEmpty.setVisibility(View.VISIBLE);
            mLayoutMain.setVisibility(View.GONE);
        }else{
            mLayoutMain.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);

            //set amount of bank & wallet
            mBankAmount.setText("Bank: "+db.getMoney(Database.MONEY_BANK));
            mWalletAmount.setText("Wallet: "+db.getMoney(Database.MONEY_WALLET));

            //set up transaction listview
            List<TransactionPojo> list = db.getAllTransaction();
            TransactionAdapter adapter = new TransactionAdapter(this,R.layout.row_transaction,list);
            mlistview.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mFabAddMoney){
            startActivity(new Intent(MainActivity.this,AddMoneyActivity.class));
        }else if (v == mFabSubtractMoney){
            startActivity(new Intent(MainActivity.this,SubtractMoneyActivity.class));
        }
    }

    /*
    * method to go to add money activity
    * */
    public void goToAddMoneyActivity(View view) {
        startActivity(new Intent(MainActivity.this,AddMoneyActivity.class));
    }
}
