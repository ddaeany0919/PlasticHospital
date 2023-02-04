package com.example.hosptial;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    //사용자 정보 담을수 있는 변수 선언
    private ArrayAdapter adapter;
    private String userID;
    private String userPassword;
    private String userName;
    private String userBirth; //생년월일
    private String userEmail;
    private String UserAddress; //주소
    private String userPhone;
    private String userGender;
    private AlertDialog dialog;
    private boolean validate = false;
    //날짜 달력 보여주는 부분
    Calendar c;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //초기화 진행 아디,비밀번호 디자인 액티비티 매칭
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText birthText = (EditText) findViewById(R.id.birthText); //생년월일 변경
        final EditText emailText = (EditText) findViewById(R.id.emailText); //이메일 추가
        final EditText addressText = (EditText) findViewById(R.id.addressText); //주소 추가
        final EditText phoneText = (EditText) findViewById(R.id.phoneText); //전화번호 추가
        final Button birthButton = (Button) findViewById(R.id.birthButton);//생년월일 입력하기 위한 버튼


        //생년월일 체크구현
           birthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MARCH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) { //날짜를 정하고 설정버튼을 눌렸을때 호출되는 메소드
                        birthText.setText(year + "" + month + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //생년월일 체크구현




        //성별구분 구현
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);//초기화 진행 genderGroup 디자인 액티비티 매칭
        int genderGroupID = genderGroup.getCheckedRadioButtonId(); //선택이 남성인지 여성인지 확인
        userGender = ((RadioButton) findViewById(genderGroupID)).getText().toString();//genderGroup 디자인 액티비티 매칭
        //라이오 버튼을 클릭했을때 이벤트 구현
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });

        /*회원가입시 아이디가 사용가능한지 검증하는 부분
        문제가 하나라도 발생시 생길수 있는 소스 구현
        중복체크 버튼 구현 및 빈칸일때 오류 구현*/
        final Button validateButton = (Button) findViewById(R.id.validateButton);//초기화 진행 vaildteButton 다지인 액티비티 매칭
        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if (validate)//중복체크가 되있다면 바로 종료시킨다
                {
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면(아무런 내용이 없다면 오류를 실행시킨다)
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                //검증시작(정상적으로 아이디를 입력했다면(서버로부터 여기서 데이터를 받음))
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {

                        //중복체크에 성공하였다면
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));

                            }else{//사용할 수 없는 아이디라면(중복체크에 실패 하였다면)
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료
                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분(실지적으로 접속 할수 있도록 생성자를 하나 만들어 준다.)
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });


        //회원가입버튼 구현
        Button registerButton = (Button) findViewById(R.id.registerButton);//초기화 진행 registerButton 디자인 액티비티 매칭
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = nameText.getText().toString();
                //int userAge = Integer.parseInt(ageText.getText().toString());
                String userBirth = birthText.getText().toString();//생년월일 변경
                String userEmail = emailText.getText().toString();//이메일 추가
                //int userPhone = Integer.parseInt(phoneText.getText().toString());//전화번호 int로 추가
                String userAddress = addressText.getText().toString();//주소추가
                String userPhone = phoneText.getText().toString();



                //중복체크를 하지 않았다면 중복 체크해달라는 메세지 구현
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요 .")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                //그리고 하나라도 빈공간이 있다면
                if (userID.equals("") || userPassword.equals("") || userName.equals("") || userBirth.equals("") || userEmail.equals("") || userPhone.equals("") || userAddress.equals("") || userGender.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요 .")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }


                //아무런 문제가 없을경우 정상적으로 회원가입 구현
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRespons = new JSONObject(response);
                            boolean success = jsonRespons.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish(); //회원가입창 닫기
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        //오류가 발생 하였다면
                        catch (Exception e) {
                            e.printStackTrace();//회원가입 오류시 출력
                        }
                    }
                };

                //실지적으로 접속 할수 있도록 생성자를 하나 만들어 준다.
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword,userName,userBirth,userEmail,userAddress,userPhone,userGender,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }

        });
    }
    //회원등록창이 끄지게 되면 실행되는 함수
    @Override
    protected void onStop() {
        super.onStop();
        if(dialog !=null)
        {
            dialog.dismiss();
            dialog = null;
        }

    }
}




