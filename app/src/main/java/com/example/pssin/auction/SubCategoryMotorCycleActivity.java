package com.example.pssin.auction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryMotorCycleActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomnav;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_motor_cycle);

        toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바설정
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setSelectedItemId(R.id.nav_list);


        final Intent intent = getIntent();
        //초기화를 해줘야지 실행이된다
        //상속받아본다 만들어둔 클래스를
        listView = (ListView) findViewById(R.id.listView);
        final List<Category> userList = new ArrayList<Category>();
        final Button salebtn = (Button) findViewById(R.id.salebtn);

        // userList.add(new Category("1","2","3","4","5","6","7"));
        // userList.add(new Category("1","2","3","4","5","6","7"));
        CategoryListAdapter adapter = new CategoryListAdapter(getApplicationContext(), userList);
        listView.setAdapter(adapter);
        //userList.add(new Category("1","2","3","4","5","6","7"));

        try{
            Log.i(this.getClass().getName() + "거노, ", "목록 불러오기 도전!");
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            String a = String.valueOf(jsonArray.length());
            int lengthJS = jsonArray.length();
            int count =0;

            String boardNumber = "";
            String id = "";
            String title = "";
            String category = "";
            String tradeLocation = "";
            String auctionStartTime = "";
            String auctionTimeLimit = "";
            while(count<lengthJS)
            {

                JSONObject object = jsonArray.getJSONObject(count);

                boardNumber = object.getString("boardNumber");
                id = object.getString("id");
                title = object.getString("title");
                category = object.getString("category");
                tradeLocation = object.getString("tradeLocation");
                auctionStartTime = object.getString("auctionStartTime");
                auctionTimeLimit = object.getString("auctionTimeLimit");
                Category user = new Category(boardNumber,id,title,category,tradeLocation,auctionStartTime,auctionTimeLimit);
                userList.add(user);
                count++;

            }
            Log.i(this.getClass().getName() + "거노, ", "목록 불러오기 성공!");

        } catch (Exception e){
            Log.e(this.getClass().getName() + "거노, ", "목록 불러오기 실패 ㅜㅜ");
            e.printStackTrace();
        }

        salebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.getClass().getName() + "거노, ", "버튼 누른다? 진짜 누른다?");

                Intent saleIntent = new Intent(SubCategoryMotorCycleActivity.this, WriteBoardActivity.class);
                saleIntent.putExtra("accountID", intent.getStringExtra("accountID"));
                SubCategoryMotorCycleActivity.this.startActivity(saleIntent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(SubCategoryMotorCycleActivity.this, ViewActivity.class);
                //  intent.putExtra("id", id);
                SubCategoryMotorCycleActivity.this.startActivity(intent);
                // 화면전환 넣기 //

            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.nav_home:

                            Intent MainIntent = getIntent();
                            Intent intent = new Intent(SubCategoryMotorCycleActivity.this,MainActivity.class);
                            //intent.putExtra("userList",result);
                            //  intent.putExtra("accountID", MainIntent.getStringExtra("accountID"));
                            SubCategoryMotorCycleActivity.this.startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0);
                            break;

                        case R.id.nav_search:

                            break;

                        case R.id.nav_search2:

                            break;
                    }
                    return true;
                }

            };


}