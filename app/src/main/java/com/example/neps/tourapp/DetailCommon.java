package com.example.neps.tourapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailCommon extends FragmentActivity {

    Bitmap bitmap;
    Context context;

    ImageView pictureImageView;
    TextView titleTextView;
    TextView addressTextView;
    TextView summaryTextView;
    TextView coordinateTextView;

    Button callBtn;

    String title;
    String url;
    String contentId;
    String contenttypeId;

    String mapx;
    String mapy;

    String tel;

    private GpsInfo gps;

    private GoogleMap mMap;

    Button btn1;

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        btn1 = (Button) findViewById(R.id.button01); //버튼

        //변수 초기화
        context = this;
        pictureImageView = (ImageView) findViewById(R.id.picture);
        titleTextView = (TextView) findViewById(R.id.title);
        addressTextView = (TextView) findViewById(R.id.address);
        summaryTextView = (TextView) findViewById(R.id.summary);
        callBtn = (Button) findViewById(R.id.telBtn);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        final DBManager dbManager = new DBManager(getApplicationContext(), "stamp.db", null, 1);

        btn1.setOnClickListener(new View.OnClickListener() {

            //거리 계산 함수
            private double deg2rad(double deg) {
                return (deg * Math.PI / 180.0);
            }
            private double rad2deg(double rad) {
                return (rad * 180 / Math.PI);
            }

            @Override
            public void onClick(View view) {
                //버튼 클릭시 인증기능
                Intent intent = getIntent();
                gps = new GpsInfo(DetailCommon.this);
                title = intent.getStringExtra("title");
                contentId = intent.getStringExtra("contentId");
                contenttypeId = intent.getStringExtra("contenttypeId");

                if( gps .isGetLocation()){

                    double mapx1=gps .getLongitude();// 현재위치 경도좌표
                    double mapy1=gps .getLatitude();// 현재위치 위도좌표

                    double mapx2=Double.parseDouble(mapx);// 관광지 경도좌표
                    double mapy2=Double.parseDouble(mapy);// 관광지 위도좌표

                    //거리 계산 공식
                    double theta = mapx1 - mapx2;
                    double dist = Math.sin(deg2rad(mapy1)) * Math.sin(deg2rad(mapy2)) + Math.cos(deg2rad(mapy1)) * Math.cos(deg2rad(mapy2)) * Math.cos(deg2rad(theta));

                    dist = Math.acos(dist);
                    dist = rad2deg(dist);
                    dist = dist * 60 * 1.1515;

                    dist = dist * 1.609344;

                    boolean RegistedVal = dbManager.exitedValue(title);

                    if(dist<=1)//1KM내에 있으면 인증 성공 아니면 인증 실패 ////////////////////////임시로 거리 출력
                    {
                        btn1.setText("도감추가 완료");
                        btn1.setEnabled(false);
                        Toast.makeText(getApplicationContext(),"도감에 등록되었습니다", Toast.LENGTH_LONG).show();
                        dbManager.insert(title, contentId, contenttypeId);
                    }
                    else
                    {
                        //dbManager.insert(title, contentId, contenttypeId); // !!!!!!나중에 지워줄 코드!!!!!!
                        Toast.makeText(getApplicationContext(),"거리가 너무 멀어요...", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    gps.showSettingsAlert();// GPS설정 실패시 뜨는 경고문
                }
            }
        });


        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        contentId = intent.getStringExtra("contentId");
        contenttypeId = intent.getStringExtra("contenttypeId");

        System.out.println(contentId + "!!!!!" + contenttypeId);
        setTitle(title);

        new MakeList().execute();
    }

    // 비동기 지역 리스트 생성
    class MakeList extends AsyncTask<String , Void , TempClassToTransferParams> implements OnMapReadyCallback {

        // 투어 API에서 지역 리스트 GET
        protected TempClassToTransferParams doInBackground(String... params) {   //여기서 비트맵 리스트를 만들어주자.

            JSONObject json = null;
            TempClassToTransferParams tcttfp = null;

            try {
                String result = TourAPI.getDetailCommon(contentId, contenttypeId);
                json = new JSONObject(result);
                System.out.print(json.toString());
                JSONObject response = json.getJSONObject("response");
                JSONObject body = response.getJSONObject("body");
                JSONObject items = body.getJSONObject("items");
                JSONObject item = items.getJSONObject("item");

                String imageUrl = "noImage";
                if (item.has("firstimage")) {
                    imageUrl = item.getString("firstimage");
                }

                String addr = "";
                if(item.has("addr1")) {
                    addr = item.getString("addr1");
                }

                mapx = "";
                if(item.has("mapx")){
                    mapx = item.getString("mapx");
                }

                mapy = "";
                if(item.has("mapy")){
                    mapy = item.getString("mapy");
                }

                tel = "none";
                if(item.has("tel")) {
                    System.out.println("[MYDEBUG]" + tel);
                    tel = item.getString("tel");
                    tel = tel.split("/")[0];
                    tel = tel.split("~")[0];
                    tel = tel.split(",")[0];

                    for (int i = 0; i < tel.length(); i++) {
                        tel = tel.replaceAll("[^0-9]", "");
                    }
                }

                String overview = item.getString("overview");

                if (!imageUrl.equals("noImage")) {
                    URL url = new URL(imageUrl);

                    HttpURLConnection conn =  (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    tcttfp = new TempClassToTransferParams(bitmap, addr, overview, mapx, mapy);

                    conn.disconnect();
                }
                else {
                    BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.default_img2);
                    bitmap = drawable.getBitmap();

                    tcttfp = new TempClassToTransferParams(bitmap, addr, overview, mapx, mapy);
                }

                return tcttfp;

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        // 작업 결과로 받아온 정보를 파싱하여 LIST VIEW에 정보 출력
        protected void onPostExecute(TempClassToTransferParams tcttfp) {
            titleTextView.setText(title);

            if (tcttfp != null) {
                pictureImageView.setImageBitmap(tcttfp.getImage());
                if(tcttfp.getAddr()=="")
                    addressTextView.setVisibility(View.GONE);
                else
                    addressTextView.setText(tcttfp.getAddr());

                summaryTextView.setText(Html.fromHtml(tcttfp.getSummary()));

                if(tcttfp.getMapx()!="" && tcttfp.getMapy()!="") {
                    //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                }
            }
            else
                System.out.print("문제 발생!!!!!!!");

            if (!tel.equals("none")) {
                callBtn.setText("전화걸기: " + tel);
            }
            else {
                callBtn.setText("전화번호 정보가 없습니다.");
                callBtn.setEnabled(false);
            }

            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    } else {
                        // Show rationale and request permission.
                    }

                }
            });

            final DBManager dbManager = new DBManager(getApplicationContext(), "stamp.db", null, 1);
            boolean registedVal = dbManager.exitedValue(title);
            if (registedVal) {
                btn1.setText("이미 등록되었습니다");
                btn1.setEnabled(false);
            }
        }

        public void onMapReady(GoogleMap googleMap) {
            // 구글 맵 객체를 불러온다.
            mMap = googleMap;

            // 위치 설정
            LatLng point = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));

            // 구글 맵에 표시할 마커에 대한 옵션 설정
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(point)
                    .title(title);

            mMap.addMarker(makerOptions);
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

            // 카메라 설정
            // 1: 세계, 5: 대륙, 10: 도시, 15: 거리, 20: 건물
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
        }
    }
}
