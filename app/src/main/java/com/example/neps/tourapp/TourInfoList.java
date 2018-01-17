package com.example.neps.tourapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TourInfoList extends Activity {
    int sigunguCode;
    String locName;
    String cat1;
    String category;
    ArrayList<String> menuList;

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    Bitmap bitmap;
    Context context;
    ArrayList<ImageItem> imageItemList = new ArrayList<>();
    MakeList makeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_info_list);

        Intent intent = getIntent();
        sigunguCode = intent.getIntExtra("sigunguCode", 0);
        locName = intent.getStringExtra("locName");
        category = intent.getStringExtra("category");
        cat1 = intent.getStringExtra("cat1");
        context = this;
        setTitle(locName +" - 카테고리 - "+category);
        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(context, R.layout.grid_item_layout, imageItemList);
        gridView.setAdapter(gridAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(context, DetailCommon.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("url", item.getUrl());
                intent.putExtra("contentId", item.getContentId());
                intent.putExtra("contenttypeId", item.getContenttypeId());
                startActivity(intent);
            }
        });

        makeList = new MakeList();
        makeList.execute();
    }

    // 비동기 지역 리스트 생성
    class MakeList extends AsyncTask<String , Void , Void> {

        // 투어 API에서 지역 리스트 GET
        protected Void doInBackground(String... params) {   //여기서 비트맵 리스트를 만들어주자.

            int pageNumber = 1;

            while (true){
                try {
                    String result = TourAPI.getAreaBasedList(5, pageNumber, sigunguCode, cat1);
                    JSONObject json = null;
                    json = new JSONObject(result);
                    JSONObject response = json.getJSONObject("response");
                    JSONObject body = response.getJSONObject("body");
                    
                    //!!!!!!더이상 가져올 비트맵이 없는경우 추가를 중단한다.!!!!!!
                    if(body.get("items").equals("")){
                        System.out.println("items가 비어있어서 종료합니다.");
                        break;
                    }

                    JSONObject items = body.getJSONObject("items");
                    JSONArray arr = items.getJSONArray("item"); // 관광지 정보가 여러개 들어있는 배열

                    for (int i = 0; i < arr.length(); i++) //(int i = 0; i < arr.length(); i++)
                    {
                        JSONObject oneItem = arr.getJSONObject(i);

                        if (oneItem.has("firstimage")) {
                            String imageUrl = oneItem.getString("firstimage");
                            String name = oneItem.getString("title");
                            String contentId = oneItem.getString("contentid");
                            String contenttypeId = oneItem.getString("contenttypeid");

                            URL url = new URL(imageUrl);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);

                            imageItemList.add(new ImageItem(bitmap, name, imageUrl, contentId, contenttypeId)); // 이미지 객체를 리스트로 더함.

                            conn.disconnect();
                            publishProgress();
                        } else {
                            String imageUrl = "noImage";
                            String name = oneItem.getString("title");
                            String contentId = oneItem.getString("contentid");
                            String contenttypeId = oneItem.getString("contenttypeid");

                            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_img2);
                            bitmap = drawable.getBitmap();

                            imageItemList.add(new ImageItem(bitmap, name, imageUrl, contentId, contenttypeId));
                            publishProgress();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                pageNumber++;
            }
            return null;
        }

        private void publishProgress(ArrayList<ImageItem> imageItemList) {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            gridAdapter.notifyDataSetChanged();
        }



        // 작업 결과로 받아온 정보를 파싱하여 LIST VIEW에 정보 출력
        protected void onPostExecute(ArrayList<ImageItem> imageItemList) {
            //여기서 아답터 설정을 해준다.

        }
    }

}
