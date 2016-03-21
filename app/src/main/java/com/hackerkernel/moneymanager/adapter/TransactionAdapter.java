package com.hackerkernel.moneymanager.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackerkernel.moneymanager.R;
import com.hackerkernel.moneymanager.pojo.TransactionPojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TransactionAdapter extends ArrayAdapter<TransactionPojo> {
    private int resource;
    private List<TransactionPojo> list;
    private LayoutInflater inflater;

    private ImageView transactionType;
    private TextView transactionDescription;
    private TextView transactionDate;
    private TextView transactionAmount;
    private ImageView transactionMoneyType;

    public TransactionAdapter(Context context, int resource, List<TransactionPojo> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resource, parent, false);
        }

        TransactionPojo current = this.list.get(position);
        //get the views
        transactionType = (ImageView) view.findViewById(R.id.transaction_type);
        transactionDescription = (TextView) view.findViewById(R.id.transaction_description);
        transactionDate = (TextView) view.findViewById(R.id.transaction_date);
        transactionAmount = (TextView) view.findViewById(R.id.transaction_amount);
        transactionMoneyType = (ImageView) view.findViewById(R.id.transaction_money_type);

        //set transaction type
        if (current.getType().equals("1")) {
            //added
            transactionType.setImageResource(R.drawable.ic_add_box_black_24dp);
        } else {
            //subtracted
            transactionType.setImageResource(R.drawable.ic_indeterminate_check_box_black_24dp);
        }

        //set descript
        transactionDescription.setText(current.getDescription());
        String date = getDateFromTimestamp(current.getTimestamp());
        transactionDate.setText(date);
        transactionAmount.setText("\u20B9" + current.getAmount());

        //set transaction money type image
        if (current.getMoneyType().equals("1")) {
            //wallet
            transactionMoneyType.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp);
        } else {
            //bank
            transactionMoneyType.setImageResource(R.drawable.ic_account_balance_black_24dp);
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private String getDateFromTimestamp(String timeStamp) {
        long timestamp = Long.parseLong(timeStamp) * 1000;
        DateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        Date netDate = (new Date(timestamp));
        return sdf.format(netDate);
    }
}
