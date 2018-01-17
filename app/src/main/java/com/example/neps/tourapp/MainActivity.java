package com.example.neps.tourapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity{

    Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (!wifi.isConnected() && !mobile.isConnected()) {
            Intent intent = new Intent(getApplicationContext(), UnconnectedActivity.class);
            startActivity(intent);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        setTitle("강원여행수첩");

        bookBtn = (Button) findViewById(R.id.stamp);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyTourBookActivity.class);
                startActivity(intent);
            }
        });




        //권한부분
        ////////////////////////////////////////////
        int fineloc_permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int call_permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);


        if(fineloc_permissionCheck != PackageManager.PERMISSION_GRANTED && call_permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
        }
        else if(fineloc_permissionCheck != PackageManager.PERMISSION_GRANTED && call_permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        else if(fineloc_permissionCheck == PackageManager.PERMISSION_GRANTED && call_permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 3);
        }
        /////////////////////////////////////////////

        new MakeList().execute();
    }

  

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode)
            {
                case 1:
                    if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED))
                    {
                        Toast.makeText(this, "권한을 모두 승인하여야 정상적인 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "위치권한을 승인하여야 정상적인 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case 3:
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "전화 권한을 승인하여야 정상적인 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }


        }

    // 비동기 지역 리스트 생성
    class MakeList extends AsyncTask<String , Void , String> {

        // 투어 API에서 지역 리스트 GET
        protected String doInBackground(String... params) {
            try {
                String result = TourAPI.getGanwonLocationList(100, 1);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Error";
        }

        // 작업 결과로 받아온 정보를 파싱하여 LIST VIEW에 정보 출력
        protected void onPostExecute(String result) {
            final ArrayList<String> menu = test(result);
             /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            final ImageButton bt1=(ImageButton)findViewById(R.id.imageButton93);

            bt1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",1);
                    intent.putExtra("locName", menu.get(0));
                    startActivity(intent);
                }
            });
            final ImageButton bt2=(ImageButton)findViewById(R.id.imageButton86);

            bt2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",2);
                    intent.putExtra("locName", menu.get(1));
                    startActivity(intent);
                }
            });
            final ImageButton bt3=(ImageButton)findViewById(R.id.imageButton88);

            bt3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",3);
                    intent.putExtra("locName", menu.get(2));
                    startActivity(intent);
                }
            });
            final ImageButton bt4=(ImageButton)findViewById(R.id.imageButton87);

            bt4.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",4);
                    intent.putExtra("locName", menu.get(3));
                    startActivity(intent);
                }
            });
            final ImageButton bt5=(ImageButton)findViewById(R.id.imageButton94);

            bt5.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",5);
                    intent.putExtra("locName", menu.get(4));
                    startActivity(intent);
                }
            });
            final ImageButton bt6=(ImageButton)findViewById(R.id.imageButton95);

            bt6.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",6);
                    intent.putExtra("locName", menu.get(5));
                    startActivity(intent);
                }
            });
            final ImageButton bt7=(ImageButton)findViewById(R.id.imageButton58);

            bt7.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",7);
                    intent.putExtra("locName", menu.get(6));
                    startActivity(intent);
                }
            });
            final ImageButton bt8=(ImageButton)findViewById(R.id.imageButton104);

            bt8.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",8);
                    intent.putExtra("locName", menu.get(7));
                    startActivity(intent);
                }
            });
            final ImageButton bt9=(ImageButton)findViewById(R.id.imageButton97);

            bt9.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",9);
                    intent.putExtra("locName", menu.get(8));
                    startActivity(intent);
                }
            });
            final ImageButton bt10=(ImageButton)findViewById(R.id.imageButton108);

            bt10.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",10);
                    intent.putExtra("locName", menu.get(9));
                    startActivity(intent);
                }
            });
            final ImageButton bt11=(ImageButton)findViewById(R.id.imageButton105);

            bt11.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",11);
                    intent.putExtra("locName", menu.get(10));
                    startActivity(intent);
                }
            });
            final ImageButton bt12=(ImageButton)findViewById(R.id.imageButton109);

            bt12.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",12);
                    intent.putExtra("locName", menu.get(11));
                    startActivity(intent);
                }
            });
            final ImageButton bt13=(ImageButton)findViewById(R.id.imageButton101);

            bt13.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",13);
                    intent.putExtra("locName", menu.get(12));
                    startActivity(intent);
                }
            });

            final ImageButton bt14=(ImageButton)findViewById(R.id.imageButton107);

            bt14.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",14);
                    intent.putExtra("locName", menu.get(13));
                    startActivity(intent);
                }
            });final ImageButton bt15=(ImageButton)findViewById(R.id.imageButton106);

            bt15.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",15);
                    intent.putExtra("locName", menu.get(14));
                    startActivity(intent);
                }
            });final ImageButton bt16=(ImageButton)findViewById(R.id.imageButton102);

            bt16.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",16);
                    intent.putExtra("locName", menu.get(15));
                    startActivity(intent);
                }
            });

            final ImageButton bt17=(ImageButton)findViewById(R.id.imageButton110);

            bt17.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",17);
                    intent.putExtra("locName", menu.get(16));
                    startActivity(intent);
                }
            });

            final ImageButton bt18=(ImageButton)findViewById(R.id.imageButton111);

            bt18.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    intent.putExtra("sigunguCode",18);
                    intent.putExtra("locName", menu.get(17));
                    startActivity(intent);
                }
            });


            /*


            <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="8dp"
        android:hint="Search..."
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        tools:layout_editor_absoluteY="8dp" />



            EditText editTextFilter = (EditText) findViewById(R.id.editText) ;


            editTextFilter.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable edit) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //adapter.getFilter().filter(s);
                }
            }) ;
            */
        }

        // 파싱 결과정보를 리턴
        public ArrayList<String> test(String result) {
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                JSONObject response = json.getJSONObject("response");
                JSONObject body = response.getJSONObject("body");
                JSONObject items = body.getJSONObject("items");
                JSONArray arr = items.getJSONArray("item");

                ArrayList<String> menuList = new ArrayList<String>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject name = arr.getJSONObject(i);
                    String menuName = name.getString("name");
                    menuList.add(menuName);
                }

                return menuList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}