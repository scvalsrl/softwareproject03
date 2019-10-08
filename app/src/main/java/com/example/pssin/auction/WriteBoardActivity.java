package com.example.pssin.auction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class WriteBoardActivity extends AppCompatActivity {
    String category = "";
    boolean is_tradeLocation_auto = true;   // 직접입력 누르면 false
    String tradeLocation = "";
    String auctionTimeLimit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        // 글 제목
        final EditText titleText = (EditText) findViewById(R.id.titleText);
        // 경매 시작가
        final EditText auctionStartingPrice = (EditText) findViewById(R.id.auctionStartingPrice);





        // Spinner
        final Spinner selectCategorySpinner = (Spinner)findViewById(R.id.selectCategorySpinner);
        final ArrayAdapter selectCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.selectCategory, android.R.layout.simple_spinner_item);
        selectCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategorySpinner.setAdapter(selectCategoryAdapter);

        //spinner 이벤트 리스너
        selectCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectCategorySpinner.getSelectedItemPosition() > 0) {
                    //선택된 항목
                    Log.v("알림",selectCategorySpinner.getSelectedItem().toString()+ "is selected");
                    category = selectCategorySpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // 거래 지역 ( 택배, 계정위치, 직접입력)
        final RadioGroup radio_location = (RadioGroup)findViewById(R.id.radio_location);
        final RadioButton radio_location0 = (RadioButton) findViewById(R.id.radio_location0);
        final RadioButton radio_location1 = (RadioButton) findViewById(R.id.radio_location1);
        final RadioButton radio_location2 = (RadioButton) findViewById(R.id.radio_location2);

        // 거래지역) 직접입력 눌렀을 때
        final EditText locationDirectInput = (EditText) findViewById(R.id.locationDirectInput);
        this.setUseableEditText(locationDirectInput, false);


        radio_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_location0){
                    WriteBoardActivity.this.setUseableEditText(locationDirectInput, false);
                }
                else if(checkedId == R.id.radio_location1){
                    WriteBoardActivity.this.setUseableEditText(locationDirectInput, false);
                }
                else if(checkedId == R.id.radio_location2){
                    WriteBoardActivity.this.setUseableEditText(locationDirectInput, true);
                }

            }
        });

        // 제한 시간 ( 1시간, 3시간, 5시간 )
        final RadioGroup radio_time = (RadioGroup) findViewById(R.id.radio_time);
        final RadioButton radio_timeLimit1 = (RadioButton) findViewById(R.id.radio_timeLimit1);
        final RadioButton radio_timeLimit3 = (RadioButton) findViewById(R.id.radio_timeLimit3);
        final RadioButton radio_timeLimit5 = (RadioButton) findViewById(R.id.radio_timeLimit5);

        // 등록 버튼
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        radio_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_timeLimit1){
                    auctionTimeLimit = "1";
                }
                else if(checkedId == R.id.radio_timeLimit3){
                    auctionTimeLimit = "3";
                }
                else if(checkedId == R.id.radio_timeLimit5){
                    auctionTimeLimit = "5";
                }

            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SubCategoryMotorCycleIntent = getIntent();
                final String id = SubCategoryMotorCycleIntent.getStringExtra("accountID");
                final String title = titleText.getText().toString();
                String temp_location = "";
                if(radio_location0.isChecked())
                    temp_location = radio_location0.getText().toString();
                else if(radio_location1.isChecked())
                    temp_location = radio_location1.getText().toString();
                else if(radio_location2.isChecked())
                    temp_location = locationDirectInput.getText().toString();

                final String tradeLocation = temp_location;

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.optBoolean("success", false);
                            if(success) // 글쓰기 성공!
                            {
                                Toast.makeText(WriteBoardActivity.this, tradeLocation, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteBoardActivity.this, SubMainActivity.class);
                                intent.putExtra("accountID", id);
                                WriteBoardActivity.this.startActivity(intent);
                            }
                            else // 글쓰기 실패...
                            {
                                Toast.makeText(WriteBoardActivity.this, "글쓰기 실패", Toast.LENGTH_SHORT).show();
                                /*
                                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBoardActivity.this);
                                builder.setMessage("로그인에 실패하였다!@")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                                */
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                WriteBoardRequest writeBoardRequest = new WriteBoardRequest(id, title, category, tradeLocation, auctionTimeLimit, responseListener);
                RequestQueue queue = Volley.newRequestQueue(WriteBoardActivity.this);
                queue.add(writeBoardRequest);
            }
        });
    }

    // 에딧 텍스트 활성화, 비활성화 기능
    private void setUseableEditText(EditText et, boolean useable) {
        et.setClickable(useable);
        et.setEnabled(useable);
        et.setFocusable(useable);
        et.setFocusableInTouchMode(useable);
        if(useable)
            et.setVisibility(View.VISIBLE);     // 활성화일 때는 보이기
        else
            et.setVisibility(View.INVISIBLE);   // 비활성화일 때는 숨기기
    }
}

