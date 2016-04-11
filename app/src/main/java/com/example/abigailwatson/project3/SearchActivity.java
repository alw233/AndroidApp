package com.example.abigailwatson.project3;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.DragEvent;
import android.widget.FrameLayout;
import android.content.ClipData;
import android.content.ClipDescription;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.View.DragShadowBuilder;
import android.content.Intent;
import android.app.ProgressDialog;

public class SearchActivity extends AppCompatActivity {

    public ToyList buttons;
    final String textSource = "http://people.cs.georgetown.edu/~wzhou/toy.data";

    private  FrameLayout targetLayout;
    private ListView lv;

    private ToyList shoppingCart = new ToyList();
    MyDragEventListener myDragEventListener = new MyDragEventListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new InternetClass(this).execute(textSource);

        setContentView(R.layout.activity_search);

        lv = (ListView) findViewById(R.id.list_of_toys);

        targetLayout = (FrameLayout)findViewById(R.id.targetlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    OnItemLongClickListener listSourceItemLongClickListener
            = new OnItemLongClickListener(){

        @Override
        public boolean onItemLongClick(AdapterView<?> l, View v,
                                       int position, long id) {

            //Selected item is passed as item in dragData
            ClipData.Item item = new ClipData.Item(Integer.toString(position));

            String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData((CharSequence) v.getTag(),
                    clipDescription,
                    item);
            DragShadowBuilder myShadow = new DragShadowBuilder(v);

            v.startDrag(dragData, //ClipData
                    myShadow,  //View.DragShadowBuilder
                    buttons.getToyList().get(position),  //Object myLocalState
                    0);    //flags

            return true;
        }};

    public void goToScreenOne(View view) {
        finish();
    }

    public void completePurchase(View view) {
        startActivityForResult(new Intent(this, PurchaseActivity.class).putExtra("toyList", shoppingCart), 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1)
        {
            finish();
        }
    }

    protected class MyDragEventListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //All involved view accept ACTION_DRAG_STARTED for MIMETYPE_TEXT_PLAIN
                    if (event.getClipDescription()
                            .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                    {
                        return true; //Accept
                    }else{
                        return false; //reject
                    }
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    //If apply only if drop on buttonTarget
                    if(v == targetLayout){
                        String droppedItemIndex = item.getText().toString();
                        shoppingCart.addToy(buttons.getToyList().get(Integer.parseInt(droppedItemIndex)));

                        return true;
                    }else{
                        return false;
                    }


                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default: //unknown case
                    return false;

            }
        }
    }

    private class InternetClass extends AsyncTask<String, Void, ToyList> {

        private ProgressDialog dialog;

        public InternetClass(SearchActivity activity) {
            dialog = new ProgressDialog(activity);
        }


        @Override
        protected ToyList doInBackground(String ... url) {
            return openTextSource(url);
        }

        protected ToyList openTextSource(String ... url1) {

            ByteArrayOutputStream bos;
            HttpURLConnection urlConnection = null;
            URL url = null;
            ToyList toyList = new ToyList();
            try {
                url = new URL(textSource);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // give it 15 seconds to respond
                urlConnection.setReadTimeout(15 * 1000);
                urlConnection.connect();

                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                bos = readStream(in);

                byte[] bytes = bos.toByteArray();
                toyList = new ToyList(bytes, bytes.length);

            } catch (IOException e) {
                e.printStackTrace();

            }
            return toyList;
        }

        private ByteArrayOutputStream readStream(BufferedInputStream is) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            try {

                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo;
            } catch (IOException e) {
                return bo;
            }

        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Downloading toys...");
            dialog.show();
        }

        protected void onPostExecute(ToyList toyList) {
            buttons = toyList;

            ToyListAdapter arrayAdapter = new ToyListAdapter(SearchActivity.this, buttons);
            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemLongClickListener(listSourceItemLongClickListener);
            lv.setOnDragListener(myDragEventListener);
            targetLayout.setOnDragListener(myDragEventListener);


            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

}
