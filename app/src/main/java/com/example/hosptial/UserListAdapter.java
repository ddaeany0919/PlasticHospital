package com.example.hosptial;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;

import java.util.List;

//위 실행화면에서 각 리스트뷰에 버튼을 추가했는데 그 버튼을 선언 후 리스너를 등록해줍니다. 이때 버튼이 눌리면 PHP서버로 넘겨줄 데이터를 설정해줍니다.
//커스 BaseAdapter 갖다대고 ATL+엔터 쳐서 Implement methods 받은면된다
public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList;
    private Activity parentActivity;//회원삭제 강의때 추가
    private List<User> saveList;//회원검색 강의때 추가

    //생성자를 만들어 준다. (Activity parentActivity 자식이 불러온 부모액티비티 추가)
    //여기서 Activity parentActivity가 추가됨 회원삭제 및 관리자기능 예제
    public UserListAdapter(Context context,List<User> userList,Activity parentActivity,List<User> saveList) {
            this.context = context;
            this.userList = userList;
        this.parentActivity = parentActivity;//회원삭제 강의때 추가
        this.saveList = saveList;//회원검색 강의때 추가
    }

    public UserListAdapter(Context applicationContext, List<User> userList, ManagementActivity managementActivity) {
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    //특정한 사용자를 반환 할수 있도록 한다.
    public Object getItem(int i) {
        return userList.get(i);
    }
    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    //가장 중요한 부분
    //int i 에서 final int i 로 바뀜 이유는 deleteButton.setOnClickListener에서 이 값을 참조하기 때문
    //하나의 사용자에 대한 뷰를 보여준다.
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View v = View.inflate(context, R.layout.user,null);
        //뷰에 다음 컴포넌트들을 연결시켜줌
        //final추가 안붙이면 에러남 리스너로 전달하고 싶은 지역변수는 final로 처리해야됨
        final TextView userID = (TextView) v.findViewById(R.id.userID);
        TextView userPassword = (TextView) v.findViewById(R.id.userPassword);
        TextView userName = (TextView) v.findViewById(R.id.userName);
        TextView userBirth = (TextView) v.findViewById(R.id.userBirth);
        TextView userEmail = (TextView) v.findViewById(R.id.userEmail);
        TextView userAddress = (TextView) v.findViewById(R.id.userAddress);
        TextView userPhone = (TextView) v.findViewById(R.id.userPhone);
        TextView userGender = (TextView) v.findViewById(R.id.userGender);


        //특정위치에 있는 유저를 가져온다.
        userID.setText(userList.get(i).getUserID());
        userPassword.setText(userList.get(i).getUserPassword());
        userName.setText(userList.get(i).getUserName());
        userBirth.setText(userList.get(i).getUserBirth());
        userEmail.setText(userList.get(i).getUserEmail());
        userAddress.setText(userList.get(i).getUserAddress());
        userPhone.setText(userList.get(i).getUserPhone());
        userGender.setText(userList.get(i).getUserGender());

        //특정유저의 아디값을 반환 할수 있게끔 한다.
        v.setTag(userList.get(i).getUserID());

        //삭제버튼 이벤트시작 구간(삭제 버튼 객체 생성)
        Button deleteButton = (Button) v.findViewById(R.id.deleteButton); //삭제버튼 객체 선언
        deleteButton.setOnClickListener(new View.OnClickListener() { //삭제버튼 이벤트줘서  함수 실행하도록 한다.
            @Override
            public void onClick(View v) { //위에 삭제버튼 클릭했을때 레스폰스 리스너를 만들어 지도록 한다.
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { //특정 웹사이트로 부터 레스폰스가 전달이 되었을때
                        try {
                            JSONObject jsonResponse = new JSONObject(response); //레스폰스 값을 받을수 있도록 한다.
                            boolean success = jsonResponse.getBoolean("success");//ㅅ레스폰스에서 받아온 값이 "success" 값을 가지고 있다면.(서버로부터 값을 받은 것을 의미함)
                            if (success) {
                                //유저검색 소스코딩 시작
                                userList.remove(i); //유저 리스트에서 해당부분을 지워주기 위해
                                for(int i= 0; i < saveList.size(); i++)//saverList를 찾아서 해줘야됨 이게 기준이기 때문임
                                {
                                    if (saveList.get(i).getUserID().equals(userID.getText().toString()))
                                    {
                                        saveList.remove(i);
                                        break;
                                    }
                                }
                                //유저검색 소스코딩 끝

                                notifyDataSetChanged(); //데이타가 변경되었다고 하는것을 어댑터에 알려주면 된다.
                            }
                        }
                        catch (Exception e) { //오류가 발생했을경우
                            e.printStackTrace(); // 오류를 출력해라
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity); //위에 parenActivity 변수를 선언해주고 생성자부분에도 매칭시켜준다.
                queue.add(deleteRequest);
            }
        });

        return v;
    }
}