package com.example.hosptial;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import com.android.volley.Response;

import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //강의에서 final을 추가시켜줌
        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final Button loginbtn = (Button)findViewById(R.id.loginButton);
        final TextView registerbtn = (TextView)findViewById(R.id.registerButton);

        registerbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                //4. 콜백 처리부분(volley 사용을 위한 ResponseListener 구현 부분)
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            Toast.makeText(getApplicationContext(), "success"+success, Toast.LENGTH_SHORT).show();

                            //서버에서 보내준 값이 true이면?
                            if(success){
                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                String userID = jsonResponse.getString("userID");
                                String userPassword = jsonResponse.getString("userPassword");

                                //로그인에 성공했으므로 MainActivity로 넘어감
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                LoginActivity.this.startActivity(intent);

                            }else{//로그인 실패시

                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다. 아이디,비밀번호를 다시 확인하세요")
                                        .setNegativeButton("다시시도", null)
                                        .create()
                                        .show();
                            }

                        }catch(JSONException e){

                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}