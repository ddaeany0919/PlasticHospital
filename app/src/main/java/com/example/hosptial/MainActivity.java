package com.example.hosptial;

import android.content.Intent;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;


import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView idText = (TextView) findViewById(R.id.idText);
        // TextView passwordText = (TextView) findViewById(R.id.passwordText);
        TextView welcome = (TextView) findViewById(R.id.welcomeMessage);
        Button managementButton = (Button) findViewById(R.id.managementButton);
        Button drivingButton = (Button) findViewById(R.id.drivingButton);
        Button calltaxiButton = (Button) findViewById(R.id.calltaxiButton);
        Button flowerButton = (Button) findViewById(R.id.flowerButton);
        Button deliveryButton = (Button) findViewById(R.id.deliveryButton);
        ImageView srView = (ImageView) findViewById(R.id.srView);
        ImageView flowerView = (ImageView) findViewById(R.id.flowerView);


        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String msg = "환영합니다," + userID + "님!";

        // idText.setText(userID);
        //passwordText.setText(userPassword);
        welcome.setText(msg);


        final int[] temp = {0};

        //전화걸기 구현   //010-3199-6200
        drivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-0000-0000"));
                startActivity(it);
            }
        });

        calltaxiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-0000-0000"));
                startActivity(it);
            }
        });
        flowerButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-0000-0000"));
                startActivity(it);
            }
        });
        deliveryButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-0000-0000"));
                startActivity(it);
            }
        });

        //웹페이지연결 구현
        srView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://210.180.118.29/phpweb/surgery/index.html"));
                startActivity(intent);
            }
        });
        flowerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.floweruu.com"));
                startActivity(intent);
            }
        });

        //admin 계정이 아니면 버튼 안보이게
        if(!userID.equals("admin")){

            //managementButton.setEnabled(false);
            managementButton.setVisibility(View.GONE);

        }

        //관리자 버튼이 눌리면 여기로 옴
        managementButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new BackgroundTask().execute();
            }
        });

    }

    //모든회원에 대한 정보를 가져오기 위한 쓰레드
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {

            //List.php은 파싱으로 가져올 웹페이지
            target = "http://cinhwa.cafe24.com/List.php";

        }

        @Override
        protected String doInBackground(Void... voids) {

            try{

                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함

                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함

                while((temp = bufferedReader.readLine()) != null){

                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌

                }
                //사용했던 것도 다 닫아줌

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            }catch(Exception e){

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);//파싱한 값을 넘겨줌
            MainActivity.this.startActivity(intent);//ManagementActivity로 넘어감
        }
    }
}