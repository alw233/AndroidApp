package com.example.abigailwatson.project3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;

public class PurchaseActivity extends AppCompatActivity {

    private ToyList purchases;

    ArrayList<String> toyNames = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    private ListView lv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        purchases = getIntent().getExtras().getParcelable("toyList");

        for (int i = 0; i < purchases.getToyList().size(); i++) {
            String name = purchases.getToy(i).getToyName();
            name += ", $";
            name += Integer.toString(purchases.getToy(i).getPrice());
            toyNames.add(name);
        }

        setContentView(R.layout.activity_purchase);

        lv = (ListView) findViewById(R.id.toys_to_purchase);
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                toyNames);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                // Can't manage to remove an item here
                purchases.getToyList().remove(position);
                toyNames.remove(position);//where arg2 is position of item you click
                updatePrice();
                arrayAdapter.notifyDataSetChanged();

                return true;
            }
        });

        tv = (TextView) findViewById(R.id.toy_prices);
        updatePrice();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void updatePrice() {
        int totalPrice = 0;
        for (int i = 0; i < purchases.getToyList().size(); i++) {
            totalPrice += purchases.getToy(i).getPrice();
        }
        String priceString = "$";
        priceString += Integer.toString(totalPrice);
        tv.setText(priceString);
    }

    public  void weblink(View view){
        goToUrl("https://goo.gl/maps/DXdnjiBnzzR2");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

    }
}
