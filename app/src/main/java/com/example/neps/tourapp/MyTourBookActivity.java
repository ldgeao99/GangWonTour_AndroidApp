package com.example.neps.tourapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyTourBookActivity extends Activity {
    ArrayList<String> titleList;
    ArrayList<String> contentIdList;
    ArrayList<String> contentTypeIdList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour_book);
        context = this;
        setTitle("나의여행도감");

        final DBManager dbManager = new DBManager(context, "stamp.db", null, 1);

        titleList = new ArrayList<>();
        contentIdList = new ArrayList<>();
        contentTypeIdList = new ArrayList<>();

        String items = dbManager.getData();

        if (items != "") {
            String[] itemArr = items.split("\n");

            for (String item : itemArr) {
                String[] div = item.split("#");
                titleList.add(div[1]);
                contentIdList.add(div[2]);
                String div3 = div[3].split("\n")[0];
                contentTypeIdList.add(div3);
                System.out.println("[MYDEBUG]" + div[1] + "/" + div[2] + "/" + div[3]);
            }

            final ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, titleList);
            final ListView listview = (ListView) findViewById(R.id.listview1) ;
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, DetailCommon.class);
                    intent.putExtra("title", titleList.get(position));
                    intent.putExtra("contentId", contentIdList.get(position));
                    intent.putExtra("contenttypeId", contentTypeIdList.get(position));
                    startActivity(intent);
                }
            });
        }
    }
}
