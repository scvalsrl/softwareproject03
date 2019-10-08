package com.example.pssin.auction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);

        ImageButton subcategoryMotorCycle = (ImageButton)findViewById(R.id.subCategoryButtonMotorCycle);

        subcategoryMotorCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("거노 : ", "클릭했다!!!!!!!!!!!!!!!!!!!!!!!!!");
                // Intent motorIntent = new Intent(SubMainActivity.this,SubCategoryMotorCycle.class);
                //SubMainActivity.this.startActivity(motorIntent);
                BackgroundTask start = new BackgroundTask();
                start.execute();
            }
        });

    }
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
            Intent intent = new Intent(SubMainActivity.this,SubCategoryMotorCycleActivity.class);
            intent.putExtra("userList",result);
            intent.putExtra("accountID", MainIntent.getStringExtra("accountID"));
            SubMainActivity.this.startActivity(intent);
        }
    }




}
