package com.example.pssin.auction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
    EditText titleText;
    LinearLayout lay1;
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board);

        titleText = (EditText) findViewById(R.id.titleText);
        lay1 = (LinearLayout)findViewById(R.id.lay1);
        txt1 = (TextView)findViewById(R.id.txt1);

        // 경매 시작가
        final ArrayAdapter selectCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.selectCategory, android.R.layout.simple_spinner_item);
        selectCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 등록 버튼
        final Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SubCategoryMotorCycleIntent = getIntent();
                final String id = SubCategoryMotorCycleIntent.getStringExtra("accountID");
                final String title = titleText.getText().toString();
                String temp_location = "";

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

        lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteBoardActivity.this,PopUp_1_Activity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);


            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                txt1.setText(result);
            }
        }
    }






}

