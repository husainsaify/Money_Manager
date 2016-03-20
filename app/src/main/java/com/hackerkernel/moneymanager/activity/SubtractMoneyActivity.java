package com.hackerkernel.moneymanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hackerkernel.moneymanager.R;
import com.hackerkernel.moneymanager.storage.Database;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubtractMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.amount) EditText mAmountView;
    @Bind(R.id.description) EditText mDescriptionView;
    @Bind(R.id.money_type) Spinner mMoneyTypeSpinner;
    @Bind(R.id.subtract_money) Button mSubtractMoneyButton;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtract_money);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(R.string.subtract_money);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSubtractMoneyButton.setOnClickListener(this);

        db = new Database(this);
    }

    @Override
    public void onClick(View v) {
        if (mSubtractMoneyButton == v){
            subtractMoneyFromDatabase();
        }
    }

    /*
    * Method to subtract money from database
    * */
    private void subtractMoneyFromDatabase() {
        String amount = mAmountView.getText().toString().trim();
        String description = mDescriptionView.getText().toString().trim();

        //get Money Type
        String[] moneyTypeArray = getResources().getStringArray(R.array.money_type_code);
        int pos = mMoneyTypeSpinner.getSelectedItemPosition();
        int moneyType = Integer.parseInt(moneyTypeArray[pos]);

        if (amount.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.enter_valid_amount, Toast.LENGTH_LONG).show();
        }else if (description.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.description_cannot_be_empty,Toast.LENGTH_LONG).show();
        }else{
            //subtract cash
            boolean result = db.subtractMoney(amount,moneyType);
            if(result){
                db.addTranscation(amount,description, Database.MONEY_SUBTRACTED,moneyType);
                Toast.makeText(getApplicationContext(),amount+" Rs subtracted from "+mMoneyTypeSpinner.getSelectedItem(),Toast.LENGTH_LONG).show();
                mAmountView.setText("");
                mDescriptionView.setText("");
            }
        }
    }
}
