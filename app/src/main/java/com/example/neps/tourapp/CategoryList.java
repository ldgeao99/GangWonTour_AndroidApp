package com.example.neps.tourapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryList extends Activity {
    int sigunguCode;
    String locName;
    ArrayList<String> menuList;
    ArrayList<String> codeList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        context = this;
        Intent intent = getIntent();
        sigunguCode = intent.getIntExtra("sigunguCode", 0);
        locName = intent.getStringExtra("locName");

        setTitle(locName + " - 카테고리");
        new MakeList1().execute();
    }

    // 비동기 지역 리스트 생성
    class MakeList1 extends AsyncTask<String , Void , String> {

        // 투어 API에서 지역 리스트 GET
        protected String doInBackground(String... params) {
            try {
                String result = TourAPI.getCategoryList();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Error";
        }

        // 작업 결과로 받아온 정보를 파싱하여 LIST VIEW에 정보 출력
        protected void onPostExecute(String result) {
            test(result);

            ArrayAdapter adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, menuList);
            ListView listview = (ListView) findViewById(R.id.listview1) ;
            listview.setAdapter(adapter) ;
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), TourInfoList.class);
                    intent.putExtra("sigunguCode", sigunguCode);
                    intent.putExtra("locName",locName);
                    intent.putExtra("category", menuList.get(position));
                    intent.putExtra("cat1", codeList.get(position));

                    startActivity(intent);
                }
            });
        }

        // 파싱 결과정보를 리턴
        public void test(String result) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONObject body = response.getJSONObject("body");
                JSONObject items = body.getJSONObject("items");
                JSONArray arr = items.getJSONArray("item");

                menuList = new ArrayList<String>();
                codeList = new ArrayList<String>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject name = arr.getJSONObject(i);
                    String menuName = name.getString("name");
                    String codeName = name.getString("code");
                    menuList.add(menuName);
                    codeList.add(codeName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
