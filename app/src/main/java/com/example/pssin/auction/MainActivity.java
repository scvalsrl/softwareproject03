package com.example.pssin.auction;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);


        final ImageButton categoryFixButton = findViewById(R.id.categoryFixButton);

        categoryFixButton.setOnClickListener(v -> {
            Intent loginIntent = getIntent();
            Intent fixIntent = new Intent(MainActivity.this, SubMainActivity.class);
            fixIntent.putExtra("accountID", loginIntent.getStringExtra("accountID"));
            MainActivity.this.startActivity(fixIntent);
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {

                    case R.id.nav_list:

                        new BackgroundTask().execute();
                        break;

                    case R.id.nav_search:
                        Intent Intent = getIntent();
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        intent.putExtra("accountID", Intent.getStringExtra("accountID"));
                        MainActivity.this.startActivity(intent);
                        break;

                    case R.id.nav_search2:

                        break;
                }
                return true;
            };




    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        final String target = "http://pssin1.cafe24.com/category.php";

        @Override
        protected void onPreExecute(){
            //초기화하는부분
        }

        @Override
        protected String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) !=null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                //해당문자열의 집합 반환
                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
            }
            //오류발생시 null
            return null;
        }
        @Override
        public  void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }


        @Override
        public void onPostExecute(String result){
            Intent MainIntent = getIntent();
            Intent intent = new Intent(MainActivity.this,SubCategoryMotorCycleActivity.class);
            intent.putExtra("userList",result);
            intent.putExtra("accountID", MainIntent.getStringExtra("accountID"));
            MainActivity.this.startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }
}