package com.example.pssin.auction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class CompanyInquiryActivity extends AppCompatActivity {

    // 사업자등록번호 OPEN API 인증키
    String serviceKey = "0ZVyZTBXZaOwhGBZ9llsGC3F7DCgMOrqsTI59ym6Ny1AUH%2Bd7Mq80hT8mKR3aE0h6Xz6o1lIDnRJRu%2B4xWlLdQ%3D%3D";
    String result     = "";
    private TextView edtXml;
    final int CONN_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_inquiry);
        edtXml = findViewById(R.id.textView5);

        /*
            ldong_addr_mgpl_dg_cd           : 법정동주소광역시도코드
            ldong_addr_mgpl_sggu_cd         : 법정동주소시군구코드
            ldong_addr_mgpl_sggu_emd_cd     : 법정동주소읍면동코드
            wkpl_nm                         : 사업장명
            bzowr_rgst_no                   : 사업자등록번호
            confirmButton                   : 확인버튼
            cancelButton                    : 취소버튼
         */
        EditText ldong_addr_mgpl_dg_cd        = findViewById(R.id.ldong_addr_mgpl_dg_cd);
        EditText ldong_addr_mgpl_sggu_cd      = findViewById(R.id.ldong_addr_mgpl_sggu_cd);
        EditText ldong_addr_mgpl_sggu_emd_cd  = findViewById(R.id.ldong_addr_mgpl_sggu_emd_cd);
        EditText wkpl_nm                      = findViewById(R.id.wkpl_nm);
        EditText bzowr_rgst_no                = findViewById(R.id.bzowr_rgst_no);
        Button confirmButton                  = findViewById(R.id.confirmButton);
        Button cancelButton                   = findViewById(R.id.cancelButton);

        // 확인버튼을 눌렀을 때 동작하는 부분
        confirmButton.setOnClickListener(v -> {
            String string_ldong_addr_mgpl_dg_cd         = ldong_addr_mgpl_dg_cd.getText().toString();
            String string_ldong_addr_mgpl_sggu_cd       = ldong_addr_mgpl_sggu_cd.getText().toString();
            String string_ldong_addr_mgpl_sggu_emd_cd   = ldong_addr_mgpl_sggu_emd_cd.getText().toString();
            String string_wkpl_nm                       = wkpl_nm.getText().toString();
            String string_bzowr_rgst_no                 = bzowr_rgst_no.getText().toString();

            // Xml 데이터를 가져오기 위해 쓰레드 생성
            new Thread() {
                public void run() {
                    result = getXmlParser(string_ldong_addr_mgpl_dg_cd, string_ldong_addr_mgpl_sggu_cd,
                            string_ldong_addr_mgpl_sggu_emd_cd, string_wkpl_nm, string_bzowr_rgst_no);

                    Bundle bun = new Bundle();
                    bun.putString("XML_DATA", result);

                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);

                    Log.i("사업자등록번호 조회", String.valueOf(result.contains("seq")));
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    if(result.contains("seq")) { // 사업자등록번호 조회 성공
                        Log.i("건호 ", "사업자등록번호 조회 성공");
                        mHandler.postDelayed(() -> {
                            // 사용하고자 하는 코드
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompanyInquiryActivity.this);
                            builder.setMessage("조회 성공.")
                                    .setPositiveButton("확인", (dialog, which) -> {
                                        Intent intent = new Intent(CompanyInquiryActivity.this, RegisterCompanyMemberActivity.class);
                                        intent.putExtra("companyNumberInquirySuccess", true);
                                        intent.putExtra("companyNameText", getIntent().getStringExtra("companyNameText"));
                                        intent.putExtra("idText", getIntent().getStringExtra("idText"));
                                        intent.putExtra("passwordText", getIntent().getStringExtra("passwordText"));
                                        intent.putExtra("phoneNumberTextLeftPosition", getIntent().getIntExtra("phoneNumberTextLeftPosition", 0));
                                        intent.putExtra("phoneNumberTextLeft", getIntent().getStringExtra("phoneNumberTextLeft"));
                                        intent.putExtra("phoneNumberTextRight", getIntent().getStringExtra("phoneNumberTextRight"));
                                        intent.putExtra("phoneNumberInquirySuccess", getIntent().getBooleanExtra("phoneNumberInquirySuccess", false));
                                        CompanyInquiryActivity.this.startActivity(intent);
                                    })
                                    .create()
                                    .show();
                        }, 0);


                    } else {                     // 사업자등록번호 조회 실패
                        Log.i("건호", "사업자등록번호 조회 실패");
                        mHandler.postDelayed(() -> {
                            // 사용하고자 하는 코드
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompanyInquiryActivity.this);
                            builder.setMessage("조회에 실패했습니다.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }, 0);
                    }
                }
            }.start();
        });


        // 취소버튼을 눌렀을 때 동작하는 부분
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyInquiryActivity.this, RegisterCompanyMemberActivity.class);
            CompanyInquiryActivity.this.startActivity(intent);
            finish();
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String inquiryXml = bun.getString("XML_DATA", "it's NULL");
            Log.i("사업자등록번호 조회 핸들러", inquiryXml);
        }
    };

    String getXmlParser(String string_ldong_addr_mgpl_dg_cd, String string_ldong_addr_mgpl_sggu_cd, String string_ldong_addr_mgpl_sggu_emd_cd, String string_wkpl_nm, String string_bzowr_rgst_no) {
        String retStr = "";
        String ldong_addr_mgpl_dg_cd        = string_ldong_addr_mgpl_dg_cd;
        String ldong_addr_mgpl_sggu_cd      = string_ldong_addr_mgpl_sggu_cd;
        String ldong_addr_mgpl_sggu_emd_cd  = string_ldong_addr_mgpl_sggu_emd_cd;
        String wkpl_nm                      = string_wkpl_nm;
        String bzowr_rgst_no                = string_bzowr_rgst_no;

        String queryUrl = "http://apis.data.go.kr/B552015/NpsBplcInfoInqireService/getBassInfoSearch?"
                        + "ldong_addr_mgpl_dg_cd=" +         ldong_addr_mgpl_dg_cd
                        + "&ldong_addr_mgpl_sggu_cd=" +      ldong_addr_mgpl_sggu_cd
                        + "&ldong_addr_mgpl_sggu_emd_cd=" +  ldong_addr_mgpl_sggu_emd_cd
                        + "&wkpl_nm=" +                      wkpl_nm
                        + "&bzowr_rgst_no=" +                bzowr_rgst_no
                        + "&pageNo=10"
                        + "&startPage=10"
                        + "&numOfRows=1"
                        + "&pageSize=1"
                        + "&serviceKey=" +                   serviceKey;
        Log.i("건호 ", queryUrl);
        BufferedReader br = null;
        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.

            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");
            urlconnection.setConnectTimeout(CONN_TIME);
            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));

            String line;
            while((line = br.readLine()) != null)
            {
                line = line.replaceAll("\"", "\'");
                retStr = retStr + line + "\n";
            }
            Log.i("문자열 확인임", retStr + " " + retStr.contains("seq"));
        } catch (IOException ioe) {
            Log.e(this.getClass().getName() + "건호", "사업자등록번호 조회 실패(IO), " + ioe.getMessage());
        } catch (Exception e) {
            Log.e(this.getClass().getName() + "건호", "사업자등록번호 조회 실패(기타), " + e.getMessage());
            e.printStackTrace();
        }

        return retStr;
    }
}
